package stepdefinitions;

import base.DriverFactory;
import config.ConfigReader;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import pages.AddProductPage;
import pages.AdminLoginPage;

public class AddProductSteps {

    private WebDriver driver;
    private AddProductPage addProductPage;

    @Given("the admin is logged in to the Admin Panel")
    public void the_admin_is_logged_in_to_the_admin_panel() {
        driver = DriverFactory.getDriver();
        driver.get(ConfigReader.getAdminUrl());
        AdminLoginPage adminLoginPage = new AdminLoginPage(driver);
        adminLoginPage.login(ConfigReader.getAdminUsername(), ConfigReader.getAdminPassword());
    }

    @Given("the admin is on the Add Product page")
    public void the_admin_is_on_the_add_product_page() {
        addProductPage = new AddProductPage(driver);
        addProductPage.navigateToAddProductPage();
    }

    @When("the admin fills the product details with a valid product name")
    public void the_admin_fills_the_product_details_with_a_valid_product_name() {
        addProductPage.enterProductName("Automation Test Product " + System.currentTimeMillis());
    }

    @When("the admin clicks the Save button")
    public void the_admin_clicks_the_save_button() {
        addProductPage.clickSave();
    }

    @When("the admin clicks the Save button without entering a product name")
    public void the_admin_clicks_the_save_button_without_entering_a_product_name() {
        addProductPage.clickSave();
    }

    @Then("the product should be saved successfully")
    public void the_product_should_be_saved_successfully() {
        Assert.assertTrue(addProductPage.isProductSavedSuccessfully(),
                "Product was not saved successfully.");
    }

    @Then("the product should not be saved")
    public void the_product_should_not_be_saved() {
        Assert.assertTrue(addProductPage.isProductNameValidationErrorDisplayed(),
                "Product Name field should show a validation error since no name was entered.");
    }
}
