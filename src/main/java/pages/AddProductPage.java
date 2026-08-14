package pages;

import base.BasePage;
import config.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

/**
 * AddProductPage.java
 *
 * Page Object for the CubeCart Admin "Add Product" screen
 * (Dashboard > Inventory > Products > Add Product).
 *
 * The Add Product form is a single page split into tabs (General,
 * Description, Pricing, Categories, Options, Images, Digital, Search
 * Engines). Every tab is already present in the DOM - clicking a tab
 * just shows/hides its section, so Selenium can fill fields on a tab
 * without needing to click it first, as long as the element is
 * visible after switching tabs.
 *
 * NOTE ON OPTIONS / IMAGES / DIGITAL TABS:
 * These tabs are intentionally not automated here. "Options" builds
 * table rows dynamically with JavaScript, and "Images" uploads files
 * through a drag-and-drop widget (Dropzone.js) with no plain
 * &lt;input type="file"&gt; in the DOM. Both need more advanced
 * handling (JavascriptExecutor / Robot class) that goes beyond a
 * beginner-friendly framework, so they are left out on purpose.
 *
 * NOTE ON DESCRIPTION FIELDS:
 * The "Description" and "Short Description" fields are turned into
 * CKEditor rich-text editors by the page's JavaScript, which hides
 * the real &lt;textarea&gt; and replaces it with an iframe. Selenium
 * cannot type into a hidden textarea, so its value is set directly
 * through CKEditor's own JavaScript API instead.
 */
public class AddProductPage extends BasePage {

    // ===================== SIDEBAR NAVIGATION =====================
    // The "Add Product" link on the Products list page (Inventory > Products in the sidebar).
    private By addProductButton = By.xpath("//a[normalize-space()='Add Product']");

    // ===================== TABS =====================
    private By generalTab = By.cssSelector("a[href='#general']");
    private By descriptionTab = By.cssSelector("a[href='#description']");
    private By pricingTab = By.cssSelector("a[href='#pricing']");
    private By categoriesTab = By.cssSelector("a[href='#category']");
    private By seoTab = By.cssSelector("a[href='#seo']");

    // ===================== GENERAL TAB =====================
    private By productNameField = By.id("name");
    private By manufacturerDropdown = By.id("manufacturer");
    private By conditionDropdown = By.id("condition");
    private By productCodeField = By.id("product_code");
    private By productWeightField = By.id("product_weight");
    private By dimensionUnitDropdown = By.id("dimension_unit");
    private By productWidthField = By.id("product_width");
    private By productHeightField = By.id("product_height");
    private By productDepthField = By.id("product_depth");
    private By stockLevelField = By.id("stock_level");
    private By stockWarningField = By.id("stock_warning");

    // ===================== PRICING TAB =====================
    private By retailPriceField = By.id("price");
    private By salePriceField = By.id("sale_price");
    private By costPriceField = By.id("cost_price");

    // ===================== CATEGORIES TAB =====================
    private By firstCategoryCheckbox = By.cssSelector("input.check_cat");

    // ===================== SEARCH ENGINES (SEO) TAB =====================
    private By seoMetaTitleField = By.id("seo_meta_title");
    private By seoUrlPathField = By.id("seo_path");
    private By seoMetaDescriptionField = By.id("seo_meta_description");

    // ===================== FORM ACTIONS =====================
    private By saveButton = By.cssSelector("input[type='submit'][value='Save']");
    private By saveAndReloadButton = By.cssSelector("input[name='submit_cont']");

    // Green banner shown on the product list page after a successful save
    private By successMessage = By.cssSelector("div.success");

    // Red "required" outline the site adds to the Product Name field when Save is clicked empty
    private By productNameValidationError = By.cssSelector("#name.error, #name.required-error");

    public AddProductPage(WebDriver driver) {
        super(driver);
    }

    // ===================== SIDEBAR NAVIGATION =====================

    /**
     * Opens the Products list under Inventory (Dashboard > Inventory > Products)
     * directly by URL. The sidebar's "Inventory" section is a collapsed,
     * JavaScript-animated accordion on a fresh page load, which makes clicking
     * it a common source of flaky "element not interactable" failures. Loading
     * the Products list URL directly reaches the exact same page reliably.
     */
    public void openProductsListPage() {
        driver.get(ConfigReader.getAdminUrl() + "?_g=products&node=index");
    }

    public void clickAddProductButton() { click(addProductButton); }

    /** Follows the real admin navigation: Inventory > Products > Add Product. */
    public void navigateToAddProductPage() {
        System.out.println("Navigating to Inventory > Products > Add Product...");
        openProductsListPage();
        clickAddProductButton();
    }

    // ===================== PAGE STATE =====================

    public boolean isAddProductPageDisplayed() {
        return isDisplayed(productNameField);
    }

    public boolean isProductSavedSuccessfully() {
        return isDisplayed(successMessage);
    }

    public boolean isProductNameValidationErrorDisplayed() {
        return isPresent(productNameValidationError);
    }

    public String getProductNameValue() {
        return getValue(productNameField);
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    // ===================== TAB NAVIGATION =====================

    public void openGeneralTab() { click(generalTab); }
    public void openDescriptionTab() { click(descriptionTab); }
    public void openPricingTab() { click(pricingTab); }
    public void openCategoriesTab() { click(categoriesTab); }
    public void openSeoTab() { click(seoTab); }

    // ===================== GENERAL TAB =====================

    public void enterProductName(String name) { type(productNameField, name); }
    public void enterProductCode(String code) { type(productCodeField, code); }
    public void selectManufacturer(String manufacturer) { selectByVisibleText(manufacturerDropdown, manufacturer); }
    public void selectCondition(String condition) { selectByVisibleText(conditionDropdown, condition); }
    public void enterProductWeight(String weight) { type(productWeightField, weight); }
    public void selectDimensionUnit(String unit) { selectByVisibleText(dimensionUnitDropdown, unit); }
    public void enterProductWidth(String width) { type(productWidthField, width); }
    public void enterProductHeight(String height) { type(productHeightField, height); }
    public void enterProductDepth(String depth) { type(productDepthField, depth); }
    public void enterStockLevel(String stockLevel) { type(stockLevelField, stockLevel); }
    public void enterStockWarningLevel(String stockWarning) { type(stockWarningField, stockWarning); }

    /** Fills the "Basic Information" + "Stock Control" fields on the General tab. */
    public void fillGeneralInformation(String productName, String productCode, String manufacturer,
                                        String condition, String weight, String stockLevel) {
        enterProductName(productName);
        enterProductCode(productCode);
        selectManufacturer(manufacturer);
        selectCondition(condition);
        enterProductWeight(weight);
        enterStockLevel(stockLevel);
    }

    // ===================== DESCRIPTION TAB (CKEditor) =====================

    /** Sets the rich-text "Description" field using CKEditor's JavaScript API. */
    public void enterDescription(String description) {
        setCkEditorContent("description", description);
    }

    /** Sets the rich-text "Short Description" field using CKEditor's JavaScript API. */
    public void enterShortDescription(String shortDescription) {
        setCkEditorContent("description_short", shortDescription);
    }

    private void setCkEditorContent(String editorInstanceId, String content) {
        ((JavascriptExecutor) driver).executeScript(
                "CKEDITOR.instances['" + editorInstanceId + "'].setData(arguments[0]);", content);
    }

    // ===================== PRICING TAB =====================

    public void enterRetailPrice(String price) { type(retailPriceField, price); }
    public void enterSalePrice(String salePrice) { type(salePriceField, salePrice); }
    public void enterCostPrice(String costPrice) { type(costPriceField, costPrice); }

    // ===================== CATEGORIES TAB =====================

    /** Assigns the product to the first category in the list. */
    public void selectFirstCategory() { check(firstCategoryCheckbox); }

    // ===================== SEARCH ENGINES (SEO) TAB =====================

    public void enterSeoMetaTitle(String metaTitle) { type(seoMetaTitleField, metaTitle); }
    public void enterSeoUrlPath(String urlPath) { type(seoUrlPathField, urlPath); }
    public void enterSeoMetaDescription(String metaDescription) { type(seoMetaDescriptionField, metaDescription); }

    // ===================== FORM ACTIONS =====================

    public void clickSave() { click(saveButton); }
    public void clickSaveAndReload() { click(saveAndReloadButton); }

    /**
     * End-to-end helper that fills every automated tab (General,
     * Description, Pricing, Categories, Search Engines) with the given
     * data and saves the product. Used by the "valid details" test.
     */
    public void addProductWithFullDetails(String productName, String productCode, String manufacturer,
                                           String condition, String weight, String stockLevel,
                                           String description, String shortDescription,
                                           String retailPrice, String seoMetaTitle) {
        System.out.println("Adding new product: " + productName + " (code: " + productCode + ")");
        fillGeneralInformation(productName, productCode, manufacturer, condition, weight, stockLevel);

        openDescriptionTab();
        enterDescription(description);
        enterShortDescription(shortDescription);

        openPricingTab();
        enterRetailPrice(retailPrice);

        openCategoriesTab();
        selectFirstCategory();

        openSeoTab();
        enterSeoMetaTitle(seoMetaTitle);

        clickSave();
    }
}
