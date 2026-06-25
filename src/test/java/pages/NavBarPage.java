package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.WaitHelper;

public class NavBarPage {

    private final WebDriver driver;
    private final WaitHelper wait;

    // Navbar links
    private final By navbarBrand    = By.className("navbar-brand");
    private final By homeLink       = By.xpath("//a[normalize-space()='Home']");
    private final By contactLink    = By.xpath("//a[normalize-space()='Contact']");
    private final By aboutUsLink    = By.xpath("//a[normalize-space()='About us']");
    private final By cartLink       = By.id("cartur");
    private final By loginLink      = By.id("login2");
    private final By signUpLink     = By.id("signin2");
    private final By logoutLink     = By.id("logout2");
    private final By welcomeLabel   = By.id("nameofuser");

    // Contact modal
    private final By contactModal      = By.id("exampleModal");
    private final By contactEmail      = By.id("recipient-email");
    private final By contactName       = By.id("recipient-name");
    private final By contactMessage    = By.id("message-text");
    private final By contactSendBtn    = By.xpath("//button[normalize-space()='Send message']");
    private final By contactCloseBtn   = By.xpath(
            "//div[@id='exampleModal']//button[normalize-space()='Close']");

    // About Us modal
    private final By aboutUsModal      = By.id("videoModal");
    private final By aboutUsVideo      = By.id("example-video");
    private final By aboutUsCloseBtn   = By.xpath(
            "//div[@id='videoModal']//button[normalize-space()='Close']");

    public NavBarPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WaitHelper(driver);
    }

    // ── Navbar visibility ─────────────────────────────────────────────────────

    public boolean isHomeLinkVisible() {
        return isVisible(homeLink);
    }

    public boolean isContactLinkVisible() {
        return isVisible(contactLink);
    }

    public boolean isAboutUsLinkVisible() {
        return isVisible(aboutUsLink);
    }

    public boolean isCartLinkVisible() {
        return isVisible(cartLink);
    }

    public boolean isLoginLinkVisible() {
        return isVisible(loginLink);
    }

    public boolean isSignUpLinkVisible() {
        return isVisible(signUpLink);
    }

    public boolean isLogoutLinkVisible() {
        return isVisible(logoutLink);
    }

    public boolean isWelcomeLabelVisible() {
        return isVisible(welcomeLabel);
    }

    public boolean isWelcomeLabelHidden() {
        try {
            WebElement el = driver.findElement(welcomeLabel);
            return !el.isDisplayed() || el.getText().trim().isEmpty();
        } catch (Exception e) {
            return true;
        }
    }

    // ── Navbar actions ────────────────────────────────────────────────────────

    public void clickNavbarBrand() {
        wait.waitForClickable(navbarBrand).click();
    }

    public void clickHome() {
        wait.waitForClickable(homeLink).click();
    }

    public void clickContact() {
        wait.waitForClickable(contactLink).click();
    }

    public void clickAboutUs() {
        wait.waitForClickable(aboutUsLink).click();
    }

    public void clickCart() {
        wait.waitForClickable(cartLink).click();
    }

    public void clickLogin() {
        wait.waitForClickable(loginLink).click();
    }

    public void clickSignUp() {
        wait.waitForClickable(signUpLink).click();
    }

    public void clickLogout() {
        wait.waitForClickable(logoutLink).click();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getNavbarBrandText() {
        return wait.waitForVisible(navbarBrand).getText().trim();
    }

    // ── Contact modal ─────────────────────────────────────────────────────────

    public boolean isContactModalVisible() {
        return isVisible(contactModal);
    }

    public boolean isContactEmailFieldVisible() {
        return isVisible(contactEmail);
    }

    public void fillContactForm(String email, String name, String message) {
        WebElement emailField = wait.waitForVisible(contactEmail);
        emailField.clear();
        emailField.sendKeys(email);

        WebElement nameField = wait.waitForVisible(contactName);
        nameField.clear();
        nameField.sendKeys(name);

        WebElement msgField = wait.waitForVisible(contactMessage);
        msgField.clear();
        msgField.sendKeys(message);
    }

    public void clickSendMessage() {
        wait.waitForClickable(contactSendBtn).click();
    }

    public void closeContactModal() {
        wait.waitForClickable(contactCloseBtn).click();
    }

    // ── Login/Logout helpers ──────────────────────────────────────────────────

    public String getWelcomeText() {
        return wait.waitForVisible(welcomeLabel).getText().trim();
    }

    public void waitForWelcomeLabel() {
        wait.waitForNonEmptyText(welcomeLabel);
    }

    public void waitForLogout() {
        wait.waitForElementToDisappear(welcomeLabel);
    }

    // ── Private helper ────────────────────────────────────────────────────────

    private boolean isVisible(By locator) {
        try {
            return wait.waitForVisible(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

}
