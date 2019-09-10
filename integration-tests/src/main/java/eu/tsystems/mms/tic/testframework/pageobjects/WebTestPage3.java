/*
 * (C) Copyright T-Systems Multimedia Solutions GmbH 2018, ..
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     Peter Lehmann <p.lehmann@t-systems.com>
 *     pele <p.lehmann@t-systems.com>
 */
/*
 * Created on 07.01.2013
 *
 * Copyright(c) 2013 - 2013 T-Systems Multimedia Solutions GmbH
 * Riesaer Str. 5, 01129 Dresden
 * All rights reserved.
 */
package eu.tsystems.mms.tic.testframework.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Created with IntelliJ IDEA. User: pele Date: 07.01.13 Time: 14:15 To change this template use File | Settings | File
 * Templates.
 */
public class WebTestPage3 extends Page {

    /**
     * URL for the input-testpage
     *
     */
    public static final String URL = "http://192.168.60.239/WebsitesForTests/Input/input.html";

    /** First text field on page. */
    @Check
    private final GuiElement input1 = new GuiElement(driver, By.id("1"));
    /** A button on the page. */
    @Check(nonFunctional = true)
    private final GuiElement button1 = new GuiElement(driver, By.id("4"));
    /** The output text field. */
    private final GuiElement textOutputField = new GuiElement(driver, By.xpath("//p[@id='99']"));
    private final GuiElement textOutputFieldNotExisting = new GuiElement(driver, By.xpath("//p[@id='notthere']"));

    /**
     * Constructor for existing sessions.
     *
     * @param driver .
     */
    public WebTestPage3(WebDriver driver) {
        super(driver);
        checkPage();
    }

    public void clickOnField() {
        logger.error("Something1");
        logger.error("Something2");
        textOutputField.click();
    }

    public void subElement() {
        new GuiElement(driver, By.tagName("body")).getSubElement(By.tagName("p")).asserts().assertIsPresentFast();
    }

    public void clickOnFieldNotExisting() {
        textOutputFieldNotExisting.click();
    }
}
