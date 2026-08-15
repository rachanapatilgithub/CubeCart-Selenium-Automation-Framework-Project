package listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * ExtentManager.java
 *
 * Creates a single, shared ExtentReports instance for the whole test
 * run. TestNG can start many test classes, but they should all write
 * into the SAME html report, so this class hands out one instance
 * only (a simple singleton) instead of a new report per class.
 */
public class ExtentManager {

    private static final String REPORT_DIRECTORY = "test-output/ExtentReport";
    private static final String REPORT_PATH = REPORT_DIRECTORY + "/ExtentReport.html";

    private static ExtentReports extentReports;

    public static ExtentReports getInstance() {
        if (extentReports == null) {
            createReportDirectoryIfMissing();

            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(REPORT_PATH);
            sparkReporter.config().setDocumentTitle("CubeCart Automation Report");
            sparkReporter.config().setReportName("CubeCart Regression Suite");

            extentReports = new ExtentReports();
            extentReports.attachReporter(sparkReporter);
            extentReports.setSystemInfo("Application", "CubeCart");
            extentReports.setSystemInfo("Environment", "QA");
            extentReports.setSystemInfo("Browser", "Chrome");
        }
        return extentReports;
    }

    private static void createReportDirectoryIfMissing() {
        try {
            Files.createDirectories(Paths.get(REPORT_DIRECTORY));
        } catch (IOException e) {
            throw new RuntimeException("Could not create Extent report folder: " + REPORT_DIRECTORY, e);
        }
    }
}
