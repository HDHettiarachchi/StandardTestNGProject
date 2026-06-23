package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.WaitHelper;

public class LoginPage {

    private final WebDriver driver;
    private final WaitHelper wait;

    private final By modal         = By.id("logInModal");
    private final By usernameField = By.id("loginusername");
    private final By passwordField = By.id("loginpassword");
    private final By loginButton   = By.xpath("//button[normalize-space()='Log in']");
    private final By welcomeLabel  = By.id("nameofuser");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WaitHelper(driver);
    }

    public void waitForModal() {
        wait.waitForVisible(modal);
    }

    public void enterUsername(String username) {
        WebElement field = wait.waitForVisible(usernameField);
        field.clear();
        field.sendKeys(username);
    }

    public void enterPassword(String password) {
        WebElement field = wait.waitForVisible(passwordField);
        field.clear();
        field.sendKeys(password);
    }

    public void clickLogin() {
        wait.waitForClickable(loginButton).click();
    }

    public String login(String username, String password) {
        waitForModal();
        enterUsername(username);
        enterPassword(password);
        clickLogin();
        return wait.waitForNonEmptyText(welcomeLabel).getText().trim();
    }

}

