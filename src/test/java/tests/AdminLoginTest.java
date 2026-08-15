package tests;

import base.BaseTest;
import config.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.AdminLoginPage;

/**
 * AdminLoginTest.java
 *
 * Covers Module 2 (Admin Login): smoke checks, a data-driven pass over
 * invalid credential combinations (including SQL injection attempts),
 * session/security checks, and logout.
 *
 * Every expected result below was confirmed by hand against the live
 * admin panel before being written into an assertion.
 */
public class AdminLoginTest extends BaseTest {

    private AdminLoginPage adminLoginPage;

    @BeforeMethod(dependsOnMethods = "setUp")
    public void openAdminLoginPage() {
        openAdminUrl();
        adminLoginPage = new AdminLoginPage(driver);
    }

    // ===================== SMOKE TESTS =====================

    @Test(priority = 1)
    public void testAdminLoginPageLoads() {
        Assert.assertTrue(adminLoginPage.isLoginPageDisplayed(), "Admin login page did not load.");
    }

    @Test(priority = 2)
    public void testAdminLoginValid() {
        adminLoginPage.login(ConfigReader.getAdminUsername(), ConfigReader.getAdminPassword());
        Assert.assertTrue(adminLoginPage.isDashboardDisplayed(), "Dashboard was not shown after a valid login.");
    }

    @Test(priority = 3)
    public void testAdminLoginInvalid() {
        adminLoginPage.login(ConfigReader.getAdminUsername(), "wrongPass");
        Assert.assertTrue(adminLoginPage.isLoginPageDisplayed(), "Should remain on the login page after a failed login.");
        Assert.assertTrue(adminLoginPage.isErrorMessageDisplayed(), "Expected an 'Invalid username or password' message.");
    }

    // ===================== DATA-DRIVEN NEGATIVE TESTS =====================

    /**
     * Every row here is expected to fail login and stay on the login page.
     * Empty-field submissions were confirmed to NOT show an error banner
     * (the page just reloads the login form), so the assertion here only
     * checks that the dashboard never appears - it does not require the
     * error banner, unlike {@link #testAdminLoginInvalid()}.
     */
    @DataProvider(name = "invalidAdminLoginData")
    public Object[][] invalidAdminLoginData() {
        return new Object[][]{
                {"TC01_EmptyUsername", "", "pass"},
                {"TC02_EmptyPassword", ConfigReader.getAdminUsername(), ""},
                {"TC03_BothEmpty", "", ""},
                {"TC04_SqlInjectionUsername", "' OR '1'='1", "pass"},
                {"TC05_SqlInjectionPassword", ConfigReader.getAdminUsername(), "' OR '1'='1"},
                {"TC06_SqlInjectionCommentAttack", "admin'--", "anything"},
                {"TC07_InvalidUsername", "notadmin", "pass"},
                {"TC08_WhitespaceOnlyUsername", "   ", "pass"},
                {"TC09_SqlInjectionDropTable", "admin'; DROP TABLE cubecart_customer; --", "pass"},
                {"TC10_SqlInjectionUnionSelect", "' UNION SELECT 1,2,3 --", "pass"},
                {"TC11_InvalidUsernameAndPassword", "wrongUser", "wrongPass"},
        };
    }

    @Test(dataProvider = "invalidAdminLoginData", priority = 4)
    public void testAdminLoginWithInvalidCombinations(String testCaseId, String username, String password) {
        adminLoginPage.login(username, password);

        Assert.assertFalse(adminLoginPage.isDashboardDisplayed(),
                testCaseId + ": dashboard must never be shown for an invalid login attempt.");
        Assert.assertTrue(adminLoginPage.isLoginPageDisplayed(),
                testCaseId + ": should remain on the login page.");
    }

    /** The admin username was confirmed to be case-insensitive - "ADMIN" logs in just like "admin". */
    @Test(priority = 4)
    public void testAdminUsernameIsCaseInsensitive() {
        adminLoginPage.login(ConfigReader.getAdminUsername().toUpperCase(), ConfigReader.getAdminPassword());
        Assert.assertTrue(adminLoginPage.isDashboardDisplayed(),
                "Username lookup was confirmed to be case-insensitive on the live site.");
    }

    // ===================== SESSION / SECURITY =====================

    @Test(priority = 5)
    public void testAdminLogout() {
        adminLoginPage.login(ConfigReader.getAdminUsername(), ConfigReader.getAdminPassword());
        Assert.assertTrue(adminLoginPage.isDashboardDisplayed(), "Setup login should succeed.");

        adminLoginPage.clickLogout();

        Assert.assertTrue(adminLoginPage.isLoginPageDisplayed(), "Login page should be shown again after logout.");
    }

    @Test(priority = 6)
    public void testUnauthorizedAccessAfterLogout() {
        adminLoginPage.login(ConfigReader.getAdminUsername(), ConfigReader.getAdminPassword());
        adminLoginPage.clickLogout();

        driver.get(ConfigReader.getAdminUrl() + "?_g=products&node=index");

        Assert.assertTrue(adminLoginPage.isLoginPageDisplayed(),
                "A logged-out session must not be able to open an admin page directly by URL.");
    }

    @Test(priority = 6)
    public void testUnauthorizedAccessWithoutEverLoggingIn() {
        driver.get(ConfigReader.getAdminUrl() + "?_g=products&node=index");

        Assert.assertTrue(adminLoginPage.isLoginPageDisplayed(),
                "An admin page must not be reachable directly by URL without logging in first.");
    }

    @Test(priority = 7)
    public void testSessionPersistsAfterBrowserRefresh() {
        adminLoginPage.login(ConfigReader.getAdminUsername(), ConfigReader.getAdminPassword());
        Assert.assertTrue(adminLoginPage.isDashboardDisplayed(), "Setup login should succeed.");

        driver.navigate().refresh();

        Assert.assertTrue(adminLoginPage.isDashboardDisplayed(),
                "Refreshing the browser should keep the admin session active.");
    }

    // ===================== UI CHECKS =====================

    @Test(priority = 1)
    public void testUsernameFieldIsEmptyOnPageLoad() {
        Assert.assertEquals(adminLoginPage.getUsernameFieldValue(), "",
                "Username field should be blank when the login page first loads.");
    }

    @Test(priority = 1)
    public void testPasswordFieldMasksInput() {
        Assert.assertEquals(adminLoginPage.getPasswordFieldType(), "password",
                "Password field must mask its input (type=password).");
    }

    @Test(priority = 1)
    public void testForgotPasswordLinkIsPresent() {
        Assert.assertTrue(adminLoginPage.isForgotPasswordLinkDisplayed(),
                "'Forgotten your password?' link should be visible on the login page.");
    }

    @Test(priority = 8)
    public void testMultipleFailedLoginAttemptsDoNotBreakThePage() {
        for (int attempt = 1; attempt <= 3; attempt++) {
            adminLoginPage.login(ConfigReader.getAdminUsername(), "wrongPass" + attempt);
            Assert.assertTrue(adminLoginPage.isLoginPageDisplayed(),
                    "Attempt " + attempt + ": login page should still be usable after a failed attempt.");
        }

        adminLoginPage.login(ConfigReader.getAdminUsername(), ConfigReader.getAdminPassword());
        Assert.assertTrue(adminLoginPage.isDashboardDisplayed(),
                "A valid login right after failed attempts should still succeed (no lockout observed).");
    }
}
