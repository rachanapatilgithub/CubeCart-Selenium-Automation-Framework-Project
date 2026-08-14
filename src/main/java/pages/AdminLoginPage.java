package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * AdminLoginPage.java
 *
 * Page Object for the CubeCart Admin Control Panel login page
 * (https://javabykiran.in/other/CC/admin_zE82E2.php).
 */
public class AdminLoginPage extends BasePage {

    private By usernameField = By.id("username");
    private By passwordField = By.id("password");
    private By loginButton = By.id("login");

    private By pageHeading = By.xpath("//h1[normalize-space()='Store Control Panel Login']");

    // Shown at the top right of every admin page once logged in, e.g. "Welcome back Admin"
    private By welcomeText = By.cssSelector("span.user_info");

    // Shown on the login page itself when the username/password is rejected
    private By errorMessage = By.cssSelector("div.error");

    private By logoutLink = By.cssSelector("a[href*='_g=logout']");
    private By forgotPasswordLink = By.linkText("Forgotten your password?");

    public AdminLoginPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoginPageDisplayed() { return isDisplayed(pageHeading); }
    public boolean isDashboardDisplayed() { return isDisplayed(welcomeText); }
    public boolean isErrorMessageDisplayed() { return isDisplayed(errorMessage); }
    public boolean isForgotPasswordLinkDisplayed() { return isDisplayed(forgotPasswordLink); }

    public String getUsernameFieldValue() { return getValue(usernameField); }
    public String getPasswordFieldType() { return driver.findElement(passwordField).getAttribute("type"); }

    public void enterUsername(String username) { type(usernameField, username); }
    public void enterPassword(String password) { type(passwordField, password); }
    public void clickLogin() { click(loginButton); }
    public void clickLogout() { click(logoutLink); }

    public void login(String username, String password) {
        System.out.println("Logging in to Admin Panel as: " + username);
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }
}
