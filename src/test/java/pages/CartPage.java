package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.WaitHelper;

import java.util.List;

public class CartPage {

    private final WebDriver driver;
    private final WaitHelper wait;

    private final By cartTableHeaders = By.xpath("//table//th");
    private final By cartRows = By.cssSelector("#tbodyid tr");
    private final By firstCartRow = By.cssSelector("#tbodyid tr:first-child");
    private final By cartBody = By.cssSelector("#tbodyid");
    private final String rowImage = "td:nth-child(1) img";
    private final String rowPrice = "td:nth-child(3)";
    private final String rowDelete = "td:nth-child(4) a";

    private final By totalAmount = By.id("totalp");
    private final By placeOrderBtn = By.xpath("//button[normalize-space()='Place Order']");

    private final By placeOrderModal = By.id("orderModal");
    private final By orderNameField = By.id("name");
    private final By orderCountryField = By.id("country");
    private final By orderCityField = By.id("city");
    private final By orderCardField = By.id("card");
    private final By orderMonthField = By.id("month");
    private final By orderYearField = By.id("year");
    private final By purchaseButton = By.xpath("//button[normalize-space()='Purchase']");

    private final By confirmationModal = By.cssSelector(".sweet-alert");
    private final By confirmationText = By.cssSelector(".sweet-alert p");
    private final By confirmOkButton = By.cssSelector(".sweet-alert button.confirm");

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WaitHelper(driver);
    }

    public void navigateToCart() {
        wait.waitForVisible(cartBody);
    }

    public List<WebElement> getTableHeaders() {
        return driver.findElements(cartTableHeaders);
    }

    public boolean areTableHeadersPresent() {
        return getTableHeaders().size() >= 4;
    }

    public boolean containsHeader(String headerText) {
        return getTableHeaders().stream()
                .anyMatch(h -> h.getText().trim().equalsIgnoreCase(headerText));
    }

    public List<WebElement> getCartRows() {
        wait.waitForVisible(firstCartRow);
        return driver.findElements(cartRows);
    }

    public boolean cartHasItems() {
        return !getCartRows().isEmpty();
    }

    public boolean firstRowHasImage() {
        List<WebElement> rows = getCartRows();
        if (rows.isEmpty()) return false;
        List<WebElement> imgs = rows.get(0).findElements(By.cssSelector(rowImage));
        return !imgs.isEmpty() && imgs.get(0).isDisplayed();
    }

    public boolean firstRowPriceIsPresent() {
        List<WebElement> rows = getCartRows();
        if (rows.isEmpty()) return false;
        WebElement priceCell = rows.get(0).findElement(By.cssSelector(rowPrice));
        return !priceCell.getText().trim().isEmpty();
    }

    public boolean firstRowHasDeleteOption() {
        List<WebElement> rows = getCartRows();
        if (rows.isEmpty()) return false;
        return !rows.get(0).findElements(By.cssSelector(rowDelete)).isEmpty();
    }

    public String getTotalAmount() {
        return wait.waitForVisible(totalAmount).getText().trim();
    }

    public void clickPlaceOrder() {
        wait.waitForClickable(placeOrderBtn).click();
        wait.waitForVisible(placeOrderModal);
    }

    public boolean isPlaceOrderModalVisible() {
        try {
            return driver.findElement(placeOrderModal).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void fillOrderDetails(String name, String country, String city,
                                 String card, String month, String year) {
        fillField(orderNameField, name);
        fillField(orderCountryField, country);
        fillField(orderCityField, city);
        fillField(orderCardField, card);
        fillField(orderMonthField, month);
        fillField(orderYearField, year);
    }

    private void fillField(By locator, String value) {
        WebElement field = wait.waitForVisible(locator);
        field.clear();
        field.sendKeys(value);
    }

    public void clickPurchase() {
        wait.waitForClickable(purchaseButton).click();
        wait.waitForVisible(confirmationModal);
    }

    public boolean isConfirmationModalVisible() {
        try {
            return driver.findElement(confirmationModal).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getConfirmationText() {
        return wait.waitForVisible(confirmationText).getText().trim();
    }

    public void clickConfirmOk() {
        wait.waitForClickable(confirmOkButton).click();
        wait.waitForElementToDisappear(confirmationModal);


    }
}