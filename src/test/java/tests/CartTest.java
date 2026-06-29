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

    // ── TC-06-1: Cart page loads, table headers present ────────────────────────

    @Test(priority = 1, description = "TC-06-1: Cart page loads and table headers are present")
    public void testCartPageLoadsWithHeaders() {
        doLogin();
        goToCart();

        CartPage cartPage = new CartPage(driver);

        System.out.println("\n[TC-06-1] Checking cart table headers...");

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

    // ── TC-06-2: Place order with empty cart ───────────────────────────────────

    @Test(
            priority = 2,
            description = "TC-06-2: Place Order with empty cart — site should prevent this",
            dataProvider = "orderData",
            dataProviderClass = CartDataProvider.class
    )
    public void testPlaceOrderWithEmptyCart(String name, String country, String city,
                                            String card, String month, String year) {

        goToCart();

        CartPage cartPage = new CartPage(driver);

        // Click Place Order on empty cart
        cartPage.clickPlaceOrder();

        Assert.assertTrue(cartPage.isPlaceOrderModalVisible(),
                "Place Order modal did not open.");

        cartPage.fillOrderDetails(name, country, city, card, month, year);
        System.out.println("[TC-06-2] Order details filled on empty cart.");

        cartPage.clickPurchase();

        boolean confirmationShown = cartPage.isConfirmationModalVisible();

        if (confirmationShown) {
            String confirmText = cartPage.getConfirmationText();
            System.out.println("[TC-06-2] Confirmation text: " + confirmText);
            cartPage.clickConfirmOk();
        }

        // This should FAIL — site should not allow purchase with empty cart
        Assert.assertFalse(confirmationShown,
                " + DEFECT: Site allowed purchase with an empty cart. "
                        + "Order should be rejected when no items are present."
        );
    }

    // ── TC-06-3: Add to cart triggers alert ───────────────────────────────────

    @Test(priority = 3, description = "TC-06-3: Add to cart button triggers 'Product added.' alert")
    public void testAddToCartAlert() {

        doLogin();

        driver.get(ConfigReader.getBaseUrl());
        HomePage homePage = new HomePage(driver);
        homePage.selectPhonesCategory();
        homePage.clickFirstProduct();

        String alertText = new ProductPage(driver).clickAddToCart();

        System.out.println("[TC-06-3] Add to cart alert: " + alertText);

        Assert.assertTrue(alertText.contains("Product added"),
                " + DEFECT: Expected 'Product added.' alert. Got: " + alertText);
        System.out.println(" + 'Product added.' alert appeared");
    }

    // ── TC-06-4: First row has image, title, price, delete ────────────────────

    @Test(priority = 4, description = "TC-06-4: Cart row displays image, title, price and delete option")
    public void testCartRowContents() {
        doLogin();
        goToCart();

        CartPage cartPage = new CartPage(driver);

        Assert.assertTrue(cartPage.cartHasItems(),
                " + DEFECT: Cart is empty after adding a product.");

        System.out.println("\n[TC-06-4] Checking cart row contents...");

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

    // ── TC-06-5: Delete button removes item from cart ──────────────────────────

    @Test(priority = 5, description = "TC-06-5: Delete button removes item from cart")
    public void testDeleteItemFromCart() {
        doLogin();
        goToCart();

        CartPage cartPage = new CartPage(driver);

        Assert.assertTrue(cartPage.cartHasItems(),
                " + Cart is empty — cannot test delete.");

        String itemTitle = cartPage.getFirstRowTitle();
        System.out.println("[TC-06-5] Deleting item: " + itemTitle);

        cartPage.deleteFirstItem();

        boolean isEmpty = cartPage.waitForCartToBeEmpty();

        Assert.assertTrue(isEmpty,
                " + DEFECT: Item '" + itemTitle + "' was not removed from cart.");
        System.out.println(" + Item deleted — cart is now empty");
    }

    // ── TC-06-6: Add item again for order flow ─────────────────────────────────

    @Test(priority = 6, description = "TC-06-6: Add item to cart again after deletion")
    public void testAddItemAfterDeletion() {
        doLogin();
        addFirstPhoneToCart();
        goToCart();

        CartPage cartPage = new CartPage(driver);

        Assert.assertTrue(cartPage.cartHasItems(),
                " + DEFECT: Cart is empty after adding product again.");
        System.out.println("[TC-06-6] + Item added to cart successfully after previous deletion");
        System.out.println(" + Item in cart: " + cartPage.getFirstRowTitle());
    }

    // ── TC-C06: Place Order modal opens with all fields ───────────────────────

    @Test(priority = 7, description = "TC-06-7: Place Order button opens modal with required fields")
    public void testPlaceOrderModalFields() {
        doLogin();
        addFirstPhoneToCart();
        goToCart();

        CartPage cartPage = new CartPage(driver);
        cartPage.clickPlaceOrder();

        Assert.assertTrue(cartPage.isPlaceOrderModalVisible(),
                " + DEFECT: Place Order modal did not open.");

        System.out.println("\n[TC-06-7] Checking Place Order modal fields...");

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

    // ── TC-06-8: Purchase without filling fields ───────────────────────────────

    @Test(priority = 8, description = "TC-06-8: Purchase button without filling fields")
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

            System.out.println("[TC-06-8] Alert on empty purchase: " + alertMsg);
            System.out.println(" + Site prevented purchase with empty fields");

        } catch (Exception e) {
            // Some browsers may not show alert — check modal is still open
            System.out.println("[TC-06-8] No alert shown — checking if modal is still open...");
            Assert.assertTrue(cartPage.isPlaceOrderModalVisible(),
                    " + DEFECT: No validation shown for empty purchase fields.");
            System.out.println(" + Modal still open — form not submitted");
        }
    }

    // ── TC-06-9: Purchase confirmation ─────────────────────────────────────────

    @Test(
            priority = 9,
            description = "TC-06-9: Purchase confirmation and order details",
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

        System.out.println("\n[TC-06-9] Confirmation text:\n" + confirmText);

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