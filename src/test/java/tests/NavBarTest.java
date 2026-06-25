package tests;

import base.BaseTest;
import config.ConfigReader;
import org.openqa.selenium.Alert;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.NavBarPage;

import java.time.Duration;

public class NavBarTest extends BaseTest {

    // ── TC-10: All navbar links are present and visible ───────────────────────

    @Test(priority = 1, description = "TC-10: All navbar links are visible on home page")
    public void testNavbarLinksVisible() {
        driver.get(ConfigReader.getBaseUrl());
        NavBarPage navBar = new NavBarPage(driver);

        System.out.println("\n[TC-10] Checking navbar links...");

        Assert.assertTrue(navBar.isHomeLinkVisible(),
                " + DEFECT: 'Home' link not visible in navbar.");
        System.out.println(" + Home link visible");

        Assert.assertTrue(navBar.isContactLinkVisible(),
                " + DEFECT: 'Contact' link not visible in navbar.");
        System.out.println(" + Contact link visible");

        Assert.assertTrue(navBar.isAboutUsLinkVisible(),
                " + DEFECT: 'About us' link not visible in navbar.");
        System.out.println(" + About Us link visible");

        Assert.assertTrue(navBar.isCartLinkVisible(),
                " + DEFECT: 'Cart' link not visible in navbar.");
        System.out.println(" + Cart link visible");

        Assert.assertTrue(navBar.isLoginLinkVisible(),
                " + DEFECT: 'Log in' link not visible in navbar.");
        System.out.println(" + Log in link visible");

        Assert.assertTrue(navBar.isSignUpLinkVisible(),
                " + DEFECT: 'Sign up' link not visible in navbar.");
        System.out.println(" + Sign up link visible");
    }

    // ── TC-11: Navbar brand/logo click navigates to home ─────────────────────

    @Test(priority = 2, description = "TC-11: Navbar brand click navigates back to home page")
    public void testNavbarBrandNavigatesToHome() {
        driver.get(ConfigReader.getBaseUrl());
        NavBarPage navBar = new NavBarPage(driver);

        // Navigate away to cart first
        navBar.clickCart();

        // Click brand to go back home
        navBar.clickNavbarBrand();

        String currentUrl = navBar.getCurrentUrl();

        System.out.println("[TC-11] Current URL after brand click: " + currentUrl);

        Assert.assertTrue(
                currentUrl.contains("demoblaze.com"),
                " + DEFECT: Navbar brand did not navigate to home. URL: " + currentUrl
        );
        System.out.println(" + Navbar brand navigates to home");
    }

    @Test(priority = 3, description = "TC-12: Home link in navbar navigates to home page")
    public void testHomeLinkNavigation() {
        driver.get(ConfigReader.getBaseUrl());
        NavBarPage navBar = new NavBarPage(driver);

        // Navigate away first
        navBar.clickCart();

        // Click Home
        navBar.clickHome();

        String currentUrl = navBar.getCurrentUrl();

        System.out.println("[TC-12] URL after Home click: " + currentUrl);

        Assert.assertTrue(
                currentUrl.contains("demoblaze.com"),
                " + DEFECT: Home link did not navigate correctly. URL: " + currentUrl
        );
        System.out.println(" + Home link navigates correctly");
    }

    // ── TC-13: Cart link navigates to cart page ───────────────────────────────

    @Test(priority = 4, description = "TC-13: Cart link in navbar navigates to cart page")
    public void testCartLinkNavigation() {
        driver.get(ConfigReader.getBaseUrl());
        NavBarPage navBar = new NavBarPage(driver);

        navBar.clickCart();

        String currentUrl = navBar.getCurrentUrl();

        System.out.println("[TC-13] URL after Cart click: " + currentUrl);

        Assert.assertTrue(
                currentUrl.contains("cart"),
                " + DEFECT: Cart link did not navigate to cart page. URL: " + currentUrl
        );
        System.out.println(" + Cart link navigates to cart page");
    }

    // ── TC-14: Contact modal opens ────────────────────────────────────────────

    @Test(priority = 5, description = "TC-14: Contact link opens the contact modal")
    public void testContactModalOpens() {
        driver.get(ConfigReader.getBaseUrl());
        NavBarPage navBar = new NavBarPage(driver);

        navBar.clickContact();

        Assert.assertTrue(navBar.isContactModalVisible(),
                " + DEFECT: Contact modal did not open.");
        System.out.println("[TC-14] + Contact modal opened");
    }

    @Test(priority = 6, description = "TC-15: Contact form send message triggers alert")
    public void testContactFormSendMessage() {
        driver.get(ConfigReader.getBaseUrl());
        NavBarPage navBar = new NavBarPage(driver);

        navBar.clickContact();

        navBar.fillContactForm(
                "test@qa.com",
                "QA Tester",
                "This is a test message from the automation suite."
        );

        navBar.clickSendMessage();

        // Site shows a browser alert on send
        WebDriverWait alertWait = new WebDriverWait(driver,
                Duration.ofSeconds(ConfigReader.getExplicitWait()));
        Alert alert = alertWait.until(ExpectedConditions.alertIsPresent());
        String alertText = alert.getText();
        alert.accept();

        System.out.println("[TC-15] Send message alert: " + alertText);

        Assert.assertNotNull(alertText,
                " + DEFECT: No alert appeared after sending contact message.");
        System.out.println(" + Send message alert appeared: " + alertText);
    }

    // ── TC-19: Logout works ───────────────────────────────────────────────────

    @Test(priority = 7, description = "TC-16: Logout hides welcome label and shows login/signup")
    public void testLogout() {
        driver.get(ConfigReader.getBaseUrl());
        NavBarPage navBar = new NavBarPage(driver);

        // Log in first
        navBar.clickLogin();
        new LoginPage(driver).login(
                ConfigReader.getUsername(),
                ConfigReader.getPassword()
        );

        // Verify logged in
        Assert.assertTrue(navBar.isWelcomeLabelVisible(),
                " + Could not log in — welcome label not shown.");
        System.out.println("[TC-16] Logged in: " + navBar.getWelcomeText());

        // Logout should be visible
        Assert.assertTrue(navBar.isLogoutLinkVisible(),
                " + DEFECT: Logout link not visible after login.");
        System.out.println(" + Logout link visible after login");

        // Now logout
        navBar.clickLogout();
        navBar.waitForLogout();

        System.out.println("[TC-16] Logged out.");

        // Welcome label should be gone
        Assert.assertTrue(navBar.isWelcomeLabelHidden(),
                " + DEFECT: Welcome label still visible after logout.");
        System.out.println(" + Welcome label hidden after logout");

        // Login and signup should be visible again
        Assert.assertTrue(navBar.isLoginLinkVisible(),
                " + DEFECT: Login link not visible after logout.");
        System.out.println(" + Login link visible after logout");

        Assert.assertTrue(navBar.isSignUpLinkVisible(),
                " + DEFECT: Sign up link not visible after logout.");
        System.out.println(" + Sign up link visible after logout");
    }

}
