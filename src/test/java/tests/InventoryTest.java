package tests;

import base.BaseTest;
import config.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.AdminLoginPage;
import pages.InventoryPage;

/**
 * InventoryTest.java
 *
 * Covers Module 3 (Inventory): navigation to every section under the
 * admin "Inventory" sidebar menu, breadcrumb validation, and basic
 * page-loading / UI checks.
 *
 * Every section is opened directly by URL rather than by clicking the
 * sidebar (see InventoryPage for why), then checked for a proper page
 * title, breadcrumb, and heading - the same three things a manual
 * tester would glance at to confirm a page "loaded correctly".
 */
public class InventoryTest extends BaseTest {

    private InventoryPage inventoryPage;

    @BeforeMethod(dependsOnMethods = "setUp")
    public void loginAsAdmin() {
        openAdminUrl();
        new AdminLoginPage(driver).login(ConfigReader.getAdminUsername(), ConfigReader.getAdminPassword());
        inventoryPage = new InventoryPage(driver);
    }

    // ===================== SMOKE TESTS =====================

    @Test(priority = 1)
    public void testCategoriesPageLoads() {
        inventoryPage.openPage("?_g=categories");
        Assert.assertTrue(inventoryPage.isHeadingDisplayed(), "Categories page heading did not load.");
        Assert.assertEquals(inventoryPage.getHeadingText(), "Categories");
    }

    @Test(priority = 1)
    public void testProductsPageLoads() {
        inventoryPage.openPage("?_g=products&node=index");
        Assert.assertTrue(inventoryPage.isHeadingDisplayed(), "Products page heading did not load.");
        Assert.assertEquals(inventoryPage.getHeadingText(), "Product Inventory");
    }

    @Test(priority = 1)
    public void testProductsPageShowsAProductsTable() {
        inventoryPage.openPage("?_g=products&node=index");
        Assert.assertTrue(inventoryPage.isProductsTableDisplayed(), "Product list table should be visible.");
    }

    // ===================== NAVIGATION: EVERY SIDEBAR SECTION =====================

    /**
     * One row per link under the "Inventory" sidebar section (plus the two
     * "Products" tabs that are real page navigations rather than in-page
     * tab switches). Each is expected to load with a visible heading and a
     * breadcrumb trail starting at "Dashboard".
     */
    @DataProvider(name = "inventorySections")
    public Object[][] inventorySections() {
        return new Object[][]{
                {"TC01_Categories", "?_g=categories"},
                {"TC02_Products", "?_g=products&node=index"},
                {"TC03_Reviews", "?_g=products&node=reviews"},
                {"TC04_ProductOptions", "?_g=products&node=options"},
                {"TC05_PromotionalCodes", "?_g=products&node=coupons"},
                {"TC06_Manufacturers", "?_g=products&node=manufacturers"},
                {"TC07_ImportCatalogue", "?_g=products&node=import"},
                {"TC08_ExportCatalogue", "?_g=products&node=export"},
                {"TC09_BulkPriceChange", "?_g=products&node=assign&prices=1"},
                {"TC10_AssignToCategory", "?_g=products&node=assign"},
                {"TC11_AssignOptionSets", "?_g=products&node=optionsets"},
        };
    }

    @Test(dataProvider = "inventorySections", priority = 2)
    public void testInventorySectionLoads(String testCaseId, String urlSuffix) {
        inventoryPage.openPage(urlSuffix);

        Assert.assertTrue(inventoryPage.isHeadingDisplayed(), testCaseId + ": page heading did not load.");
        Assert.assertTrue(inventoryPage.isBreadcrumbDisplayed(), testCaseId + ": breadcrumb did not load.");
        Assert.assertTrue(inventoryPage.getBreadcrumbText().contains("Dashboard"),
                testCaseId + ": breadcrumb should always start at Dashboard.");
    }

    // ===================== BREADCRUMB / BUTTONS / TITLE =====================

    @Test(priority = 3)
    public void testBreadcrumbTextOnCategoriesPage() {
        inventoryPage.openPage("?_g=categories");
        Assert.assertTrue(inventoryPage.getBreadcrumbText().contains("Categories"),
                "Breadcrumb should mention the current section name.");
    }

    @Test(priority = 3)
    public void testBreadcrumbDashboardLinkNavigatesHome() {
        inventoryPage.openPage("?_g=categories");
        inventoryPage.clickDashboardBreadcrumbLink();

        Assert.assertEquals(inventoryPage.getHeadingText(), "Dashboard",
                "Clicking 'Dashboard' in the breadcrumb should return to the Dashboard page.");
    }

    @Test(priority = 3)
    public void testAddProductButtonIsPresentOnProductsPage() {
        inventoryPage.openPage("?_g=products&node=index");
        Assert.assertTrue(inventoryPage.isAddProductLinkDisplayed(),
                "'Add Product' link should be visible on the Products page.");
    }

    @Test(priority = 3)
    public void testAddCategoryButtonIsPresentOnCategoriesPage() {
        inventoryPage.openPage("?_g=categories");
        Assert.assertTrue(inventoryPage.isAddCategoryLinkDisplayed(),
                "'Add Category' link should be visible on the Categories page.");
    }

    @Test(priority = 3)
    public void testProductsPageHasPagination() {
        inventoryPage.openPage("?_g=products&node=index");
        Assert.assertTrue(inventoryPage.isPaginationDisplayed(),
                "Product list should show pagination controls since there are more than one page of products.");
    }

    @Test(priority = 3)
    public void testAdminPageTitleIsConsistent() {
        inventoryPage.openPage("?_g=categories");
        Assert.assertEquals(inventoryPage.getPageTitle(), "Admin Control Panel",
                "Every admin page should share the same browser tab title.");
    }
}
