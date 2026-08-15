package base;

import config.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import utils.ScreenshotUtils;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * BaseTest.java
 *
 * Parent class for every plain TestNG test class. Starts a fresh
 * browser before each @Test method and closes it afterwards, taking
 * a screenshot of the final page state either way so there is visual
 * proof of both passed and failed runs.
 *
 * setUp() and tearDown() also print a simple before/after banner to the
 * console for every single test. TestNG's own report only shows up at
 * the end of the run, so these prints make it easy to follow along
 * live - which test is currently running, what data it is using, and
 * whether it passed or failed - without waiting for the HTML report.
 */
public class BaseTest {
    protected WebDriver driver;

    @BeforeMethod
    public void setUp(Method method, Object[] testData) {
        System.out.println("\n=====================================================");
        System.out.println("STARTING TEST : " + method.getName());
        if (testData != null && testData.length > 0) {
            System.out.println("TEST DATA     : " + Arrays.toString(testData));
        }
        System.out.println("=====================================================");
        driver = DriverFactory.initDriver();
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        String status = result.isSuccess() ? "PASSED" : (result.getStatus() == ITestResult.SKIP ? "SKIPPED" : "FAILED");
        if (driver != null) {
            String screenshotPath = ScreenshotUtils.takeScreenshot(driver, result.getMethod().getMethodName());
            System.out.println("RESULT        : " + result.getMethod().getMethodName() + " -> " + status);
            System.out.println("SCREENSHOT    : " + screenshotPath);
        }
        DriverFactory.quitDriver();
        System.out.println("=====================================================\n");
    }

    protected void openStoreUrl() { driver.get(ConfigReader.getUrl()); }
    protected void openAdminUrl() { driver.get(ConfigReader.getAdminUrl()); }
}
