package pages;

import config.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.WaitHelper;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class HomePage {

    private final WebDriver driver;
    private final WaitHelper wait;

    private final By signUpLink   = By.id("signin2");
    private final By loginLink    = By.id("login2");
    private final By cartNavLink  = By.id("cartur");
    private final By navbarBrand  = By.className("navbar-brand");
    private final By welcomeLabel = By.id("nameofuser");

    private final By phonesCategory   = By.xpath("//a[normalize-space()='Phones']");
    private final By laptopsCategory  = By.xpath("//a[normalize-space()='Laptops']");
    private final By monitorsCategory = By.xpath("//a[normalize-space()='Monitors']");

    private final By productCardTitle = By.cssSelector(".card-block h4.card-title a");

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WaitHelper(driver);
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public String getNavbarBrandText() {
        return wait.waitForVisible(navbarBrand).getText().trim();
    }

    public String getWelcomeText() {
        return wait.waitForVisible(welcomeLabel).getText().trim();
    }

    public void clickSignUp() {
        wait.waitForClickable(signUpLink).click();
    }

    public void clickLogin() {
        wait.waitForClickable(loginLink).click();
    }

    public void clickCart() {
        wait.waitForClickable(cartNavLink).click();
    }

    public void selectPhonesCategory() {
        wait.waitForClickable(phonesCategory).click();
        waitForProductsToLoad();
    }

    public void selectLaptopsCategory() {
        wait.waitForClickable(laptopsCategory).click();
        waitForProductsToLoad();
    }

    public void selectMonitorsCategory() {
        wait.waitForClickable(monitorsCategory).click();
        waitForProductsToLoad();
    }

    private void waitForProductsToLoad() {
        new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWait()))
                .until(ExpectedConditions.visibilityOfElementLocated(productCardTitle));
    }

    public List<String> getProductTitles() {
        List<WebElement> elements = driver.findElements(productCardTitle);
        return elements.stream()
                .map(e -> e.getText().trim())
                .filter(t -> !t.isEmpty())
                .collect(Collectors.toList());
    }

    public void clickProductByTitle(String title) {
        By locator = By.xpath(
                "//a[@class='hrefch' and normalize-space()='" + title + "']"
        );
        wait.waitForClickable(locator).click();
    }

    public void clickFirstProduct() {
        wait.waitForClickable(productCardTitle).click();
    }

}
