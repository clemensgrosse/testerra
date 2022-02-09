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
 package eu.tsystems.mms.tic.testframework.constants;

/**
 * Class holding keys of all properties.
 *
 * @author sepr
 */
public final class TesterraProperties {

    /**
     * Hide default constructor.
     */
    private TesterraProperties() {

    }

    /**
     * Property to set the browser used by Selenium/ Webdriver.
     */
    public static final String BROWSER = "tt.browser";
    public static final String BROWSER_VERSION = "tt.browser.version";

    // BROWSER_SETTING syntax: browser:version
    // BROWSER_SETTING overrides BROWSER and BROWSER_VERSION
    public static final String BROWSER_SETTING = "tt.browser.setting";
    public static final String BROWSER_PLATFORM = "tt.browser.platform";

    /**
     * Property to set the host of the remote selenium server.
     */
    public static final String SELENIUM_SERVER_URL = "tt.selenium.server.url";
    /**
     * @deprecated Use {@link #SELENIUM_SERVER_URL} instead
     */
    public static final String SELENIUM_SERVER_HOST = "tt.selenium.server.host";

    /**
     * Property to set the port of the remote selenium server.
     * @deprecated Use {@link #SELENIUM_SERVER_URL} instead
     */
    public static final String SELENIUM_SERVER_PORT = "tt.selenium.server.port";

    /**
     * @deprecated Use {@link #SELENIUM_SERVER_URL} instead
     * Property Key for webdriver mode remote|local
     */
    public static final String WEBDRIVERMODE = "tt.webdriver.mode";

    /**
     * Property key of baseUrl used by Selenium.
     */
    public static final String BASEURL = "tt.baseurl";

    /**
     * Property key stating to take automatic screenshots or not.
     */
    public static final String SCREENSHOTTER_ACTIVE = "tt.screenshotter.active";

    /**
     * Key of reportDir Property.
     */
    public static final String REPORTDIR = "tt.report.dir";
    public static final String REPORTNAME = "tt.report.name";

    /**
     * Failed tests maximum number of retries.
     */
    public static final String FAILED_TESTS_MAX_RETRIES = "tt.failed.tests.max.retries";

    /**
     * Failed tests condition: Throwable Class(~es, devided by ','.
     */
    public static final String FAILED_TESTS_IF_THROWABLE_CLASSES = "tt.failed.tests.if.throwable.classes";

    /**
     * Failed tests condition. Throwable Message(~s, devided by ',').
     */
    public static final String FAILED_TESTS_IF_THROWABLE_MESSAGES = "tt.failed.tests.if.throwable.messages";

    /**
     * WDM close windows rule.
     */
    public static final String CLOSE_WINDOWS_AFTER_TEST_METHODS = "tt.wdm.closewindows.aftertestmethods";

    /**
     * WDM close windows rule.
     */
    public static final String CLOSE_WINDOWS_ON_FAILURE = "tt.wdm.closewindows.onfailure";

    /**
     * WDM: Timeout / Duration Setting for Window Switching
     */
    public static final String WEBDRIVER_WINDOW_SWITCH_MAX_DURATION = "tt.wdm.timeouts.seconds.window.switch.duration";

    /**
     * Visually marks every GuiElement that is being processed. Might break a LayoutTest.
     */
    public static final String DEMO_MODE = "tt.demomode";

    /**
     * Element timeout seconds.
     */
    public static final String ELEMENT_TIMEOUT_SECONDS = "tt.element.timeout.seconds";

    /**
     * State condition.
     */
    public static final String ON_STATE_TESTFAILED_SKIP_FOLLOWING_TESTS = "tt.on.state.testfailed.skip.following.tests";
    /**
     * State condition.
     */
    public static final String ON_STATE_TESTFAILED_SKIP_SHUTDOWN = "tt.on.state.testfailed.skip.shutdown";

    public static final String REPORT_SCREENSHOTS_PREVIEW = "tt.report.screenshots.preview";

    /**
     * Module source root
     */
    public static final String MODULE_SOURCE_ROOT = "tt.module.source.root";
    /**
     * tt.source.lines.prefetch
     */
    public static final String SOURCE_LINES_PREFETCH = "tt.source.lines.prefetch";
    /**
     * tt.report.activate.sources
     */
    public static final String REPORT_ACTIVATE_SOURCES = "tt.report.activate.sources";

    public static final String GUIELEMENT_DEFAULT_ASSERT_IS_COLLECTOR = "tt.guielement.default.assertcollector";
    @Deprecated
    public static final String GUIELEMENT_USE_JS_ALTERNATIVES = "tt.guielement.use.js.alternatives";

    /*
     * Package the project is located in.
     */
    public static final String PROJECT_PACKAGE = "tt.project.package";

    /**
     * Flag for Perf Test Statistics generation.
     */
    public static final String PERF_GENERATE_STATISTICS = "tt.perf.generate.statistics";
    /**
     * Perf test thinktime.
     */
    public static final String PERF_PAGE_THINKTIME_MS = "tt.perf.page.thinktime.ms";
    /**
     * reuse existing driver for a thread of dataprovider
     */
    public static final String REUSE_DATAPROVIDER_DRIVER_BY_THREAD = "tt.reuse.dataprovider.driver.by.thread";
    /**
     * Perf test Property, used to activate performance test related behaviour and to set default values for the performance test
     */
    public static final String PERF_TEST = "tt.perf.test";

    /**
     * If true, screenshot after page is loaded will be taken.
     */
    public static final String SCREENSHOT_ON_PAGELOAD = "tt.screenshot.on.pageload";

    public static final String DB_TIMEOUT = "tt.db.timeout";

    public static final String DRY_RUN = "tt.dryrun";

    public static final String GUIELEMENT_CHECK_RULE = "tt.guielement.checkrule";

    public static final String BROWSER_MAXIMIZE = "tt.browser.maximize";
    public static final String BROWSER_MAXIMIZE_POSITION = "tt.browser.maximize.position";

    public static final String WEBDRIVER_TIMEOUT_SECONDS_PAGELOAD = "webdriver.timeouts.seconds.pageload";
    public static final String WEBDRIVER_TIMEOUT_SECONDS_SCRIPT = "webdriver.timeouts.seconds.script";

    public static final String SYSTEM_SETTINGS_FILE = "tt.system.settings.file";

    public static final String LIST_TESTS = "tt.list.tests";

    public static final String DELAY_AFTER_GUIELEMENT_FIND_MILLIS = "tt.delay.after.guielement.find.millis";
    public static final String DELAY_BEFORE_GUIELEMENT_ACTION_MILLIS = "tt.delay.before.guielement.action.millis";
    public static final String DELAY_AFTER_GUIELEMENT_ACTION_MILLIS = "tt.delay.after.guielement.action.millis";

    public static final String RUNCFG = "tt.runcfg";

    public static final String WATCHDOG_ENABLE = "tt.watchdog.enable";
    public static final String WATCHDOG_TIMEOUT_SECONDS = "tt.watchdog.timeout.seconds";

    /*
    failure corridor
     */
    public static final String FAILURE_CORRIDOR_ACTIVE = "tt.failure.corridor.active";
    public static final String FAILURE_CORRIDOR_ALLOWED_FAILED_TESTS_HIGH = "tt.failure.corridor.allowed.failed.tests.high";
    public static final String FAILURE_CORRIDOR_ALLOWED_FAILED_TESTS_MID = "tt.failure.corridor.allowed.failed.tests.mid";
    public static final String FAILURE_CORRIDOR_ALLOWED_FAILED_TESTS_LOW = "tt.failure.corridor.allowed.failed.tests.low";

    @Deprecated
    public static final String DISPLAY_RESOLUTION = "tt.display.resolution";
    public static final String WINDOW_SIZE="tt.window.size";

    public static final String PAGE_FACTORY_LOOPS = "tt.page.factory.loops";
    public static final String EXECUTION_OMIT_IN_DEVELOPMENT = "tt.execution.omit.indevelopment";

    /*
    layout stuff
     */

    /*
     * The mode for the screenreferencer. Values are pixel (default) or annotated.
     */
    public static final String LAYOUTCHECK_MODE = "tt.layoutcheck.mode";

    // if true, will use non-functional asserts
    @Deprecated
    public static final String LAYOUTCHECK_ASSERT_INFO_MODE = "tt.layoutcheck.assert.info.mode";

    public static final String LAYOUTCHECK_TAKEREFERENCE = "tt.layoutcheck.takereference";
    public static final String LAYOUTCHECK_REFERENCE_NAMETEMPLATE = "tt.layoutcheck.reference.nametemplate";
    public static final String LAYOUTCHECK_ANNOTATED_NAMETEMPLATE = "tt.layoutcheck.annotated.nametemplate";
    public static final String LAYOUTCHECK_ANNOTATIONDATA_NAMETEMPLATE = "tt.layoutcheck.annotationdata.nametemplate";
    public static final String LAYOUTCHECK_ACTUAL_NAMETEMPLATE = "tt.layoutcheck.actual.nametemplate";
    public static final String LAYOUTCHECK_DISTANCE_NAMETEMPLATE = "tt.layoutcheck.distance.nametemplate";
    public static final String LAYOUTCHECK_REFERENCE_PATH = "tt.layoutcheck.reference.path";
    public static final String LAYOUTCHECK_DISTANCE_PATH = "tt.layoutcheck.distance.path";
    public static final String LAYOUTCHECK_ACTUAL_PATH = "tt.layoutcheck.actual.path";
    public static final String LAYOUTCHECK_USE_IGNORE_COLOR = "tt.layoutcheck.use.ignore.color";
    public static final String LAYOUTCHECK_USE_AREA_COLOR = "tt.layoutcheck.use.area.color";
    public static final String LAYOUTCHECK_PIXEL_RGB_DEVIATION_PERCENT = "tt.layout.pixel.rgb.deviation.percent";

    // Properties for the layout comparator working with
    public static final String LAYOUTCHECK_MATCH_THRESHOLD = "tt.layoutcheck.match.threshold";
    public static final String LAYOUTCHECK_DISPLACEMENT_THRESHOLD = "tt.layoutcheck.displacement.threshold";
    public static final String LAYOUTCHECK_INTRA_GROUPING_THRESHOLD = "tt.layoutcheck.intra.grouping.threshold";
    public static final String LAYOUTCHECK_MINIMUM_MARKED_PIXELS = "tt.layoutcheck.minimum.marked.pixels";
    public static final String LAYOUTCHECK_MAXIMUM_MARKED_PIXELS_RATIO = "tt.layoutcheck.maximum.marked.pixels.ratio";
    /**
     * minimalDistanceBetweenMatches
     */
    public static final String LAYOUTCHECK_MIN_MATCH_DISTANCE = "tt.layoutcheck.min.match.distance";
    /**
     * minimalSizeDifferenceOfSubImages
     */
    public static final String LAYOUTCHECK_MIN_SIZE_DIFFERENCE_SUB_IMAGES = "tt.layoutcheck.min.size.difference.sub.images";
    /**
     * minimumSimilarMovementErrorsForDisplacementCorrection
     */
    public static final String LAYOUTCHECK_MIN_SIMULAR_MOVEMENT_ERRORS = "tt.layoutcheck.min.similar.movement.errors";
    /**
     * distanceBetweenMultipleMatchesToProduceWarning
     */
    public static final String LAYOUTCHECK_DISTANCE_MULTIPLE_MATCHES = "tt.layoutcheck.distance.multiple.matches";
    public static final String LAYOUTCHECK_IGNORE_AMBIGUOUS_MOVEMENT = "tt.layoutcheck.ignore.ambiguous.movement";
    public static final String LAYOUTCHECK_IGNORE_MOVEMENT = "tt.layoutcheck.ignore.movement";
    public static final String LAYOUTCHECK_IGNORE_GROUP_MOVEMENT = "tt.layoutcheck.ignore.group.movement";
    public static final String LAYOUTCHECK_IGNORE_MISSING_ELEMENTS = "tt.layoutcheck.ignore.missing.elements";
    public static final String LAYOUTCHECK_IGNORE_AMBIGUOUS_MATCH = "tt.layoutcheck.ignore.ambiguous.match";

    public static final String LAYOUTCHECK_TEXT_ERRORDETECTOR_MINIMAL_LINELENGTH = "tt.layoutcheck.text.error.detector.minimal.line.length";
    public static final String LAYOUTCHECK_TEXT_ERRORDETECTOR_MINIMAL_EDGE_STRENGTH = "tt.layoutcheck.text.error.detector.minimal.edge.strength";

    public static final String SCREENCASTER_ACTIVE = "tt.screencaster.active";
    public static final String SCREENCASTER_ACTIVE_ON_SUCCESS = "tt.screencaster.active.on.success";
    public static final String SCREENCASTER_ACTIVE_ON_FAILED = "tt.screencaster.active.on.failed";

}
