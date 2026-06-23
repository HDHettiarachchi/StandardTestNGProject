package pages;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.WaitHelper;

public class SignUpPage {

    private final WebDriver driver;
    private final WaitHelper wait;

    private final By modal         = By.id("signInModal");
    private final By usernameField = By.id("sign-username");
    private final By passwordField = By.id("sign-password");
    private final By signUpButton  = By.xpath("//button[normalize-space()='Sign up']");

    public SignUpPage(WebDriver driver) {
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

    public void clickSignUp() {
        wait.waitForClickable(signUpButton).click();
    }

    public String handleAlert() {
        Alert alert = wait.waitForAlert();
        String message = alert.getText();
        alert.accept();
        return message;
    }

    public String signUp(String username, String password) {
        waitForModal();
        enterUsername(username);
        enterPassword(password);
        clickSignUp();
        return handleAlert();
    }

}
