package tests;

import base.BaseTest;
import config.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;

/**
 * LoginTest.java
 *
 * Covers the End User Login page. Only the invalid-credentials case
 * is testable with static data, since a valid login needs an account
 * that already exists - that path is exercised by
 * {@link RegisterTest#testRegisterWithValidDetails()}, which logs the
 * new user in automatically straight after registration.
 */
public class LoginTest extends BaseTest {

    @Test(priority = 1)
    public void testLoginPageLoads() {
        driver.get(ConfigReader.getUrl() + "/login");
        Assert.assertTrue(new LoginPage(driver).isLoginPageDisplayed(),
                "Login page did not load.");
    }

    @Test(priority = 2)
    public void testLoginWithInvalidCredentials() {
        driver.get(ConfigReader.getUrl() + "/login");
        LoginPage loginPage = new LoginPage(driver);

        loginPage.login("invalid" + System.currentTimeMillis() + "@mail.com", "WrongPass1");

        Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
                "Expected an error message after logging in with invalid credentials.");
    }
}
