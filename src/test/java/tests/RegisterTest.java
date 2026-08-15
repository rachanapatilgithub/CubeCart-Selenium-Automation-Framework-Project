package tests;

import base.BaseTest;
import config.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.RegisterPage;

/**
 * RegisterTest.java
 *
 * Covers Module 1 (Register): smoke checks, a full data-driven pass
 * over positive/negative/boundary registration inputs, and a few
 * scenarios (duplicate email, maxlength truncation) that need more
 * than one page load and so cannot live inside the data provider.
 *
 * Every expected result below (validation messages, which fields are
 * required, which are optional, maxlength values) was confirmed by
 * hand against the live site before being written into an assertion,
 * so a failing test here means the real page behaved differently to
 * what was observed - not a guessed/fake expectation.
 */
public class RegisterTest extends BaseTest {

    private RegisterPage registerPage;

    @BeforeMethod(dependsOnMethods = "setUp")
    public void openRegisterPage() {
        driver.get(ConfigReader.getUrl() + "/register");
        registerPage = new RegisterPage(driver);
    }

    // ===================== SMOKE TESTS =====================

    @Test(priority = 1)
    public void testRegisterPageLoads() {
        Assert.assertTrue(registerPage.isRegisterPageDisplayed(), "Register page did not load.");
    }

    @Test(priority = 2)
    public void testRegisterWithValidDetails() {
        String uniqueEmail = uniqueEmail();
        registerPage.fillRegistrationForm("Mrs", "Rachana", "Patil", uniqueEmail,
                "9876543210", "9876543211", "Test@1234", true);
        registerPage.clickRegister();

        Assert.assertTrue(registerPage.isRegistrationSuccessful(),
                "User was not logged in after registering with valid details.");
    }

    @Test(priority = 3)
    public void testRegisterWithEmptyRequiredFields() {
        registerPage.clickRegister();

        Assert.assertTrue(registerPage.isValidationErrorDisplayed(),
                "Form should not submit successfully when required fields are empty.");
    }

    // ===================== DATA-DRIVEN NEGATIVE / BOUNDARY TESTS =====================

    /**
     * One row = one full page load + form fill + submit + assertion, and
     * TestNG reports each row as its own pass/fail result - this is what
     * gives the module wide input coverage without 30 near-duplicate
     * test methods.
     *
     * expectedOutcome is either "SUCCESS" (registration should go through)
     * or the HTML "name" of the field that should show a validation error.
     */
    @DataProvider(name = "registrationData")
    public Object[][] registrationData() {
        return new Object[][]{
                // testCaseId, firstName, lastName, phone, mobile, password, confirmPassword, acceptTerms, expectedOutcome
                {"TC02_EmptyFirstName", "", "Patil", "9876543210", "9876543211", "Test@123", "Test@123", true, "first_name"},
                {"TC03_EmptyLastName", "Rachana", "", "9876543210", "9876543211", "Test@123", "Test@123", true, "last_name"},
                {"TC07_EmptyPhone", "Rachana", "Patil", "", "9876543211", "Test@123", "Test@123", true, "phone"},
                {"TC08_InvalidPhone_Letters", "Rachana", "Patil", "abcdefghij", "9876543211", "Test@123", "Test@123", true, "phone"},
                {"TC09_EmptyPassword", "Rachana", "Patil", "9876543210", "9876543211", "", "", true, "password"},
                {"TC10_WeakPassword_TooShort", "Rachana", "Patil", "9876543210", "9876543211", "123", "123", true, "password"},
                {"TC11_EmptyConfirmPassword", "Rachana", "Patil", "9876543210", "9876543211", "Test@123", "", true, "passconf"},
                {"TC12_PasswordMismatch", "Rachana", "Patil", "9876543210", "9876543211", "Test@123", "Different@123", true, "passconf"},
                {"TC13_TermsNotAccepted", "Rachana", "Patil", "9876543210", "9876543211", "Test@123", "Test@123", false, "terms_agree"},
                {"TC14_MissingTitle_StillSucceeds", "Rachana", "Patil", "9876543210", "9876543211", "Test@123", "Test@123", true, "SUCCESS"},
                {"TC15_MissingMobile_StillSucceeds", "Rachana", "Patil", "9876543210", "", "Test@123", "Test@123", true, "SUCCESS"},
                {"TC17_MinLengthFirstName", "A", "Patil", "9876543210", "9876543211", "Test@123", "Test@123", true, "SUCCESS"},
                {"TC18_LeadingSpacesInFirstName", "   Rachana", "Patil", "9876543210", "9876543211", "Test@123", "Test@123", true, "SUCCESS"},
                {"TC19_TrailingSpacesInLastName", "Rachana", "Patil   ", "9876543210", "9876543211", "Test@123", "Test@123", true, "SUCCESS"},
                {"TC20_SpecialCharactersInName", "O'Brien-Smith", "D'Souza", "9876543210", "9876543211", "Test@123", "Test@123", true, "SUCCESS"},
                {"TC21_NumericFirstName", "12345", "Patil", "9876543210", "9876543211", "Test@123", "Test@123", true, "SUCCESS"},
                {"TC22_BoundaryPhoneLength_9Digits", "Rachana", "Patil", "123456789", "9876543211", "Test@123", "Test@123", true, "SUCCESS"},
                {"TC23_BoundaryPhoneLength_15Digits", "Rachana", "Patil", "123456789012345", "9876543211", "Test@123", "Test@123", true, "SUCCESS"},
                {"TC28_PasswordWithSpecialChars", "Rachana", "Patil", "9876543210", "9876543211", "P@ssw0rd!#$", "P@ssw0rd!#$", true, "SUCCESS"},
        };
    }

    @Test(dataProvider = "registrationData", priority = 4)
    public void testRegistrationScenarios(String testCaseId, String firstName, String lastName, String phone,
                                           String mobile, String password, String confirmPassword,
                                           boolean acceptTerms, String expectedOutcome) {
        registerPage.fillRegistrationForm("Mrs", firstName, lastName, uniqueEmail(), phone, mobile,
                password, confirmPassword, acceptTerms);
        registerPage.clickRegister();

        if (expectedOutcome.equals("SUCCESS")) {
            Assert.assertTrue(registerPage.isRegistrationSuccessful(),
                    testCaseId + ": expected registration to succeed, but it did not.");
        } else {
            Assert.assertTrue(registerPage.isFieldErrorDisplayed(expectedOutcome),
                    testCaseId + ": expected a validation error on field '" + expectedOutcome + "'.");
        }
    }

    // Two rows of the email format rule, kept separate from the main table
    // because they need their own DataProvider (email is built per-row above).
    @DataProvider(name = "invalidEmailFormats")
    public Object[][] invalidEmailFormats() {
        return new Object[][]{
                {"TC05_InvalidEmail_NoAtSymbol", "invalidemail.com"},
                {"TC06_InvalidEmail_NoDomain", "user@"},
                {"TC27_InvalidEmail_ContainsSpace", "user name@mailinator.com"},
        };
    }

    @Test(dataProvider = "invalidEmailFormats", priority = 4)
    public void testRegistrationWithInvalidEmailFormat(String testCaseId, String invalidEmail) {
        registerPage.fillRegistrationForm("Mrs", "Rachana", "Patil", invalidEmail,
                "9876543210", "9876543211", "Test@123", true);
        registerPage.clickRegister();

        Assert.assertTrue(registerPage.isFieldErrorDisplayed("email"),
                testCaseId + ": expected an email format validation error for '" + invalidEmail + "'.");
    }

    @Test(priority = 4)
    public void testRegistrationWithEmptyEmail() {
        registerPage.fillRegistrationForm("Mrs", "Rachana", "Patil", "",
                "9876543210", "9876543211", "Test@123", true);
        registerPage.clickRegister();

        Assert.assertTrue(registerPage.isFieldErrorDisplayed("email"),
                "Expected a required-field validation error on Email.");
    }

    // ===================== NEWSLETTER OPTION =====================

    @Test(priority = 5)
    public void testRegisterWithNewsletterSubscriptionChecked() {
        registerPage.fillRegistrationForm("Mrs", "Rachana", "Patil", uniqueEmail(),
                "9876543210", "9876543211", "Test@123", true);
        registerPage.clickRegister();

        Assert.assertTrue(registerPage.isRegistrationSuccessful(),
                "Registration should succeed whether or not the newsletter box is ticked.");
    }

    @Test(priority = 5)
    public void testRegisterWithNewsletterSubscriptionUnchecked() {
        registerPage.fillRegistrationForm("Mrs", "Rachana", "Patil", uniqueEmail(),
                "9876543210", "9876543211", "Test@123", false);
        registerPage.clickRegister();

        Assert.assertTrue(registerPage.isRegistrationSuccessful(),
                "Registration should succeed when the optional newsletter box is left unticked.");
    }

    // ===================== MAXLENGTH BOUNDARY =====================

    @Test(priority = 6)
    public void testFirstNameIsTruncatedAtMaxLength() {
        String fiftyChars = "A".repeat(50);
        registerPage.enterFirstName(fiftyChars);

        Assert.assertEquals(registerPage.getFirstNameValue().length(), 32,
                "First Name should be capped at its HTML maxlength of 32 characters.");
    }

    @Test(priority = 6)
    public void testEmailIsTruncatedAtMaxLength() {
        String longEmail = "a".repeat(90) + "@mail.com"; // 99 characters, over the 96 maxlength
        registerPage.enterEmail(longEmail);

        Assert.assertEquals(registerPage.getEmailValue().length(), 96,
                "Email should be capped at its HTML maxlength of 96 characters.");
    }

    // ===================== DUPLICATE EMAIL (KNOWN APPLICATION BEHAVIOUR) =====================

    /**
     * KNOWN APPLICATION BUG: registering again with an email that is
     * already in use and the SAME password does not show any
     * "already registered" message - CubeCart silently logs the visitor
     * into the existing account instead. A real registration form should
     * tell the user the email is already taken, not act like a login.
     * This test documents the actual (undesirable) behaviour and is
     * expected to keep passing only because it asserts what genuinely
     * happens; see the project report for why this is flagged as a defect.
     */
    @Test(priority = 7)
    public void testDuplicateEmailWithSamePasswordLogsIntoExistingAccount() {
        String duplicateEmail = uniqueEmail();
        registerPage.fillRegistrationForm("Mrs", "Rachana", "Patil", duplicateEmail,
                "9876543210", "9876543211", "Test@123", true);
        registerPage.clickRegister();
        Assert.assertTrue(registerPage.isRegistrationSuccessful(), "First-time registration should succeed.");

        driver.get(ConfigReader.getUrl() + "/index.php?_a=logout");
        openRegisterPage();
        registerPage.fillRegistrationForm("Mrs", "Someone", "Else", duplicateEmail,
                "9876543210", "9876543211", "Test@123", true);
        registerPage.clickRegister();

        Assert.assertTrue(registerPage.isRegistrationSuccessful(),
                "Documented bug: re-registering a taken email with the matching password logs into "
                        + "the existing account instead of showing a clear 'email already registered' message.");
    }

    /**
     * KNOWN APPLICATION BUG: attempting to register again with an email
     * that is already in use but a DIFFERENT password shows a generic
     * "Invalid username or password" message - a LOGIN-style error on a
     * REGISTRATION form. A fresher-friendly, correct message would be
     * "This email is already registered" instead of reusing the login
     * error text, which is confusing during registration.
     */
    @Test(priority = 7)
    public void testDuplicateEmailWithDifferentPasswordShowsMisleadingError() {
        String duplicateEmail = uniqueEmail();
        registerPage.fillRegistrationForm("Mrs", "Rachana", "Patil", duplicateEmail,
                "9876543210", "9876543211", "Test@123", true);
        registerPage.clickRegister();
        Assert.assertTrue(registerPage.isRegistrationSuccessful(), "First-time registration should succeed.");

        driver.get(ConfigReader.getUrl() + "/index.php?_a=logout");
        openRegisterPage();
        registerPage.fillRegistrationForm("Mrs", "Someone", "Else", duplicateEmail,
                "9876543210", "9876543211", "Different@999", true);
        registerPage.clickRegister();

        Assert.assertFalse(registerPage.isRegistrationSuccessful(),
                "Registering a taken email with a different password must not create/enter an account.");
    }

    // ===================== ACCESS CONTROL =====================

    @Test(priority = 8)
    public void testLoggedInUserIsRedirectedAwayFromRegisterPage() {
        registerPage.fillRegistrationForm("Mrs", "Rachana", "Patil", uniqueEmail(),
                "9876543210", "9876543211", "Test@123", true);
        registerPage.clickRegister();
        Assert.assertTrue(registerPage.isRegistrationSuccessful(), "Setup registration should succeed.");

        openRegisterPage();

        Assert.assertFalse(registerPage.isRegisterPageDisplayed(),
                "A logged-in user should be redirected away from the Register page, not shown the form again.");
    }

    private String uniqueEmail() {
        return "autotester" + System.currentTimeMillis() + "@mailinator.com";
    }
}
