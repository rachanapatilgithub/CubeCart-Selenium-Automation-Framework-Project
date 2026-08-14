package base;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import utils.WaitUtils;

/**
 * BasePage.java
 *
 * Parent class for every Page Object in the framework.
 * It holds the WebDriver instance and provides reusable, wait-safe
 * actions (click, type, select, read text) so that individual page
 * classes only need to define their locators and business methods.
 */
public class BasePage {
    protected WebDriver driver;
    protected WaitUtils waitUtils;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver);
    }

    /** Waits for the element to be clickable, then clicks it. */
    protected void click(By locator) {
        try {
            waitUtils.waitForClickability(locator).click();
        } catch (ElementNotInteractableException e) {
            // Some elements (e.g. an accordion-style sidebar menu) report as
            // clickable a moment before their open/close animation finishes.
            // A brief pause and a single retry fixes that without adding a
            // sleep to every action in the framework.
            sleepBriefly();
            waitUtils.waitForClickability(locator).click();
        }
    }

    private void sleepBriefly() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /** Waits for the element to be visible, clears it, then types the given text. */
    protected void type(By locator, String text) {
        WebElement element = waitUtils.waitForVisibility(locator);
        element.clear();
        element.sendKeys(text);
    }

    /** Selects a dropdown option by its visible text. */
    protected void selectByVisibleText(By locator, String visibleText) {
        WebElement dropdown = waitUtils.waitForVisibility(locator);
        new Select(dropdown).selectByVisibleText(visibleText);
    }

    /** Ticks a checkbox only if it is not already checked. */
    protected void check(By locator) {
        try {
            WebElement checkbox = waitUtils.waitForClickability(locator);
            if (!checkbox.isSelected()) {
                checkbox.click();
            }
        } catch (ElementNotInteractableException | TimeoutException e) {
            // A handful of checkboxes on long, dynamically laid-out admin
            // pages (e.g. a category tree) never satisfy Selenium's native
            // click conditions even though they are visibly on screen. In
            // that case, ticking the box directly through JavaScript and
            // firing a "change" event is a safe, well-known fallback.
            WebElement checkbox = driver.findElement(locator);
            if (!checkbox.isSelected()) {
                ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].checked = true; arguments[0].dispatchEvent(new Event('change'));", checkbox);
            }
        }
    }

    /** Returns the visible text of an element, waiting for it to appear first. */
    protected String getText(By locator) {
        return waitUtils.waitForVisibility(locator).getText();
    }

    /** Returns true if the element becomes visible within the explicit wait time. */
    public boolean isDisplayed(By locator) {
        try {
            return waitUtils.waitForVisibility(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /** Returns true if the element exists in the DOM, without waiting for visibility. */
    protected boolean isPresent(By locator) {
        return !driver.findElements(locator).isEmpty();
    }

    /** Returns the current value typed into an input field. */
    protected String getValue(By locator) {
        return waitUtils.waitForVisibility(locator).getAttribute("value");
    }
}
