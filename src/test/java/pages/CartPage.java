package pages;

import utils.WaitHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import config.ConfigReader;

import java.time.Duration;
import java.util.List;

public class CartPage {

    private final WebDriver driver;
    private final WaitHelper wait;

    private final By cartTableHeaders = By.cssSelector("thead th");
    private final By cartTable        = By.cssSelector("table.table");
    private final By cartRows          = By.cssSelector("#tbodyid tr");
    private final By firstCartRow      = By.cssSelector("#tbodyid tr:first-child");
    private final By cartBody          = By.cssSelector("#tbodyid");
    private final String rowImage      = "td:nth-child(1) img";
    private final String rowTitle      = "td:nth-child(2)";
    private final String rowPrice      = "td:nth-child(3)";
    private final String rowDelete     = "td:nth-child(4) a";

    private final By totalAmount       = By.id("totalp");
    private final By placeOrderBtn     = By.xpath("//button[normalize-space()='Place Order']");

    private final By placeOrderModal   = By.id("orderModal");
    private final By orderNameField    = By.id("name");
    private final By orderCountryField = By.id("country");
    private final By orderCityField    = By.id("city");
    private final By orderCardField    = By.id("card");
    private final By orderMonthField   = By.id("month");
    private final By orderYearField    = By.id("year");
    private final By orderTotalLabel   = By.id("totalm");
    private final By purchaseButton    = By.xpath("//button[normalize-space()='Purchase']");
    private final By modalCloseBtn     = By.xpath(
            "//div[@id='orderModal']//button[normalize-space()='Close']");

    private final By confirmationModal = By.cssSelector(".sweet-alert");
    private final By confirmationText  = By.cssSelector(".sweet-alert p");
    private final By confirmOkButton   = By.cssSelector(".sweet-alert button.confirm");

    private final By cartNavLink       = By.id("cartur");

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WaitHelper(driver);
    }

    // ── Navigation ────────────────────────────────────────────────────────────

    public void navigateToCart() {
        wait.waitForVisible(cartTable);
    }

    public void goToCartViaNavbar() {
        wait.waitForClickable(cartNavLink).click();
        wait.waitForVisible(cartTable);
    }

    // ── Table headers ─────────────────────────────────────────────────────────

    public List<WebElement> getTableHeaders() {
        wait.waitForVisible(cartTable);
        return driver.findElements(cartTableHeaders);
    }

    public boolean areTableHeadersPresent() {
        return getTableHeaders().size() >= 4;
    }

    public boolean containsHeader(String headerText) {
        return getTableHeaders().stream()
                .anyMatch(h -> h.getText().trim().equalsIgnoreCase(headerText));
    }

    // ── Cart rows ─────────────────────────────────────────────────────────────

    public boolean isCartEmpty() {
        // Wait briefly then check — empty cart has no rows
        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.visibilityOfElementLocated(cartBody));
        List<WebElement> rows = driver.findElements(cartRows);
        return rows.isEmpty();
    }

    public List<WebElement> getCartRows() {
        wait.waitForVisible(firstCartRow);
        return driver.findElements(cartRows);
    }

    public boolean cartHasItems() {
        try {
            wait.waitForVisible(firstCartRow);
            return !driver.findElements(cartRows).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean firstRowHasImage() {
        List<WebElement> rows = getCartRows();
        if (rows.isEmpty()) return false;
        List<WebElement> imgs = rows.get(0).findElements(By.cssSelector(rowImage));
        return !imgs.isEmpty() && imgs.get(0).isDisplayed();
    }

    public boolean firstRowHasTitle() {
        List<WebElement> rows = getCartRows();
        if (rows.isEmpty()) return false;
        WebElement titleCell = rows.get(0).findElement(By.cssSelector(rowTitle));
        return !titleCell.getText().trim().isEmpty();
    }

    public String getFirstRowTitle() {
        List<WebElement> rows = getCartRows();
        if (rows.isEmpty()) return "";
        return rows.get(0).findElement(By.cssSelector(rowTitle)).getText().trim();
    }

    public boolean firstRowPriceIsPresent() {
        List<WebElement> rows = getCartRows();
        if (rows.isEmpty()) return false;
        WebElement priceCell = rows.get(0).findElement(By.cssSelector(rowPrice));
        return !priceCell.getText().trim().isEmpty();
    }

    public String getFirstRowPrice() {
        List<WebElement> rows = getCartRows();
        if (rows.isEmpty()) return "";
        return rows.get(0).findElement(By.cssSelector(rowPrice)).getText().trim();
    }

    public boolean firstRowHasDeleteOption() {
        List<WebElement> rows = getCartRows();
        if (rows.isEmpty()) return false;
        return !rows.get(0).findElements(By.cssSelector(rowDelete)).isEmpty();
    }

    // ── Delete ────────────────────────────────────────────────────────────────

    public void deleteFirstItem() {
        List<WebElement> rows = getCartRows();
        if (!rows.isEmpty()) {
            WebElement deleteLink = rows.get(0).findElement(By.cssSelector(rowDelete));
            deleteLink.click();

            // Wait for row to disappear after deletion
            new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWait()))
                    .until(ExpectedConditions.stalenessOf(rows.get(0)));
        }
    }

    public boolean waitForCartToBeEmpty() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWait()))
                    .until(driver -> driver.findElements(cartRows).isEmpty());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // ── Total ─────────────────────────────────────────────────────────────────

    public String getTotalAmount() {
        return wait.waitForVisible(totalAmount).getText().trim();
    }

    // ── Place Order ───────────────────────────────────────────────────────────

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

    public boolean isOrderNameFieldVisible() {
        return isVisible(orderNameField);
    }

    public boolean isOrderCountryFieldVisible() {
        return isVisible(orderCountryField);
    }

    public boolean isOrderCityFieldVisible() {
        return isVisible(orderCityField);
    }

    public boolean isOrderCardFieldVisible() {
        return isVisible(orderCardField);
    }

    public boolean isOrderMonthFieldVisible() {
        return isVisible(orderMonthField);
    }

    public boolean isOrderYearFieldVisible() {
        return isVisible(orderYearField);
    }

    public String getOrderModalTotalText() {
        return wait.waitForVisible(orderTotalLabel).getText().trim();
    }

    public void fillOrderDetails(String name, String country, String city,
                                 String card, String month, String year) {
        fillField(orderNameField,    name);
        fillField(orderCountryField, country);
        fillField(orderCityField,    city);
        fillField(orderCardField,    card);
        fillField(orderMonthField,   month);
        fillField(orderYearField,    year);
    }

    public void closeOrderModal() {
        wait.waitForClickable(modalCloseBtn).click();
        wait.waitForElementToDisappear(placeOrderModal);
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

    public void clickPurchaseWithoutWaiting() {
        wait.waitForClickable(purchaseButton).click();
    }

    // ── Confirmation ──────────────────────────────────────────────────────────

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

    // ── Private helper ────────────────────────────────────────────────────────

    private boolean isVisible(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}