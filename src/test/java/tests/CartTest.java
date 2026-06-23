package tests;

import base.BaseTest;
import config.ConfigReader;
import dataproviders.CartDataProvider;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.HomePage;
import pages.LoginPage;
import pages.ProductPage;

public class CartTest extends BaseTest {

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void doLogin() {
        driver.get(ConfigReader.getBaseUrl());
        HomePage homePage = new HomePage(driver);
        homePage.clickLogin();
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

    // ── TC-06 Add to Cart Popup ───────────────────────────────────────────────

    @Test(priority = 1, description = "TC-06: Add to cart triggers 'Product added.' alert")
    public void testAddToCartPopup() {
        doLogin();

        driver.get(ConfigReader.getBaseUrl());
        HomePage homePage = new HomePage(driver);
        homePage.selectPhonesCategory();
        homePage.clickFirstProduct();

        String alertText = new ProductPage(driver).clickAddToCart();

        System.out.println("[TC-06] Add to cart alert: " + alertText);

        Assert.assertTrue(alertText.contains("Product added"),
                "Expected 'Product added.' alert. Got: " + alertText);
    }

    // ── TC-07 Cart Validation ─────────────────────────────────────────────────

    @Test(priority = 2, description = "TC-07: Cart has headers, image, price, and delete option")
    public void testCartValidation() {
        doLogin();
        addFirstPhoneToCart();
        goToCart();

        CartPage cartPage = new CartPage(driver);

        Assert.assertTrue(cartPage.areTableHeadersPresent(), "Cart table headers missing.");
        Assert.assertTrue(cartPage.containsHeader("Pic"),    "Missing 'Pic' header.");
        Assert.assertTrue(cartPage.containsHeader("Title"),  "Missing 'Title' header.");
        Assert.assertTrue(cartPage.containsHeader("Price"),  "Missing 'Price' header.");
        Assert.assertTrue(cartPage.containsHeader("x"),      "Missing 'x' header.");

        System.out.println("[TC-07] All headers verified.");

        Assert.assertTrue(cartPage.cartHasItems(),            "Cart is empty.");
        Assert.assertTrue(cartPage.firstRowHasImage(),        "Cart row missing image.");
        Assert.assertTrue(cartPage.firstRowPriceIsPresent(),  "Cart row price empty.");
        Assert.assertTrue(cartPage.firstRowHasDeleteOption(), "Cart row missing delete.");

        System.out.println("[TC-07] Cart total: $" + cartPage.getTotalAmount());
    }

    // ── TC-08 Place Order Popup ───────────────────────────────────────────────

    @Test(
            priority = 3,
            description = "TC-08: Place Order button opens order form modal",
            dataProvider = "orderData",
            dataProviderClass = CartDataProvider.class
    )
    public void testPlaceOrderPopup(String name, String country, String city,
                                    String card, String month, String year) {
        doLogin();
        addFirstPhoneToCart();
        goToCart();

        CartPage cartPage = new CartPage(driver);
        cartPage.clickPlaceOrder();

        Assert.assertTrue(cartPage.isPlaceOrderModalVisible(),
                "Place Order modal did not appear.");

        cartPage.fillOrderDetails(name, country, city, card, month, year);

        System.out.println("[TC-08] Order form filled successfully.");
    }

    // ── TC-09 Purchase Confirmation ───────────────────────────────────────────

    @Test(
            priority = 4,
            description = "TC-09: Purchase popup shows order details and Thank You message",
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
        cartPage.fillOrderDetails(name, country, city, card, month, year);
        cartPage.clickPurchase();

        Assert.assertTrue(cartPage.isConfirmationModalVisible(),
                "Purchase confirmation modal did not appear.");

        String confirmText = cartPage.getConfirmationText();
        System.out.println("[TC-09] Confirmation text:\n" + confirmText);

        Assert.assertTrue(confirmText.contains("Amount"),
                "Missing 'Amount' in confirmation.");
        Assert.assertTrue(confirmText.contains("Card Number"),
                "Missing 'Card Number' in confirmation.");
        Assert.assertTrue(confirmText.contains("Name"),
                "Missing 'Name' in confirmation.");

        cartPage.clickConfirmOk();
        System.out.println("[TC-09] Confirmation dismissed with OK.");
    }

}
