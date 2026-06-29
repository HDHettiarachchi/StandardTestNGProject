package tests;

import base.BaseTest;
import config.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.ProductPage;

public class ProductTest extends BaseTest {

    // Expected first product titles per category
    private static final String EXPECTED_PHONES_TITLE   = "Samsung galaxy s6";
    private static final String EXPECTED_LAPTOPS_TITLE  = "Sony vaio i5";
    private static final String EXPECTED_MONITORS_TITLE = "Apple monitor 24";

    @Test(priority = 1, description = "TC-05a: First product in Phones category")
    public void testPhonesProduct() {
        driver.get(ConfigReader.getBaseUrl());

        HomePage homePage = new HomePage(driver);
        homePage.selectPhonesCategory();
        homePage.clickFirstProduct();

        ProductPage productPage = new ProductPage(driver);
        productPage.waitForPageToLoad();

        String name  = productPage.getProductName();
        String price = productPage.getProductPrice();
        String desc  = productPage.getProductDescription();

        System.out.println("\n[TC-05a] Phones — First Product");
        System.out.println("  Expected Title : " + EXPECTED_PHONES_TITLE);
        System.out.println("  Actual Title   : " + name);
        System.out.println("  Price          : " + price);
        System.out.println("  Desc           : " + desc);

        Assert.assertEquals(name, EXPECTED_PHONES_TITLE,
                " + DEFECT: Product title mismatch. "
                        + "Expected: '" + EXPECTED_PHONES_TITLE + "' "
                        + "| Got: '" + name + "'");
        Assert.assertFalse(price.isEmpty(), " + Product price is empty.");
        Assert.assertTrue(price.contains("$"), " + Price missing '$'.");
        Assert.assertFalse(desc.isEmpty(),  " + Product description is empty.");
    }

    @Test(priority = 2, description = "TC-05b: First product in Laptops category")
    public void testLaptopsProduct() {
        driver.get(ConfigReader.getBaseUrl());

        HomePage homePage = new HomePage(driver);
        homePage.selectLaptopsCategory();
        homePage.clickFirstProduct();

        ProductPage productPage = new ProductPage(driver);
        productPage.waitForPageToLoad();

        String name  = productPage.getProductName();
        String price = productPage.getProductPrice();
        String desc  = productPage.getProductDescription();

        System.out.println("\n[TC-05b] Laptops — First Product");
        System.out.println("  Expected Title : " + EXPECTED_LAPTOPS_TITLE);
        System.out.println("  Actual Title   : " + name);
        System.out.println("  Price          : " + price);
        System.out.println("  Desc           : " + desc);

        Assert.assertEquals(name, EXPECTED_LAPTOPS_TITLE,
                " + DEFECT: Product title mismatch. "
                        + "Expected: '" + EXPECTED_LAPTOPS_TITLE + "' "
                        + "| Got: '" + name + "'");
        Assert.assertFalse(price.isEmpty(), " + Product price is empty.");
        Assert.assertTrue(price.contains("$"), " + Price missing '$'.");
        Assert.assertFalse(desc.isEmpty(),  " + Product description is empty.");
    }

    @Test(priority = 3, description = "TC-05c: First product in Monitors category")
    public void testMonitorsProduct() {
        driver.get(ConfigReader.getBaseUrl());

        HomePage homePage = new HomePage(driver);
        homePage.selectMonitorsCategory();
        homePage.clickFirstProduct();

        ProductPage productPage = new ProductPage(driver);
        productPage.waitForPageToLoad();

        String name  = productPage.getProductName();
        String price = productPage.getProductPrice();
        String desc  = productPage.getProductDescription();

        System.out.println("\n[TC-05c] Monitors — First Product");
        System.out.println("  Expected Title : " + EXPECTED_MONITORS_TITLE);
        System.out.println("  Actual Title   : " + name);
        System.out.println("  Price          : " + price);
        System.out.println("  Desc           : " + desc);

        Assert.assertEquals(name, EXPECTED_MONITORS_TITLE,
                " + DEFECT: Product title mismatch. "
                        + "Expected: '" + EXPECTED_MONITORS_TITLE + "' "
                        + "| Got: '" + name + "'");
        Assert.assertFalse(price.isEmpty(), " + Product price is empty.");
        Assert.assertTrue(price.contains("$"), " + Price missing '$'.");
        Assert.assertFalse(desc.isEmpty(),  " + Product description is empty.");
    }
}