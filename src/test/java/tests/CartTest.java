package tests;

import base.BaseTest;
import config.ConfigReader;
import dataproviders.CartDataProvider;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.*;

public class CartTest extends BaseTest {

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void doLogin() {
        driver.get(ConfigReader.getBaseUrl());
        new HomePage(driver).clickLogin();
        new LoginPage(driver).login(
                ConfigReader.getUsername(),
                ConfigReader.getPassword()
        );
    }

    private void addFirstPhoneToCart() {
        driver.get(ConfigReader.getBaseUrl());
        HomePage homePage = new HomePage(driver);
        homePage.selectPhonesCategory();
        homePage.clickFirstProduct();
        new ProductPage(driver).clickAddToCart();
    }

    private void goToCart() {
        driver.get(ConfigReader.getBaseUrl());
        new HomePage(driver).clickCart();
        new CartPage(driver).navigateToCart();
    }

    // ── TC-C01: Cart page loads, table headers present ────────────────────────

    @Test(priority = 1, description = "TC-C01: Cart page loads and table headers are present")
    public void testCartPageLoadsWithHeaders() {
        doLogin();
        goToCart();

        CartPage cartPage = new CartPage(driver);

        System.out.println("\n[TC-C01] Checking cart table headers...");

        cartPage.getTableHeaders().forEach(h ->
                System.out.println("  Found header: '" + h.getText().trim() + "'")
        );

        Assert.assertTrue(cartPage.areTableHeadersPresent(),
                " + DEFECT: Cart table headers missing.");

        Assert.assertTrue(cartPage.containsHeader("Pic"),
                " + DEFECT: Missing 'Pic' header.");
        System.out.println(" + Pic header present");

        Assert.assertTrue(cartPage.containsHeader("Title"),
                " + DEFECT: Missing 'Title' header.");
        System.out.println(" + Title header present");

        Assert.assertTrue(cartPage.containsHeader("Price"),
                " + DEFECT: Missing 'Price' header.");
        System.out.println(" + Price header present");

        Assert.assertTrue(cartPage.containsHeader("x"),
                " + DEFECT: Missing 'x' (delete) header.");
        System.out.println(" x (delete) header present");

        Assert.assertFalse(cartPage.containsHeader("x"),
                " + UI DEFECT: Delete column header is 'x' — should be 'Delete' or 'Remove'.");
    }

    // ── TC-C02: Add to cart triggers alert ───────────────────────────────────

    @Test(priority = 2, description = "TC-C02: Add to cart button triggers 'Product added.' alert")
    public void testAddToCartAlert() {
        doLogin();

        driver.get(ConfigReader.getBaseUrl());
        HomePage homePage = new HomePage(driver);
        homePage.selectPhonesCategory();
        homePage.clickFirstProduct();

        String alertText = new ProductPage(driver).clickAddToCart();

        System.out.println("[TC-C02] Add to cart alert: " + alertText);

        Assert.assertTrue(alertText.contains("Product added"),
                " + DEFECT: Expected 'Product added.' alert. Got: " + alertText);
        System.out.println(" + 'Product added.' alert appeared");
    }

    // ── TC-C03: First row has image, title, price, delete ────────────────────

    @Test(priority = 3, description = "TC-C03: Cart row displays image, title, price and delete option")
    public void testCartRowContents() {
        doLogin();
        addFirstPhoneToCart();
        goToCart();

        CartPage cartPage = new CartPage(driver);

        Assert.assertTrue(cartPage.cartHasItems(),
                " + DEFECT: Cart is empty after adding a product.");

        System.out.println("\n[TC-C03] Checking cart row contents...");

        Assert.assertTrue(cartPage.firstRowHasImage(),
                " + DEFECT: Cart row missing product image.");
        System.out.println(" + Product image displayed");

        Assert.assertTrue(cartPage.firstRowHasTitle(),
                " + DEFECT: Cart row missing product title.");
        System.out.println(" + Product title: " + cartPage.getFirstRowTitle());

        Assert.assertTrue(cartPage.firstRowPriceIsPresent(),
                " + DEFECT: Cart row missing product price.");
        System.out.println(" + Product price: " + cartPage.getFirstRowPrice());

        Assert.assertTrue(cartPage.firstRowHasDeleteOption(),
                " + DEFECT: Cart row missing delete option.");
        System.out.println(" + Delete option present");

        System.out.println(" + Cart total: $" + cartPage.getTotalAmount());
    }

    // ── TC-C04: Delete button removes item from cart ──────────────────────────

    @Test(priority = 4, description = "TC-C04: Delete button removes item from cart")
    public void testDeleteItemFromCart() {
        doLogin();
        addFirstPhoneToCart();
        goToCart();

        CartPage cartPage = new CartPage(driver);

        Assert.assertTrue(cartPage.cartHasItems(),
                " + Cart is empty — cannot test delete.");

        String itemTitle = cartPage.getFirstRowTitle();
        System.out.println("[TC-C04] Deleting item: " + itemTitle);

        cartPage.deleteFirstItem();

        boolean isEmpty = cartPage.waitForCartToBeEmpty();

        Assert.assertTrue(isEmpty,
                " + DEFECT: Item '" + itemTitle + "' was not removed from cart.");
        System.out.println(" + Item deleted — cart is now empty");
    }

    // ── TC-C05: Add item again for order flow ─────────────────────────────────

    @Test(priority = 5, description = "TC-C05: Add item to cart again after deletion")
    public void testAddItemAfterDeletion() {
        doLogin();
        addFirstPhoneToCart();
        goToCart();

        CartPage cartPage = new CartPage(driver);

        Assert.assertTrue(cartPage.cartHasItems(),
                " + DEFECT: Cart is empty after adding product again.");
        System.out.println("[TC-C05] + Item added to cart successfully after previous deletion");
        System.out.println(" + Item in cart: " + cartPage.getFirstRowTitle());
    }

    // ── TC-C06: Place Order modal opens with all fields ───────────────────────

    @Test(priority = 6, description = "TC-C06: Place Order button opens modal with all required fields")
    public void testPlaceOrderModalFields() {
        doLogin();
        addFirstPhoneToCart();
        goToCart();

        CartPage cartPage = new CartPage(driver);
        cartPage.clickPlaceOrder();

        Assert.assertTrue(cartPage.isPlaceOrderModalVisible(),
                " + DEFECT: Place Order modal did not open.");

        System.out.println("\n[TC-C06] Checking Place Order modal fields...");

        Assert.assertTrue(cartPage.isOrderNameFieldVisible(),
                " + DEFECT: Name field missing in Place Order modal.");
        System.out.println(" + Name field visible");

        Assert.assertTrue(cartPage.isOrderCountryFieldVisible(),
                " + DEFECT: Country field missing in Place Order modal.");
        System.out.println(" + Country field visible");

        Assert.assertTrue(cartPage.isOrderCityFieldVisible(),
                " + DEFECT: City field missing in Place Order modal.");
        System.out.println(" + City field visible");

        Assert.assertTrue(cartPage.isOrderCardFieldVisible(),
                " + DEFECT: Credit card field missing in Place Order modal.");
        System.out.println(" + Credit card field visible");

        Assert.assertTrue(cartPage.isOrderMonthFieldVisible(),
                " + DEFECT: Month field missing in Place Order modal.");
        System.out.println(" + Month field visible");

        Assert.assertTrue(cartPage.isOrderYearFieldVisible(),
                " + DEFECT: Year field missing in Place Order modal.");
        System.out.println(" + Year field visible");

        cartPage.closeOrderModal();
    }

    // ── TC-C07: Purchase without filling fields ───────────────────────────────

    @Test(priority = 7, description = "TC-C07: Purchase button without filling fields")
    public void testPurchaseWithoutFillingFields() {
        doLogin();
        addFirstPhoneToCart();
        goToCart();

        CartPage cartPage = new CartPage(driver);
        cartPage.clickPlaceOrder();

        Assert.assertTrue(cartPage.isPlaceOrderModalVisible(),
                " + Place Order modal did not open.");

        // Click Purchase without filling any fields
        cartPage.clickPurchaseWithoutWaiting();

        // DemoBlaze shows a browser alert when fields are empty
        try {
            org.openqa.selenium.Alert alert = new org.openqa.selenium.support.ui.WebDriverWait(
                    driver, java.time.Duration.ofSeconds(ConfigReader.getExplicitWait()))
                    .until(org.openqa.selenium.support.ui.ExpectedConditions.alertIsPresent());

            String alertMsg = alert.getText();
            alert.accept();

            System.out.println("[TC-C07] Alert on empty purchase: " + alertMsg);
            System.out.println(" + Site prevented purchase with empty fields");

        } catch (Exception e) {
            // Some browsers may not show alert — check modal is still open
            System.out.println("[TC-C07] No alert shown — checking if modal is still open...");
            Assert.assertTrue(cartPage.isPlaceOrderModalVisible(),
                    " + DEFECT: No validation shown for empty purchase fields.");
            System.out.println(" + Modal still open — form not submitted");
        }
    }

    // ── TC-C08: Purchase confirmation ─────────────────────────────────────────

    @Test(
            priority = 8,
            description = "TC-C08: Purchase confirmation and order details",
            dataProvider = "orderData",
            dataProviderClass = CartDataProvider.class
    )
    public void testPurchaseConfirmation(String name, String country, String city,
                                         String card, String month, String year) {
        doLogin();
        addFirstPhoneToCart();
        goToCart();

        CartPage cartPage = new CartPage(driver);
        cartPage.clickPlaceOrder();

        Assert.assertTrue(cartPage.isPlaceOrderModalVisible(),
                " + Place Order modal did not appear.");

        cartPage.fillOrderDetails(name, country, city, card, month, year);
        cartPage.clickPurchase();

        Assert.assertTrue(cartPage.isConfirmationModalVisible(),
                " + DEFECT: Purchase confirmation modal did not appear.");

        String confirmText = cartPage.getConfirmationText();

        System.out.println("\n[TC-C08] Confirmation text:\n" + confirmText);

        Assert.assertTrue(confirmText.contains("Amount"),
                " + DEFECT: Missing 'Amount' in confirmation.");
        System.out.println(" + Amount present");

        Assert.assertTrue(confirmText.contains("Card Number"),
                " + DEFECT: Missing 'Card Number' in confirmation.");
        System.out.println(" + Card Number present");

        Assert.assertTrue(confirmText.contains("Name"),
                " + DEFECT: Missing 'Name' in confirmation.");
        System.out.println(" + Name present");

        cartPage.clickConfirmOk();
        System.out.println(" + Confirmation dismissed with OK");
    }
}