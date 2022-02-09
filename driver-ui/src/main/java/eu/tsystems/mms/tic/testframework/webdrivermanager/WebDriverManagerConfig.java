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
package eu.tsystems.mms.tic.testframework.webdrivermanager;

import eu.tsystems.mms.tic.testframework.common.PropertyManager;
import eu.tsystems.mms.tic.testframework.constants.TesterraProperties;
import eu.tsystems.mms.tic.testframework.enums.Position;
import eu.tsystems.mms.tic.testframework.logging.Loggable;
import eu.tsystems.mms.tic.testframework.webdrivermanager.desktop.WebDriverMode;
import java.net.MalformedURLException;
import org.apache.commons.lang3.StringUtils;

/**
 * Class holding configuration settings for the WebDriverManager. Some are writable. This class is not ThreadSafe, some
 * settings may not be valid.
 */
public class WebDriverManagerConfig extends AbstractWebDriverConfiguration implements Loggable {
    /**
     * Specifies if windows should be closed.
     */
    private boolean executeCloseWindows = true;

    /**
     * Close windows after Test Methods.
     */
    private boolean closeWindowsAfterTestMethod;

    /**
     * Close windows on failure.
     */
    private boolean closeWindowsOnFailure;

    private boolean maximize;

    private Position maximizePosition;

    /**
     * Default constructor.
     */
    public WebDriverManagerConfig() {
        this.reset();
    }

    public WebDriverManagerConfig reset() {
        this.executeCloseWindows = true;
        this.closeWindowsAfterTestMethod = PropertyManager.getBooleanProperty(TesterraProperties.CLOSE_WINDOWS_AFTER_TEST_METHODS, true);
        this.closeWindowsOnFailure = PropertyManager.getBooleanProperty(TesterraProperties.CLOSE_WINDOWS_ON_FAILURE, true);
        this.maximize = PropertyManager.getBooleanProperty(TesterraProperties.BROWSER_MAXIMIZE, false);
        this.maximizePosition = Position.valueOf(PropertyManager.getProperty(TesterraProperties.BROWSER_MAXIMIZE_POSITION, Position.CENTER.toString()).toUpperCase());
        String baseUrl = PropertyManager.getProperty(TesterraProperties.BASEURL, "");
        if (!baseUrl.isEmpty()) {
            try {
                this.setBaseUrl(baseUrl);
            } catch (MalformedURLException e) {
                log().error("Unable to read " + TesterraProperties.BASEURL, e);
            }
        }
        this.initBrowser();
        return this;
    }

    private void initBrowser() {
        String browserSetting = PropertyManager.getProperty(TesterraProperties.BROWSER_SETTING);
        if (StringUtils.isNotBlank(browserSetting)) {
            String[] split = browserSetting.split(":");
            if (split.length > 0) this.setBrowser(split[0].trim());
            if (split.length > 1) this.setBrowserVersion(split[1].trim());
            if (split.length > 2) this.setPlatformName(split[2].trim());
        }

        if (StringUtils.isBlank(this.getBrowser())) {
            this.setBrowser(PropertyManager.getProperty(TesterraProperties.BROWSER, ""));
        }

        if (StringUtils.isBlank(this.getBrowserVersion())) {
            this.setBrowserVersion(PropertyManager.getProperty(TesterraProperties.BROWSER_VERSION, ""));
        }

        if (StringUtils.isBlank(this.getPlatformName().orElse(null))) {
            this.setPlatformName(PropertyManager.getProperty(TesterraProperties.BROWSER_PLATFORM, ""));
        }
    }

    @Deprecated
    public WebDriverMode getWebDriverMode() {
        return null;
    }

    public boolean shouldShutdownSessions() {
        return executeCloseWindows;
    }

    public WebDriverManagerConfig setShutdownSessions(boolean close) {
        this.executeCloseWindows = close;
        return this;
    }

    public boolean shouldShutdownSessionAfterTestMethod() {
        return executeCloseWindows && closeWindowsAfterTestMethod;
    }

    public WebDriverManagerConfig setShutdownSessionAfterTestMethod(boolean shutdown) {
        if (shutdown) {
            this.setShutdownSessions(true);
        }
        this.closeWindowsAfterTestMethod = shutdown;
        return this;
    }

    public boolean shouldShutdownSessionOnFailure() {
        return executeCloseWindows && closeWindowsOnFailure;
    }

    public boolean shouldMaximizeViewport() {
        return maximize;
    }

    public Position getMaximizePosition() {
        return maximizePosition;
    }
}
