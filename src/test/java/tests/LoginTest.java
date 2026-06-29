package tests;

import base.BaseTest;
import config.ConfigReader;
import dataproviders.LoginDataProvider;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;

public class LoginTest extends BaseTest {

    @Test(
            priority = 1,
            description = "TC-02: Log in with registered credentials",
            dataProvider = "validLoginData",
            dataProviderClass = LoginDataProvider.class
    )
    public void testLogin(String username, String password) {
        driver.get(ConfigReader.getBaseUrl());

        HomePage homePage = new HomePage(driver);
        homePage.clickLogin();

        LoginPage loginPage = new LoginPage(driver);
        String welcomeText = loginPage.login(username, password);

        System.out.println("[TC-02] Welcome text: " + welcomeText);

        Assert.assertTrue(welcomeText.toLowerCase().contains("welcome"),
                "Welcome label missing after login. Got: " + welcomeText);
        Assert.assertTrue(welcomeText.contains(username),
                "Welcome label does not contain username. Got: " + welcomeText);
    }

    @Test(
            priority = 2,
            description = "TC-03: Verify page title and navbar brand",
            dataProvider = "pageTitleData",
            dataProviderClass = LoginDataProvider.class
    )
    public void testPageTitle(String expectedTitle, String expectedBrand) {
        driver.get(ConfigReader.getBaseUrl());

        HomePage homePage = new HomePage(driver);

        String browserTitle = homePage.getPageTitle();
        String navbarBrand  = homePage.getNavbarBrandText();

        System.out.println("[TC-03] Title: " + browserTitle + " | Brand: " + navbarBrand);

        Assert.assertEquals(browserTitle, expectedTitle,
                "Browser tab title mismatch.");
        Assert.assertTrue(navbarBrand.toUpperCase().contains(expectedBrand.toUpperCase()),
                "Navbar brand mismatch. Got: " + navbarBrand);
    }

    @Test(
            priority = 3,
            description = "TC-02-N: Negative login scenarios",
            dataProvider = "negativeLoginData",
            dataProviderClass = LoginDataProvider.class
    )
    public void testLoginNegative(String username, String password,
                                  String expectedAlert, String scenario) {
        driver.get(ConfigReader.getBaseUrl());

        new HomePage(driver).clickLogin();

        LoginPage loginPage = new LoginPage(driver);
        String alertMsg = loginPage.loginAndGetAlert(username, password);

        System.out.println("\n──────────────────────────────────────");
        System.out.println("  Scenario : " + scenario);
        System.out.println("  Username : '" + username + "'");
        System.out.println("  Password : '" + password + "'");
        System.out.println("  Alert    : " + alertMsg);
        System.out.println("──────────────────────────────────────");

        Assert.assertTrue(
                alertMsg.contains(expectedAlert),
                scenario + " | Expected alert to contain: '"
                        + expectedAlert + "' but got: '" + alertMsg + "'"
        );

        System.out.println(" + Correctly rejected: " + alertMsg);
    }

}
