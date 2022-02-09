/*
 * Testerra
 *
 * (C) 2020, Eric Kubenka, T-Systems Multimedia Solutions GmbH, Deutsche Telekom AG
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

package io.testerra.report.test;

import eu.tsystems.mms.tic.testframework.AbstractWebDriverTest;
import eu.tsystems.mms.tic.testframework.common.PropertyManager;
import eu.tsystems.mms.tic.testframework.constants.TesterraProperties;
import eu.tsystems.mms.tic.testframework.core.server.Server;
import eu.tsystems.mms.tic.testframework.core.testpage.TestPage;
import eu.tsystems.mms.tic.testframework.logging.Loggable;
import eu.tsystems.mms.tic.testframework.pageobjects.POConfig;
import java.io.File;
import java.lang.reflect.Method;
import java.net.BindException;
import java.net.URISyntaxException;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

/**
 * Abstract test class for tests based on static test site resources
 */
public abstract class AbstractReportTest extends AbstractWebDriverTest implements Loggable {

    private final static File serverRootDir = new File("./");
    private final static Server server = new Server(serverRootDir);

    @BeforeTest(alwaysRun = true)
    public void setUp() throws Exception {
        POConfig.setUiElementTimeoutInSeconds(3);
        try {
            server.start(80);
        } catch (BindException e) {
            log().warn("Use already running WebServer: " + e.getMessage());
        }
    }

    @BeforeMethod(alwaysRun = true)
    public void visitTestPage(Method method) {
        visitTestPage(getWebDriver());
    }

    /**
     * Open a custom Webdriver session with the default test page.
     *
     * @param driver {@link WebDriver} Current webDriver Instance
     */
    public synchronized void visitTestPage(WebDriver driver) {
        visitTestPage(driver, getReportDir());
    }

    /**
     * Open a custom Webdriver session with the default test page.
     *
     * @param driver   {@link WebDriver} Current Instance
     * @param directory {@link TestPage} page to open
     */
    public synchronized void visitTestPage(WebDriver driver, String directory) {
        Assert.assertTrue(serverRootDir.exists(), String.format("Server root directory '%s' doesn't exists", serverRootDir));
        File reportDir = new File(serverRootDir, directory);
        Assert.assertTrue(reportDir.exists(), String.format("Report directory '%s' doesn't exists", reportDir));
        if (!driver.getCurrentUrl().contains(directory)) {
            String baseUrl = String.format("http://localhost:%d/%s", server.getPort(), directory);
            driver.get(baseUrl);
        }
    }

    protected String getReportDir() {
        return PropertyManager.getProperty(TesterraProperties.REPORTDIR, "test-report");
    }
}
