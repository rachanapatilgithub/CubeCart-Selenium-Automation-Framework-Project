package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * RegisterPage.java
 *
 * Page Object for the End User "Register" page
 * (https://javabykiran.in/other/CC/register).
 *
 * Locators below were confirmed against the live registration form
 * (id="registration_form"): every field uses a stable id attribute,
 * so id locators are used instead of fragile placeholder/xpath locators.
 */
public class RegisterPage extends BasePage {

    private By titleField = By.id("title");
    private By firstNameField = By.id("first_name");
    private By lastNameField = By.id("last_name");
    private By emailField = By.id("email");
    private By phoneField = By.id("phone");
    private By mobileField = By.id("mobile");
    private By passwordField = By.id("password");
    private By confirmPasswordField = By.id("passconf");
    private By termsCheckbox = By.id("terms");
    private By newsletterCheckbox = By.id("mailing");
    private By registerButton = By.id("register_submit");

    // Shown next to the Logout link only after a successful registration/login
    private By logoutLink = By.cssSelector("a[href*='_a=logout']");

    // The jQuery validation plugin marks empty required fields with this class
    private By firstNameValidationError = By.id("first_name-error");

    public RegisterPage(WebDriver driver) {
        super(driver);
    }

    public boolean isRegisterPageDisplayed() {
        return isDisplayed(firstNameField);
    }

    public void enterTitle(String title) { type(titleField, title); }
    public void enterFirstName(String firstName) { type(firstNameField, firstName); }
    public void enterLastName(String lastName) { type(lastNameField, lastName); }
    public void enterEmail(String email) { type(emailField, email); }
    public void enterPhone(String phone) { type(phoneField, phone); }
    public void enterMobile(String mobile) { type(mobileField, mobile); }
    public void enterPassword(String password) { type(passwordField, password); }
    public void enterConfirmPassword(String password) { type(confirmPasswordField, password); }
    public void checkAgreeToTerms() { check(termsCheckbox); }
    public void checkSubscribeToNewsletter() { check(newsletterCheckbox); }
    public void clickRegister() { click(registerButton); }

    /**
     * Fills every field on the registration form using the values
     * given by the caller. Terms & Conditions is always ticked because
     * the site will not allow registration without it.
     */
    public void fillRegistrationForm(String title, String firstName, String lastName, String email,
                                      String phone, String mobile, String password, boolean subscribeToNewsletter) {
        System.out.println("Filling registration form for: " + firstName + " " + lastName + " (" + email + ")");
        enterTitle(title);
        enterFirstName(firstName);
        enterLastName(lastName);
        enterEmail(email);
        enterPhone(phone);
        enterMobile(mobile);
        enterPassword(password);
        enterConfirmPassword(password);
        checkAgreeToTerms();
        if (subscribeToNewsletter) {
            checkSubscribeToNewsletter();
        }
    }

    /**
     * Shorter overload for callers that don't need Title or Newsletter -
     * skips both and does not subscribe to the newsletter.
     */
    public void fillRegistrationForm(String firstName, String lastName, String email,
                                      String phone, String mobile, String password) {
        fillRegistrationForm("", firstName, lastName, email, phone, mobile, password, false);
    }

    /**
     * Full-control overload used by the negative/boundary data-driven tests,
     * where Password and Confirm Password must sometimes be DIFFERENT
     * (e.g. to test the "passwords do not match" validation) and Terms &
     * Conditions must sometimes be left unticked on purpose.
     */
    public void fillRegistrationForm(String title, String firstName, String lastName, String email,
                                      String phone, String mobile, String password, String confirmPassword,
                                      boolean acceptTerms) {
        enterTitle(title);
        enterFirstName(firstName);
        enterLastName(lastName);
        enterEmail(email);
        enterPhone(phone);
        enterMobile(mobile);
        enterPassword(password);
        enterConfirmPassword(confirmPassword);
        if (acceptTerms) {
            checkAgreeToTerms();
        }
    }

    /** True once the user lands on a logged-in page after a successful registration. */
    public boolean isRegistrationSuccessful() {
        return isDisplayed(logoutLink);
    }

    /** True when the form was rejected by client-side validation (e.g. empty required fields). */
    public boolean isValidationErrorDisplayed() {
        return isDisplayed(firstNameValidationError);
    }

    /**
     * True when the given field shows a jQuery Validate error message.
     * The site names each error element "&lt;field name&gt;-error", e.g.
     * "email-error" or "passconf-error", so any field can be checked by
     * passing its HTML "name" attribute - no extra locators needed per field.
     */
    public boolean isFieldErrorDisplayed(String fieldName) {
        return isDisplayed(By.id(fieldName + "-error"));
    }

    /** Returns the current First Name value - used to check the maxlength truncation boundary. */
    public String getFirstNameValue() {
        return getValue(firstNameField);
    }

    public String getEmailValue() {
        return getValue(emailField);
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
