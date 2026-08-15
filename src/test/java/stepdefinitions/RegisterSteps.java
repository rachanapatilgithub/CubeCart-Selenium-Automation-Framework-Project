package stepdefinitions;

import base.DriverFactory;
import config.ConfigReader;
import io.cucumber.java.en.*;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import pages.RegisterPage;

public class RegisterSteps {
    private WebDriver driver;
    private RegisterPage registerPage;

    @Given("the user is on the Register page")
    public void the_user_is_on_the_register_page() {
        driver = DriverFactory.getDriver();
        driver.get(ConfigReader.getUrl() + "/register");
        registerPage = new RegisterPage(driver);
        Assert.assertTrue(registerPage.isRegisterPageDisplayed(), "Register page not loaded");
    }

    @When("the user fills the registration form with all valid details")
    public void the_user_fills_the_registration_form_with_all_valid_details() {
        String email = "user" + System.currentTimeMillis() + "@mail.com";
        registerPage.fillRegistrationForm("Rachana", "QA", email,
                "9999999999", "8888888888", "Pass123");
    }

    @And("the user clicks the Register button")
    public void the_user_clicks_the_register_button() {
        registerPage.clickRegister();
    }

    @Then("the registration should be successful and browser closed")
    public void the_registration_should_be_successful_and_browser_closed() {
        Assert.assertTrue(registerPage.isRegistrationSuccessful(),
                "Registration failed, still on register page");
        DriverFactory.quitDriver();
    }
}
