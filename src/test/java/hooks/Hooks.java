package hooks;

import base.DriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.WebDriver;
import utils.ScreenshotUtils;

/**
 * Hooks.java
 *
 * Cucumber equivalent of BaseTest: starts the browser before every
 * scenario and closes it afterwards, always saving a screenshot of
 * the last page so there is visual proof of both passed and failed runs.
 */
public class Hooks {

    @Before
    public void setUp(Scenario scenario) {
        System.out.println("\n=====================================================");
        System.out.println("STARTING SCENARIO : " + scenario.getName());
        System.out.println("=====================================================");
        DriverFactory.initDriver();
    }

    @After
    public void tearDown(Scenario scenario) {
        WebDriver driver = DriverFactory.getDriver();
        if (driver != null) {
            String screenshotPath = ScreenshotUtils.takeScreenshot(driver, scenario.getName());
            System.out.println("RESULT            : " + scenario.getName() + " -> " + scenario.getStatus());
            System.out.println("SCREENSHOT        : " + screenshotPath);
        }
        DriverFactory.quitDriver();
        System.out.println("=====================================================\n");
    }
}
