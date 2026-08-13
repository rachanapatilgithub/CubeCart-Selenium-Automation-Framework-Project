# CubeCart E-Commerce Automation Framework

A complete, beginner-friendly Selenium + TestNG + Cucumber automation framework
built for interview preparation (Software Testing Fresher role).

The framework automates five modules of a live CubeCart demo store:

1. User Registration
2. User Login
3. Admin Login
4. Inventory Navigation (Admin Panel)
5. Add Product (Admin Panel)

**146 test methods** across these 5 modules, including data-driven positive/negative/boundary
cases, and **3 genuine application defects found and deliberately left failing** (negative price
accepted, negative stock accepted, duplicate product code accepted) - see section 13.

---

## 1. Project Overview

This project follows the **Page Object Model (POM)** design pattern and
supports two ways of running tests:

- **Plain TestNG + Selenium tests** (`src/test/java/tests`)
- **BDD tests using Cucumber** (`src/test/resources/features` + step definitions)

Both styles reuse the exact same Page classes (`RegisterPage`, `LoginPage`,
`AdminLoginPage`, `InventoryPage`, `AddProductPage`), which is a realistic, industry-standard
way of structuring a framework.

**Application Under Test (AUT):**
- End User Store: `https://javabykiran.in/other/CC`
- Admin Panel: `https://javabykiran.in/other/CC/admin_zE82E2.php`

> ⚠️ These values are stored in `config.properties`, not hard-coded in Java,
> so they can be changed easily if the demo store URL ever changes.

---

## 2. Technology Stack

| Category           | Technology              |
|---------------------|--------------------------|
| Language            | Java 17                 |
| Automation Tool     | Selenium WebDriver 4     |
| Test Framework      | TestNG (incl. DataProvider) |
| BDD Framework       | Cucumber (Java + TestNG) |
| Build Tool          | Maven                   |
| Design Pattern      | Page Object Model (POM)  |
| Driver Management   | WebDriverManager (Bonigarcia) |
| Reporting           | ExtentReports (HTML, with screenshots on failure) |
| IDE                 | Eclipse IDE              |
| Browser             | Google Chrome            |

---

## 3. Folder Structure

```
CubeCartAutomationFramework
│
├── src
│   ├── main
│   │   └── java
│   │       ├── base       -> DriverFactory, BasePage
│   │       ├── pages      -> RegisterPage, LoginPage, AdminLoginPage, InventoryPage, AddProductPage
│   │       ├── utils      -> WaitUtils, ScreenshotUtils
│   │       └── config     -> ConfigReader
│   │
│   └── test
│       ├── java
│       │   ├── base             -> BaseTest
│       │   ├── tests            -> RegisterTest, LoginTest, AdminLoginTest, InventoryTest, AddProductTest
│       │   ├── listeners        -> ExtentManager, ExtentTestListener (HTML reporting)
│       │   ├── runner           -> TestRunner (Cucumber + TestNG bridge)
│       │   ├── hooks            -> Hooks (Cucumber @Before / @After)
│       │   └── stepdefinitions  -> Step definition classes for each feature file
│       │
│       └── resources
│           └── features   -> Register.feature, Login.feature, AdminLogin.feature, AddProduct.feature
│
├── screenshots            -> auto-created; stores screenshots of every test (pass and fail)
├── test-output/ExtentReport/ExtentReport.html  -> HTML test report, generated after each run
├── config.properties      -> URLs, admin credentials, wait times
├── pom.xml                -> Maven dependencies and build configuration
├── testng.xml             -> TestNG suite file (runs both Java tests, Cucumber tests, and the Extent listener)
└── README.md
```

---

## 4. How to Import into Eclipse

1. Open Eclipse IDE.
2. Go to **File > Import > Maven > Existing Maven Projects**.
3. Click **Next**, then **Browse** and select the `CubeCartAutomationFramework` folder.
4. Eclipse will detect `pom.xml` automatically. Click **Finish**.
5. Wait for Maven to download all dependencies (internet connection required).
6. Right-click the project → **Maven → Update Project** if you see any red error marks.

---

## 5. How to Run with Maven

Open a terminal inside the project folder and run:

```bash
mvn clean test
```

This will download dependencies, compile the project, and run `testng.xml` - both the plain
TestNG tests and the Cucumber tests, with the ExtentReports listener attached.

---

## 6. How to Run with TestNG (inside Eclipse)

1. Right-click on `testng.xml` in the project root.
2. Select **Run As → TestNG Suite**.
3. All test classes listed inside `testng.xml` will execute in Chrome.
4. Two reports are generated automatically:
   - `test-output/index.html` - TestNG's own report
   - `test-output/ExtentReport/ExtentReport.html` - a cleaner HTML report with a screenshot for every test

You can also right-click any single Test class
(e.g. `RegisterTest.java`) → **Run As → TestNG Test** to run it alone.

---

## 7. How to Run Cucumber Feature Files

Cucumber scenarios are executed through `TestRunner.java`
(found in `src/test/java/runner`), which is already wired into `testng.xml`.

To run only Cucumber tests:
1. Right-click `TestRunner.java`.
2. Select **Run As → TestNG Test**.

Cucumber HTML/JSON reports are generated at:
```
target/cucumber-reports/cucumber.html
target/cucumber-reports/cucumber.json
```

---

## 8. Framework Architecture

```
Test Layer (tests/*, stepdefinitions/*)
        │
        ▼
Page Object Layer (pages/*)   <-- contains locators + page actions
        │
        ▼
Base Layer (base/BasePage, base/DriverFactory)  <-- reusable click/type/wait logic
        │
        ▼
Utility Layer (utils/WaitUtils, utils/ScreenshotUtils)
        │
        ▼
Listener Layer (listeners/ExtentTestListener)  <-- HTML report + screenshot per test
        │
        ▼
Config Layer (config/ConfigReader -> config.properties)
        │
        ▼
Selenium WebDriver -> Google Chrome -> CubeCart Store
```

- **DriverFactory** creates and destroys the browser session.
- **BasePage** gives every Page class shared, wait-safe methods (click, type, select, isDisplayed),
  with a retry-and-JavaScript-fallback for the rare elements that don't respond to a normal click.
- **Page classes** hold locators and page-specific actions only — no test logic.
- **Test / Step Definition classes** call Page class methods and make assertions.
- **ExtentTestListener** turns every `@Test` method into one row in the HTML report automatically.
- **ConfigReader** keeps all environment data outside the Java code.

This layered structure means a locator change only needs to be fixed in
ONE Page class, not in every test that uses that page.

---

## 9. Modules Covered

| Module              | Page Class          | Test Class        | Test Count |
|----------------------|----------------------|---------------------|------------|
| User Registration    | `RegisterPage`       | `RegisterTest`      | 33         |
| User Login           | `LoginPage`          | `LoginTest`         | 2          |
| Admin Login          | `AdminLoginPage`     | `AdminLoginTest`    | 23         |
| Inventory Navigation | `InventoryPage`      | `InventoryTest`     | 20         |
| Add Product          | `AddProductPage`     | `AddProductTest`    | 35         |

Cucumber (`.feature` files) additionally covers Register, Login, Admin Login and Add Product
through Gherkin scenarios, reusing the exact same Page classes.

---

## 10. A Note on Locators

Every locator in this framework was confirmed against the **live rendered page** (via browser
inspection), not guessed - `id` attributes were used wherever the site provided one (e.g.
`#first_name`, `#login-username`, `#product_code`), since IDs are the fastest and most stable
locator type. CSS Selectors and a small number of relative XPaths (e.g. matching a link by its
visible text) were used only where no unique `id`/`class` was available.

If your CubeCart theme differs, or a locator ever fails:
1. Open the page in Chrome.
2. Right-click the field → **Inspect**.
3. Note its `id`, `name`, or a stable attribute.
4. Update only the locator line inside the relevant Page class
   (e.g. `RegisterPage.java`) — no other file needs to change.

This is normal, real-world automation maintenance, and a great topic to
discuss in an interview: *"How do you handle a locator that breaks?"*

---

## 11. Handling Tricky UI Elements

A few real UI quirks were found and handled deliberately, worth mentioning in an interview:

- **CKEditor rich-text fields** (Add Product's Description/Short Description) replace the real
  `<textarea>` with an iframe, so `sendKeys()` can't reach it - `AddProductPage` sets this content
  through CKEditor's own JavaScript API instead.
- **An animated sidebar accordion menu** was found to cause flaky `ElementNotInteractable`
  failures when clicked through Selenium - the framework navigates to the target page directly
  by URL instead, which is more reliable.
- **Options and Images tabs** on Add Product were intentionally left out of scope - they use a
  dynamically-generated table and a drag-and-drop upload widget respectively, which need more
  advanced handling than a beginner framework should carry.

---

## 12. Future Improvements

- Add a `Logout` module and test for the end-user Login flow.
- Add cross-browser support (Firefox, Edge) via a `browser` value in `config.properties`.
- Add API-level test data cleanup (delete test users/products after each run).
- Integrate with a CI tool (e.g. Jenkins or GitHub Actions) to run tests on every commit.

---

## 13. Genuine Application Defects Found During Automation

Three tests are **expected to fail** on purpose - they assert the *correct* behaviour, and the
live CubeCart site doesn't do that:

| Test | Expected | Actual (observed defect) |
|---|---|---|
| `testAddProductWithNegativePriceShouldBeRejected` | Negative Retail Price rejected | Saved silently, shows "-500.00" |
| `testAddProductWithNegativeStockLevelShouldBeRejected` | Negative Stock rejected | Saved silently, shows "-25" |
| `testDuplicateProductCodeShouldBeRejected` | Duplicate Product Code rejected/warned | Two products saved with the identical code, no warning |

These are documented, not hidden - a real automation project finds real bugs, and this is exactly
the kind of finding worth explaining in an interview.

---

## 14. Interview Talking Points

- Why Page Object Model? → Reusability, easy maintenance, one place to fix locators.
- Why explicit waits over `Thread.sleep()`? → Faster, more reliable, avoids flaky tests.
- Why WebDriverManager? → No need to manually download/match ChromeDriver versions.
- Why separate `config.properties`? → Environment values change without touching code.
- Why both TestNG and Cucumber here? → Shows understanding of both classic and BDD styles.
- Why ExtentReports? → A clean, shareable HTML report with screenshots, better for showing
  results to a non-technical audience than the console or a raw TestNG report.
- Why did some tests fail on purpose? → They caught real defects - a good tester's job isn't to
  make everything pass, it's to prove what's actually true about the application.

---

**Project:** CubeCartAutomationFramework
**Purpose:** Selenium/Java interview preparation for Software Testing Fresher roles
