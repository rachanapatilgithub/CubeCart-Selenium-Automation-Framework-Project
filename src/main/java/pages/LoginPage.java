package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * LoginPage.java
 *
 * Page Object for the End User "Login" page
 * (https://javabykiran.in/other/CC/login).
 */
public class LoginPage extends BasePage {

    private By usernameField = By.id("login-username");
    private By passwordField = By.id("login-password");
    private By rememberMeCheckbox = By.id("login-remember");
    private By loginButton = By.cssSelector("#login_form [type='submit']");

    // Shown when CubeCart rejects the login attempt
    private By errorMessage = By.cssSelector(".alert-box.alert");

    // Shown next to the Logout link only after a successful login
    private By logoutLink = By.cssSelector("a[href*='_a=logout']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoginPageDisplayed() {
        return isDisplayed(usernameField);
    }

    public void enterEmail(String email) { type(usernameField, email); }
    public void enterPassword(String password) { type(passwordField, password); }
    public void checkRememberMe() { check(rememberMeCheckbox); }
    public void clickLogin() { click(loginButton); }

    public void login(String email, String password) {
        System.out.println("Logging in as user: " + email);
        enterEmail(email);
        enterPassword(password);
        clickLogin();
    }

    public boolean isErrorMessageDisplayed() { return isDisplayed(errorMessage); }
    public boolean isLoginSuccessful() { return isDisplayed(logoutLink); }
    public String getCurrentUrl() { return driver.getCurrentUrl(); }
}
