package pages;

import base.BasePage;
import config.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * InventoryPage.java
 *
 * Page Object for Module 3 (Inventory): the Categories, Products, and
 * related admin sections reachable from the "Inventory" sidebar menu.
 *
 * Every one of these pages shares the same header layout (a breadcrumb
 * trail under "Dashboard" and an h3 page title), so one small, reusable
 * Page Object can validate all of them instead of writing a near-identical
 * class per sidebar link.
 *
 * NOTE: pages are opened directly by URL rather than by clicking the
 * sidebar's "Inventory" accordion menu. That menu is a JavaScript-animated,
 * collapsed-by-default widget which was found to cause flaky
 * "element not interactable" failures when clicked through Selenium (see
 * AddProductPage for the same issue). Navigating straight to the URL a
 * sidebar link points to reaches the exact same page reliably.
 */
public class InventoryPage extends BasePage {

    private By pageHeading = By.cssSelector("h3");
    private By breadcrumb = By.cssSelector(".location");
    private By productsTable = By.cssSelector("table");
    private By addProductLink = By.xpath("//a[normalize-space()='Add Product']");
    private By addCategoryLink = By.xpath("//a[normalize-space()='Add Category']");
    private By dashboardBreadcrumbLink = By.cssSelector(".location a[href='?']");
    private By pagination = By.cssSelector(".pagination");

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    /** Opens any admin page by its query-string suffix, e.g. "?_g=categories". */
    public void openPage(String urlSuffix) {
        driver.get(ConfigReader.getAdminUrl() + urlSuffix);
    }

    public boolean isHeadingDisplayed() { return isDisplayed(pageHeading); }
    public String getHeadingText() { return getText(pageHeading); }

    public boolean isBreadcrumbDisplayed() { return isDisplayed(breadcrumb); }
    public String getBreadcrumbText() { return getText(breadcrumb); }

    public boolean isProductsTableDisplayed() { return isDisplayed(productsTable); }
    public boolean isAddProductLinkDisplayed() { return isDisplayed(addProductLink); }
    public boolean isAddCategoryLinkDisplayed() { return isDisplayed(addCategoryLink); }
    public boolean isPaginationDisplayed() { return isDisplayed(pagination); }

    public void clickDashboardBreadcrumbLink() { click(dashboardBreadcrumbLink); }

    public String getPageTitle() { return driver.getTitle(); }
}
