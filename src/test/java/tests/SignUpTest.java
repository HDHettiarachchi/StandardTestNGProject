package tests;

import base.BaseTest;
import config.ConfigReader;
import dataproviders.SignUpDataProvider;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.SignUpPage;

public class SignUpTest extends BaseTest {

    // ── TC-01 Positive ────────────────────────────────────────────────────────

    @Test(
            priority = 1,
            description = "TC-01: Sign up with valid credentials",
            dataProvider = "signUpData",
            dataProviderClass = SignUpDataProvider.class
    )
    public void testSignUp(String username, String password) {
        driver.get(ConfigReader.getBaseUrl());

        HomePage homePage = new HomePage(driver);
        homePage.clickSignUp();

        SignUpPage signUpPage = new SignUpPage(driver);
        String alertMsg = signUpPage.signUp(username, password);

        System.out.println("[TC-01] Alert: " + alertMsg);

        Assert.assertTrue(
                alertMsg.contains("Sign up successful") || alertMsg.contains("already exist"),
                "Unexpected sign up alert: " + alertMsg
        );
    }

    // ── TC-01 Negative — site should reject these ─────────────────────────────

    @Test(
            priority = 2,
            description = "TC-01-N: Sign up negative cases — site should reject",
            dataProvider = "negativeSignUpData",
            dataProviderClass = SignUpDataProvider.class
    )
    public void testSignUpNegative(String username, String password,
                                   String expectedAlert, String scenario) {
        driver.get(ConfigReader.getBaseUrl());

        new HomePage(driver).clickSignUp();
        String alertMsg = new SignUpPage(driver).signUp(username, password);

        System.out.println("\n──────────────────────────────────────");
        System.out.println("  Scenario : " + scenario);
        System.out.println("  Username : '" + username + "'");
        System.out.println("  Password : '" + password + "'");
        System.out.println("  Alert    : " + alertMsg);
        System.out.println("──────────────────────────────────────");

        Assert.assertTrue(
                alertMsg.contains(expectedAlert),
                scenario + " | Expected: '" + expectedAlert
                        + "' | Got: '" + alertMsg + "'"
        );
    }

    // ── TC-01 No Validation — site accepts these, flagged as bugs ────────────

    @Test(
            priority = 3,
            description = "TC-01-NV: Sign up inputs with no server validation — documenting site behaviour",
            dataProvider = "noValidationSignUpData",
            dataProviderClass = SignUpDataProvider.class
    )
    public void testSignUpNoValidation(String username, String password,
                                       String scenario, boolean isBug) {
        driver.get(ConfigReader.getBaseUrl());

        new HomePage(driver).clickSignUp();
        String alertMsg = new SignUpPage(driver).signUp(username, password);

        System.out.println("\n══════════════════════════════════════");
        System.out.println("  Scenario  : " + scenario);
        System.out.println("  Username  : '" + username + "'");
        System.out.println("  Password  : '" + password + "'");
        System.out.println("  Response  : " + alertMsg);

        if (isBug) {
            if (alertMsg.contains("Sign up successful")) {
                System.out.println("  ⚠ BUG     : Site accepted invalid input.");
                System.out.println("    → This should be rejected by the server.");
            } else {
                System.out.println("  ✔ INFO    : Site rejected with: " + alertMsg);
            }
        }

        System.out.println("══════════════════════════════════════\n");

        // We only assert a response came back — not what it says
        // These are documentation tests, not pass/fail validations
        Assert.assertNotNull(alertMsg,
                scenario + " | No response received from site.");
    }

}
