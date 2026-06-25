package tests;

import base.BaseTest;
import config.ConfigReader;
import dataproviders.ProductDataProvider;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.ProductPage;

import java.util.List;

public class ProductTest extends BaseTest {

    @Test(
            priority = 1,
            description = "TC-05: Every product has name, price ($), and description",
            dataProvider = "categoryData",
            dataProviderClass = ProductDataProvider.class
    )
    public void testProductDetails(String category, String[] keywords) {
        driver.get(ConfigReader.getBaseUrl());

        HomePage homePage = new HomePage(driver);
        CategoryTest.selectCategory(homePage, category);

        List<String> productTitles = homePage.getProductTitles();
        System.out.println("\n[TC-05] Category: " + category);
        System.out.println("  Products to validate: " + productTitles);

        for (String title : productTitles) {
            driver.get(ConfigReader.getBaseUrl());
            HomePage hp = new HomePage(driver);
            CategoryTest.selectCategory(hp, category);
            hp.clickProductByTitle(title);

            ProductPage productPage = new ProductPage(driver);

            String name  = productPage.getProductName();
            String price = productPage.getProductPrice();
            String desc  = productPage.getProductDescription();

            System.out.println("\n  ── " + title);
            System.out.println("     Name  : " + name);
            System.out.println("     Price : " + price);
            System.out.println("     Desc  : " + desc);

            Assert.assertFalse(name.isEmpty(),
                    " + DEFECT: Product name not displayed on UI for: '" + title + "'");

            Assert.assertFalse(price.isEmpty(),
                    " + DEFECT: Product price not displayed on UI for: '" + title + "'");

            Assert.assertTrue(price.contains("$"),
                    " + DEFECT: Price is missing '$' currency symbol for: '"
                            + title + "' | Displayed price: '" + price + "'");

            Assert.assertFalse(desc.isEmpty(),
                    " + DEFECT: Product description not displayed on UI for: '" + title + "'");
        }
    }

}
