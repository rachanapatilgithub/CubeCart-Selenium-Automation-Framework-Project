package listeners;

import base.DriverFactory;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.ScreenshotUtils;

/**
 * ExtentTestListener.java
 *
 * TestNG listener that turns every @Test method into one row in the
 * ExtentReports html report, with a screenshot attached whenever a
 * test fails. It is wired into every run through testng.xml, so no
 * test class needs to call it directly.
 *
 * This whole framework runs tests one at a time, not in parallel (see
 * DriverFactory's single static driver field), so this listener just
 * keeps track of the one test currently running in a plain field.
 */
public class ExtentTestListener implements ITestListener {

    private ExtentTest currentTest;
    private final ExtentReports extentReports = ExtentManager.getInstance();

    @Override
    public void onTestStart(ITestResult result) {
        currentTest = extentReports.createTest(
                result.getMethod().getMethodName(),
                result.getMethod().getDescription());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        currentTest.log(Status.PASS, "Test passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        currentTest.log(Status.FAIL, result.getThrowable());
        attachScreenshotIfAvailable(result);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        currentTest.log(Status.SKIP, "Test skipped: " + result.getThrowable());
    }

    @Override
    public void onFinish(ITestContext context) {
        extentReports.flush();
        System.out.println("\nExtentReport generated at: test-output/ExtentReport/ExtentReport.html");
    }

    private void attachScreenshotIfAvailable(ITestResult result) {
        WebDriver driver = DriverFactory.getDriver();
        if (driver == null) {
            return;
        }
        String screenshotPath = ScreenshotUtils.takeScreenshot(driver, result.getMethod().getMethodName());
        if (screenshotPath != null) {
            try {
                currentTest.addScreenCaptureFromPath(new java.io.File(screenshotPath).getAbsolutePath());
            } catch (Exception e) {
                System.out.println("Could not attach screenshot to Extent report: " + e.getMessage());
            }
        }
    }
}
