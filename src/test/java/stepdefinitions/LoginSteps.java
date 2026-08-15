package stepdefinitions;

import base.DriverFactory;
import config.ConfigReader;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import pages.LoginPage;

public class LoginSteps {

    private WebDriver driver;
    private LoginPage loginPage;

    @Given("the user is on the Login page")
    public void the_user_is_on_the_login_page() {
        driver = DriverFactory.getDriver();
        driver.get(ConfigReader.getUrl() + "/login");
        loginPage = new LoginPage(driver);
    }

    @When("the user logs in with email {string} and password {string}")
    public void the_user_logs_in_with_email_and_password(String email, String password) {
        loginPage.login(email, password);
    }

    @Then("an error message should be displayed")
    public void an_error_message_should_be_displayed() {
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
                "Expected an error message after logging in with invalid credentials.");
    }
}
