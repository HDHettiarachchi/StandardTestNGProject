package pages;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.WaitHelper;

public class ProductPage {

    private final WebDriver driver;
    private final WaitHelper wait;

    private final By productName        = By.cssSelector(".name");
    private final By productPrice       = By.cssSelector(".price-container");
    private final By productDescription = By.cssSelector("#more-information p");
    private final By addToCartButton    = By.xpath("//a[normalize-space()='Add to cart']");

    public ProductPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WaitHelper(driver);
    }

    public String getProductName() {
        return wait.waitForVisible(productName).getText().trim();
    }

    public String getProductPrice() {
        return wait.waitForVisible(productPrice).getText().trim();
    }

    public String getProductDescription() {
        return wait.waitForVisible(productDescription).getText().trim();
    }

    public boolean isPriceWithCurrency() {
        return getProductPrice().contains("$");
    }

    public boolean hasValidName() {
        return !getProductName().isEmpty();
    }

    public boolean hasValidDescription() {
        return !getProductDescription().isEmpty();
    }

    public String clickAddToCart() {
        wait.waitForClickable(addToCartButton).click();
        Alert alert = wait.waitForAlert();
        String message = alert.getText();
        alert.accept();
        return message;
    }

}
