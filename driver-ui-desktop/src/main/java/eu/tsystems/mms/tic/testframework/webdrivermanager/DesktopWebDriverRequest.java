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
import eu.tsystems.mms.tic.testframework.logging.Loggable;
import eu.tsystems.mms.tic.testframework.webdrivermanager.desktop.WebDriverMode;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Dimension;

public class DesktopWebDriverRequest extends AbstractWebDriverRequest implements Loggable, Serializable {
    private URL seleniumServerURL;

    /**
     * @deprecated Use {@link #getServerUrl()} instead
     */
    public URL getSeleniumServerUrl() {
        if (seleniumServerURL == null) {
            String serverUrl = PropertyManager.getProperty(TesterraProperties.SELENIUM_SERVER_URL);
            if (StringUtils.isBlank(serverUrl)) {
                final String serverHost = PropertyManager.getProperty(TesterraProperties.SELENIUM_SERVER_HOST);
                if (StringUtils.isNotBlank(serverHost)) {
                    serverUrl = String.format("http://%s:%d/wd/hub", serverHost, PropertyManager.getIntProperty(TesterraProperties.SELENIUM_SERVER_PORT, 4444));
                }
            }

            if (StringUtils.isNotBlank(serverUrl)) {
                try {
                    setSeleniumServerUrl(serverUrl);
                } catch (MalformedURLException e) {
                    log().error("Unable to retrieve default Selenium URL from properties", e);
                }
            }
        }
        return seleniumServerURL;
    }

    /**
     * @deprecated Use {@link #setSeleniumServerUrl(String)} instead
     */
    public DesktopWebDriverRequest setSeleniumServerUrl(String url) throws MalformedURLException {
        return this.setSeleniumServerUrl(new URL(url));
    }

    /**
     * @deprecated Use {@link #setServerUrl(URL)} instead
     */
    public DesktopWebDriverRequest setSeleniumServerUrl(URL url) {
        this.setWebDriverMode(WebDriverMode.remote);
        this.seleniumServerURL = url;
        return this;
    }

    public void setServerUrl(String url) throws MalformedURLException {
        this.setSeleniumServerUrl(url);
    }

    public void setServerUrl(URL url) {
        this.setSeleniumServerUrl(url);
    }

    /**
     * @deprecated Use {@link #getServerUrl()}
     */
    public WebDriverMode getWebDriverMode() {
        return null;
    }

    /**
     * @deprecated Use {@link #setSeleniumServerUrl(URL)} instead
     */
    public DesktopWebDriverRequest setWebDriverMode(WebDriverMode webDriverMode) {
        return this;
    }

    @Override
    public Optional<URL> getServerUrl() {
        return Optional.ofNullable(this.getSeleniumServerUrl());
    }

    public Dimension getWindowSize() {
        Dimension dimension;
        String windowSizeProperty = PropertyManager.getProperty(TesterraProperties.WINDOW_SIZE, PropertyManager.getProperty(TesterraProperties.DISPLAY_RESOLUTION));
        if (org.apache.commons.lang3.StringUtils.isNotBlank(windowSizeProperty)) {
            Pattern pattern = Pattern.compile("(\\d+)x(\\d+)");
            Matcher matcher = pattern.matcher(windowSizeProperty);

            if (matcher.find()) {
                int width = Integer.parseInt(matcher.group(1));
                int height = Integer.parseInt(matcher.group(2));
                dimension = new Dimension(width, height);
            } else {
                dimension = getDefaultDimension();
                log().error(String.format("Unable to parse property %s=%s, falling back to default: %s", TesterraProperties.WINDOW_SIZE, windowSizeProperty, dimension));
            }
        } else {
            dimension = getDefaultDimension();
        }
        return dimension;
    }

    private Dimension getDefaultDimension() {
        return new Dimension(1920, 1080);
    }
}
