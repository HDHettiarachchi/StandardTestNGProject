package pages;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import config.ConfigReader;
import utils.WaitHelper;

import java.time.Duration;

public class ProductPage {

    private final WebDriver driver;
    private final WaitHelper wait;
    private final WebDriverWait explicitWait;

    private final By productName        = By.cssSelector(".name");
    private final By productPrice       = By.cssSelector(".price-container");
    private final By productDescription = By.cssSelector("#more-information p");
    private final By addToCartButton    = By.xpath("//a[normalize-space()='Add to cart']");

    public ProductPage(WebDriver driver) {
        this.driver       = driver;
        this.wait         = new WaitHelper(driver);
        this.explicitWait = new WebDriverWait(driver,
                Duration.ofSeconds(ConfigReader.getExplicitWait()));
    }

    // Wait for the full product page to load before doing anything
    public void waitForPageToLoad() {
        explicitWait.until(ExpectedConditions.visibilityOfElementLocated(productName));
        explicitWait.until(ExpectedConditions.visibilityOfElementLocated(productPrice));
        explicitWait.until(ExpectedConditions.visibilityOfElementLocated(productDescription));
    }

    public String getProductName() {
        explicitWait.until(ExpectedConditions.visibilityOfElementLocated(productName));
        return driver.findElement(productName).getText().trim();
    }

    public String getProductPrice() {
        explicitWait.until(ExpectedConditions.visibilityOfElementLocated(productPrice));
        return driver.findElement(productPrice).getText().trim();
    }

    public String getProductDescription() {
        explicitWait.until(ExpectedConditions.visibilityOfElementLocated(productDescription));
        return driver.findElement(productDescription).getText().trim();
    }

    public String clickAddToCart() {
        explicitWait.until(ExpectedConditions.elementToBeClickable(addToCartButton));
        driver.findElement(addToCartButton).click();
        Alert alert = wait.waitForAlert();
        String message = alert.getText();
        alert.accept();
        return message;
    }
}