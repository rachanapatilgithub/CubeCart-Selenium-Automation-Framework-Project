package stepdefinitions;

import base.DriverFactory;
import config.ConfigReader;
import io.cucumber.java.en.*;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import pages.AdminLoginPage;

public class AdminLoginSteps {
    private WebDriver driver;
    private AdminLoginPage adminLoginPage;

    @Given("the admin opens the Admin URL")
    public void the_admin_opens_the_admin_url() {
        driver = DriverFactory.getDriver();
        driver.get(ConfigReader.getAdminUrl());
        adminLoginPage = new AdminLoginPage(driver);
        Assert.assertTrue(adminLoginPage.isLoginPageDisplayed(), "Admin login page not loaded");
    }

    @When("the admin logs in with valid credentials")
    public void the_admin_logs_in_with_valid_credentials() {
        adminLoginPage.login(ConfigReader.getAdminUsername(), ConfigReader.getAdminPassword());
    }

    @Then("the admin dashboard should be displayed")
    public void the_admin_dashboard_should_be_displayed() {
        Assert.assertTrue(adminLoginPage.isDashboardDisplayed(), "Dashboard not displayed");
    }

    @When("the admin logs in with username {string} and password {string}")
    public void the_admin_logs_in_with_username_and_password(String username, String password) {
        adminLoginPage.login(username, password);
    }

    @Then("the admin login should fail")
    public void the_admin_login_should_fail() {
        Assert.assertTrue(adminLoginPage.isLoginPageDisplayed(), "Should remain on the login page after a failed login.");
        Assert.assertTrue(adminLoginPage.isErrorMessageDisplayed(), "Expected an error message after an invalid admin login.");
    }
}
