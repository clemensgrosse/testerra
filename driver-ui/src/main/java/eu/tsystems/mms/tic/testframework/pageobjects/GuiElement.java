/*
 * Testerra
 *
 * (C) 2020, Peter Lehmann, T-Systems Multimedia Solutions GmbH, Deutsche Telekom AG
 *
 * Deutsche Telekom AG and all other contributors /
 * copyright owners license this file to you under the Apache
 * License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */
 package eu.tsystems.mms.tic.testframework.pageobjects;

import eu.tsystems.mms.tic.testframework.annotations.PageOptions;
import eu.tsystems.mms.tic.testframework.common.PropertyManager;
import eu.tsystems.mms.tic.testframework.constants.TesterraProperties;
import eu.tsystems.mms.tic.testframework.exceptions.SystemException;
import eu.tsystems.mms.tic.testframework.internal.Flags;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.Checkable;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.ConfiguredAssert;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.Nameable;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.asserts.ConfigurableGuiElementAssert;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.asserts.GuiElementAssert;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.asserts.GuiElementAssertDescriptionDecorator;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.asserts.GuiElementAssertHighlightDecorator;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.asserts.PerformanceTestGuiElementAssert;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.core.GuiElementCore;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.core.GuiElementCoreFrameAwareDecorator;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.core.GuiElementCoreSequenceDecorator;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.core.GuiElementData;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.core.GuiElementStatusCheck;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.core.GuiElementStatusCheckFrameAwareDecorator;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.creation.GuiElementCoreFactory;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.facade.DelayActionsGuiElementFacade;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.facade.GuiElementFacade;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.facade.GuiElementFacadeLoggingDecorator;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.facade.StandardGuiElementFacade;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.frames.FrameLogic;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.waiters.GuiElementWait;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.waiters.StandardGuiElementWait;
import eu.tsystems.mms.tic.testframework.webdrivermanager.WebDriverSessionsManager;
import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * GuiElement is the access point for most tests and is an extension of WebElement.
 * <p>
 * Authors: pele, rnhb
 */
public class GuiElement implements
    Checkable,
    GuiElementCore,
    Nameable
{

    private static final Map<String, GuiElementCoreFactory> coreFactories = new HashMap<>();

    public static void registerGuiElementCoreFactory(GuiElementCoreFactory guiElementCoreFactory, String... browsers) {
        LOGGER.debug("Registering " + guiElementCoreFactory.getClass().getSimpleName() + " for browsers " + String.join(",", browsers));

        for (String browser : browsers) {
            coreFactories.put(browser, guiElementCoreFactory);
        }
    }

    /**
     * logger. What a surprise. Glad this JavaDoc is here.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GuiElement.class);

    private boolean forcedStandardAsserts = false;

    @Deprecated
    public GuiElementAssert nonFunctionalAssert;

    private GuiElementAssert functionalAssert;

    private GuiElementAssert functionalStandardAssert;
    private GuiElementAssert functionalAssertCollector;
    private GuiElementAssert nonFunctionalAssertReplacement;
    /**
     * Facade for all parts of the lowest GuiElement
     */
    private GuiElementFacade guiElementFacade;

    private GuiElementCore guiElementCore;
    private GuiElementWait guiElementWait;
    private Locate locator;
    private final GuiElementData guiElementData;

    private GuiElement(GuiElementData guiElementData) {
        this.guiElementData = guiElementData;
        buildInternals(guiElementData.webDriver, guiElementData.by);
    }

    /**
     * Constructor with explicit web driver session.
     *
     * @param driver Actual driver object.
     * @param by     By locator.
     */
    public GuiElement(final WebDriver driver, final By by) {
        this(driver, by, null);
    }

    public GuiElement(final WebDriver driver, final Locate locator) {
        this(driver, locator, null);
    }

    /**
     * Constructor with frames and explicit webDriver session.
     *
     * @param locator     By locator.
     * @param frames frames containing the element
     * @param driver Driver Object.
     */
    public GuiElement(
        final WebDriver driver,
        final Locate locator,
        final GuiElement... frames
    ) {
        this(driver, locator.getBy(), frames);
        this.locator = locator;
    }

    public GuiElement(
        final WebDriver driver,
        final By by,
        final GuiElement... frames
    ) {
        FrameLogic frameLogic = null;
        if (frames != null && frames.length > 0) {
            frameLogic = new FrameLogic(driver, frames);
        }
        guiElementData = new GuiElementData(driver, "", frameLogic, by, this);
        buildInternals(driver, by);
        this.locator = Locate.by(by);
    }

    public Locate getLocator() {
        return locator;
    }

    private void buildInternals(WebDriver webDriver, By by) {
        // Create core depending on requested Browser
        String currentBrowser = WebDriverSessionsManager.getRequestedBrowser(webDriver).orElse(null);
        guiElementData.browser = currentBrowser;

        if (coreFactories.containsKey(currentBrowser)) {
            GuiElementCoreFactory guiElementCoreFactory = coreFactories.get(currentBrowser);
            guiElementCore = guiElementCoreFactory.create(by, webDriver, this.guiElementData);
        } else {
            throw new SystemException("No GuiElementCoreFactory registered for " + currentBrowser);
        }
        GuiElementStatusCheck guiElementStatusCheck = guiElementCore;

        if (this.guiElementData.hasFrameLogic()) {
            // if frames are set, the waiter should use frame switches when executing its sequences
            guiElementStatusCheck = new GuiElementStatusCheckFrameAwareDecorator(guiElementStatusCheck, this.guiElementData);
            guiElementCore = new GuiElementCoreFrameAwareDecorator(guiElementCore, this.guiElementData);
        }

        // guielement wait uses the core and not the facade
        guiElementWait = new StandardGuiElementWait(guiElementStatusCheck, this.guiElementData);

        // Wrap the core with sequence decorator, such that its methods are executed with sequence
        guiElementCore = new GuiElementCoreSequenceDecorator(guiElementCore, this.guiElementData);

        functionalStandardAssert = getGuiElementAssert(true, false, guiElementCore, guiElementWait, this.guiElementData);
        functionalAssertCollector = getGuiElementAssert(true, true, guiElementCore, guiElementWait, this.guiElementData);

        nonFunctionalAssert = getGuiElementAssert(false, false, guiElementCore, guiElementWait, this.guiElementData);
        nonFunctionalAssertReplacement = nonFunctionalAssert;

        initDefaultAssert();
    }

    private void initDefaultAssert() {
        if (!forcedStandardAsserts && Flags.GUIELEMENT_DEFAULT_ASSERT_IS_COLLECTOR) {
            functionalAssert = functionalAssertCollector;
        } else {
            functionalAssert = functionalStandardAssert;
        }

        guiElementFacade = getFacade(guiElementCore, guiElementWait, functionalAssert);
    }

    @Deprecated
    public void forceStandardAsserts() {
        forcedStandardAsserts = true;
        initDefaultAssert();
    }

    @Deprecated
    public void resetDefaultAsserts() {
        forcedStandardAsserts = false;
        initDefaultAssert();
    }

    private GuiElementAssert getGuiElementAssert(boolean functional, boolean collected,
                                                 GuiElementCore guiElementCore,
                                                 GuiElementWait guiElementWait,
                                                 GuiElementData guiElementData) {
        GuiElementAssert guiElementAssert;
        if (PropertyManager.getBooleanProperty(TesterraProperties.PERF_TEST, false)) {
            guiElementAssert = new PerformanceTestGuiElementAssert();
        } else {
            ConfiguredAssert configuredAssert = new ConfiguredAssert(functional, collected, guiElementData);
            guiElementAssert = new ConfigurableGuiElementAssert(guiElementCore, guiElementWait, configuredAssert, guiElementData);
            guiElementAssert = new GuiElementAssertHighlightDecorator(guiElementAssert, guiElementData);
        }
        return guiElementAssert;
    }

    private GuiElementFacade getFacade(GuiElementCore guiElementCore, GuiElementWait guiElementWait, GuiElementAssert guiElementAssert) {
        GuiElementFacade guiElementFacade;
        guiElementFacade = new StandardGuiElementFacade(guiElementCore, guiElementWait, guiElementAssert);
        guiElementFacade = new GuiElementFacadeLoggingDecorator(guiElementFacade, guiElementData);

        int delayAfterAction = PropertyManager.getIntProperty(TesterraProperties.DELAY_AFTER_GUIELEMENT_ACTION_MILLIS);
        int delayBeforeAction = PropertyManager.getIntProperty(TesterraProperties.DELAY_BEFORE_GUIELEMENT_ACTION_MILLIS);
        if (delayAfterAction > 0 || delayBeforeAction > 0) {
            guiElementFacade = new DelayActionsGuiElementFacade(guiElementFacade, delayBeforeAction, delayAfterAction, guiElementData);
        }
        return guiElementFacade;
    }

    /**
     * After retrieving all WebElements of the given locator, this method can be used to filter the WebElements.
     * Different filters can be applied in conjunction (and).
     *
     * @param filter Filters to be applied
     *
     * @return The same GuiElement
     * @deprecated Use {@link Locate} instead
     */
    @Deprecated
    public GuiElement withWebElementFilter(Predicate<WebElement> filter) {
        locator.filter(filter);
        return this;
    }

    /**
     * Add a Decorator to log every action performed on this element with addition to a short description of it.
     *
     * @param description A very short description of this GuiElement, for example "Continue Shopping Button"
     *
     * @deprecated Use {@link #setName(String)} instead
     */
    @Deprecated
    public GuiElement setDescription(String description) {
        guiElementData.name = description;
        return this;
    }

    /**
     * Get sub element by locator. Using this executes a find on the parent element and the parent.findElement for the
     * given locator. It does not wait for the subelement if the parent has been found!
     *
     * @param by Locator of new element.
     *
     * @return GuiElement
     */
    @Override
    public GuiElement getSubElement(By by) {
        return getSubElement(by, null);
    }

    /**
     * Get sub element by locator. Using this executes a find on the parent element and the parent.findElement for the
     * given locator. It does not wait for the subelement if the parent has been found!
     *
     * @param by   Locator of new element.
     * @param description Description for GuiElement
     *
     * @return GuiElement
     */
    @Deprecated
    @Override
    public GuiElement getSubElement(By by, String description) {
        return guiElementFacade.getSubElement(by, description);
    }

    @Override
    public GuiElement getSubElement(Locate locator) {
        return guiElementFacade.getSubElement(locator);
    }

    @Override
    public WebElement getWebElement() {
        return guiElementFacade.getWebElement();
    }

    @Override
    public By getBy() {
        return guiElementFacade.getBy();
    }

    @Override
    @Deprecated
    public void scrollToElement(int yOffset) {
        guiElementFacade.scrollToElement(yOffset);
    }

    @Override
    public void scrollIntoView(Point offset) {
        guiElementFacade.scrollIntoView(offset);
    }

    @Override
    public void select() {
        guiElementFacade.select();
    }

    /**
     * Select/Deselect a selectable element. If null is passed, no action will be performed.
     *
     * @param select true/false/null.
     */
    public void select(Boolean select) {
        if (select == null) {
            LOGGER.info("Select option is null. Selecting/Deselecting nothing.");
        }
        if (select) {
            guiElementFacade.select();
        } else {
            guiElementFacade.deselect();
        }
    }

    @Override
    public void deselect() {
        guiElementFacade.deselect();
    }

    @Override
    public void type(String text) {
        guiElementFacade.type(text);
    }

    @Override
    public void click() {
        guiElementFacade.click();
    }

    /**
     * @deprecated
     * This method is no longer acceptable as a part of GuiElement.
     * Use {@link eu.tsystems.mms.tic.testframework.webdrivermanager.DesktopWebDriverUtils#clickJS()} instead
     *
     */
    @Override
    @Deprecated
    public void clickJS() {
        guiElementFacade.clickJS();
    }

    /**
     * @deprecated
     * This method is no longer acceptable as a part of GuiElement.
     * Use {@link eu.tsystems.mms.tic.testframework.webdrivermanager.DesktopWebDriverUtils#clickAbsolute()} instead
     *
     */
    @Override
    @Deprecated
    public void clickAbsolute() {
        guiElementFacade.clickAbsolute();
    }

    /**
     * @deprecated
     * This method is no longer acceptable as a part of GuiElement.
     * Use {@link eu.tsystems.mms.tic.testframework.webdrivermanager.DesktopWebDriverUtils#mouseOverAbsolute2Axis()} instead
     *
     */
    @Override
    @Deprecated
    public void mouseOverAbsolute2Axis() {
        guiElementFacade.mouseOverAbsolute2Axis();
    }

    @Override
    public void submit() {
        guiElementFacade.submit();
    }

    @Override
    public void sendKeys(CharSequence... charSequences) {
        guiElementFacade.sendKeys(charSequences);
    }

    @Override
    public void clear() {
        guiElementFacade.clear();
    }

    @Override
    public String getTagName() {
        return guiElementFacade.getTagName();
    }

    @Override
    public String getAttribute(String attributeName) {
        return guiElementFacade.getAttribute(attributeName);
    }

    @Override
    public boolean isSelected() {
        return guiElementFacade.isSelected();
    }

    @Override
    public boolean isEnabled() {
        return guiElementFacade.isEnabled();
    }

    @Override
    public String getText() {
        return guiElementFacade.getText();
    }

    @Override
    public boolean isDisplayed() {
        return guiElementFacade.isDisplayed();
    }

    @Override
    public boolean isVisible(final boolean fullyVisible) {
        return guiElementFacade.isVisible(fullyVisible);
    }

    /**
     * Checks if the element is selectable.
     * <p>
     * WARNING: To determine if the element is truly selectable, a selection or deselection will be done and reverted.
     * Keep this in mind.
     *
     * @return true, if the element is selectable.
     */
    @Override
    public boolean isSelectable() {
        return guiElementFacade.isSelectable();
    }

    @Override
    public Point getLocation() {
        return guiElementFacade.getLocation();
    }

    @Override
    public Dimension getSize() {
        return guiElementFacade.getSize();
    }

    @Override
    public String getCssValue(String cssIdentifier) {
        return guiElementFacade.getCssValue(cssIdentifier);
    }

    @Override
    public void mouseOver() {
        guiElementFacade.mouseOver();
    }

    /**
     * @deprecated
     * This method is no longer acceptable as a part of GuiElement.
     * Use {@link eu.tsystems.mms.tic.testframework.webdrivermanager.DesktopWebDriverUtils#mouseOverJS()} instead
     *
     */
    @Override
    @Deprecated
    public void mouseOverJS() {
        guiElementFacade.mouseOverJS();
    }

    @Override
    public boolean isPresent() {
        return guiElementFacade.isPresent();
    }

    @Override
    public Select getSelectElement() {
        return guiElementFacade.getSelectElement();
    }

    @Override
    public List<String> getTextsFromChildren() {
        return guiElementFacade.getTextsFromChildren();
    }

    @Override
    public boolean anyFollowingTextNodeContains(String contains) {
        return guiElementFacade.anyFollowingTextNodeContains(contains);
    }

    public WebDriver getWebDriver() {
        return guiElementData.webDriver;
    }

    @Override
    public void doubleClick() {
        guiElementFacade.doubleClick();
    }

    /**
     * @deprecated This feature has been deprecated
     * @see {@link #setTimeoutInSeconds(int)} for description
     */
    @Deprecated
    public int getTimeoutInSeconds() {
        return guiElementData.getTimeoutInSeconds();
    }

    /**
     * @deprecated Setting the timeout for an element the way it's implemented is a design flaw,
     * because you can lose control over the timeout flow.
     * Setting timeouts on elements is controlled differently in future APIs and you
     * should not use this feature anymore.
     * When you want to use element timeouts, then refer to {@link PageOptions#elementTimeoutInSeconds()} instead
     */
    @Deprecated
    public void setTimeoutInSeconds(int timeoutInSeconds) {
        guiElementData.setTimeoutInSeconds(timeoutInSeconds);
    }

    /**
     * @deprecated This feature has been deprecated
     * @see {@link #setTimeoutInSeconds(int)} for description
     */
    @Deprecated
    public void restoreDefaultTimeout() {
        int timeoutInSeconds = POConfig.getUiElementTimeoutInSeconds();
        guiElementData.setTimeoutInSeconds(timeoutInSeconds);
    }

    @Override
    public void highlight(Color color) {
        guiElementFacade.highlight(color);
    }

    @Override
    public void swipe(int offsetX, int offSetY) {
        guiElementFacade.swipe(offsetX, offSetY);
    }

    @Override
    public int getLengthOfValueAfterSendKeys(String textToInput) {
        return guiElementFacade.getLengthOfValueAfterSendKeys(textToInput);
    }

    @Override
    public int getNumberOfFoundElements() {
        return guiElementFacade.getNumberOfFoundElements();
    }

    @Override
    public void rightClick() {
        guiElementFacade.rightClick();
    }

    /**
     * @deprecated
     * This method is no longer acceptable as a part of GuiElement.
     * Use {@link eu.tsystems.mms.tic.testframework.webdrivermanager.DesktopWebDriverUtils#rightClickJS()} instead
     *
     */
    @Override
    @Deprecated
    public void rightClickJS() {
        guiElementFacade.rightClickJS();
    }

    /**
     * @deprecated
     * This method is no longer acceptable as a part of GuiElement.
     * Use {@link eu.tsystems.mms.tic.testframework.webdrivermanager.DesktopWebDriverUtils#doubleClickJS()} instead
     *
     */
    @Override
    @Deprecated
    public void doubleClickJS() {
        guiElementFacade.doubleClickJS();
    }

    @Override
    public File takeScreenshot() {
        return guiElementFacade.takeScreenshot();
    }

    /**
     * Get Frame Login object. It encapsulates the correct order of switching the frames containing this GuiElement.
     *
     * @return Frame Logic object or null.
     */
    public FrameLogic getFrameLogic() {
        return guiElementData.frameLogic;
    }

    public boolean hasFrameLogic() {
        return guiElementData.hasFrameLogic();
    }

    /**
     * Get the parent element of this element, from getSubElement().
     *
     * @return parent object or null if this element has no parent
     */
    public GuiElementCore getParent() {
        return guiElementData.parent;
    }

    /**
     * Sets an element as parent.
     *
     * @param parent Object that should act as parent.
     */
    public GuiElement setParent(GuiElementCore parent) {
        guiElementData.parent = parent;
        return this;
    }

    @Override
    public String toString() {
        return guiElementData.toString();
    }

    @Deprecated
    public WebDriver getDriver() {
        return getWebDriver();
    }

    public boolean hasSensibleData() {
        return guiElementData.sensibleData;
    }

    public GuiElement sensibleData() {
        guiElementData.sensibleData = true;
        return this;
    }

    @Override
    public GuiElement setName(String name) {
        guiElementData.name = name;
        return this;
    }

    @Override
    public String getName() {
        return guiElementData.name;
    }

    /**
     * Provides access to all functional assert methods.
     *
     * @return GuiElementAssert object for functional assertions
     */
    public GuiElementAssert asserts() {
        return functionalAssert;
    }

    /**
     * Provides access to all assert methods. If an assertion fails, the assertDescription will be given as cause,
     * instead of the technical cause like an isDisplayed error.
     *
     * @param errorMessage Cause returned on assertion error.
     *
     * @return GuiElementAssert object for functional assertions
     */
    public GuiElementAssert asserts(String errorMessage) {
        GuiElementAssertDescriptionDecorator guiElementAssertDescriptionDecorator
                = new GuiElementAssertDescriptionDecorator(errorMessage, functionalAssert);
        return guiElementAssertDescriptionDecorator;
    }

    /**
     * Provides access to all non-functional assert methods.
     *
     * @return GuiElementAssert object for non-functional assertions
     * @deprecated Use {@link #optionalAsserts()} instead
     */
    public GuiElementAssert nonFunctionalAsserts() {
        return nonFunctionalAssertReplacement;
    }

    /**
     * Provides access to optional assert methods
     */
    public GuiElementAssert optionalAsserts() { return nonFunctionalAssertReplacement; }

    /**
     * Provides access to all non-functional assert methods. If an assertion fails, the assertDescription will be
     * given as cause, instead of the technical cause like an isDisplayed error.
     *
     * @param errorMessage Cause returned on assertion error.
     *
     * @return GuiElementAssert object for non-functional assertions
     * @deprecated Use {@link #optionalAsserts(String)} instead
     */
    public GuiElementAssert nonFunctionalAsserts(String errorMessage) {
        GuiElementAssertDescriptionDecorator guiElementAssertDescriptionDecorator
                = new GuiElementAssertDescriptionDecorator(errorMessage, nonFunctionalAssertReplacement);
        return guiElementAssertDescriptionDecorator;
    }

    /**
     * Provides access to optional assert methods
     */
    public GuiElementAssert optionalAsserts(String errorMessage) {
        return nonFunctionalAsserts(errorMessage);
    }

    /**
     * Provides access to all functional assert methods.
     *
     * @return GuiElementAssert object for functional assertions
     */
    public GuiElementAssert assertCollector() {
        return functionalAssertCollector;
    }

    /**
     * Provides access to all assert methods. If an assertion fails, the assertDescription will be given as cause,
     * instead of the technical cause like an isDisplayed error.
     *
     * @param errorMessage Cause returned on assertion error.
     *
     * @return GuiElementAssert object for functional assertions
     */
    public GuiElementAssert assertCollector(String errorMessage) {
        GuiElementAssertDescriptionDecorator guiElementAssertDescriptionDecorator
                = new GuiElementAssertDescriptionDecorator(errorMessage, functionalAssertCollector);
        return guiElementAssertDescriptionDecorator;
    }

    public List<GuiElement> getList() {
        int numberOfFoundElements = getNumberOfFoundElements();
        List<GuiElement> guiElements = new ArrayList<>(numberOfFoundElements);
        for (int i = 0; i < numberOfFoundElements; i++) {
            GuiElement guiElement = new GuiElement(new GuiElementData(guiElementData, i));
            guiElements.add(guiElement);
        }
        return guiElements;
    }

    public GuiElement shadowRoot() {
        guiElementData.shadowRoot = true;
        return this;
    }

    /**
     * Provides access to all wait methods
     */
    public GuiElementWait waits() {
        return guiElementWait;
    }
}
