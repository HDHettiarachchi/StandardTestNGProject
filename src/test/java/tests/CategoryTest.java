package tests;

import base.BaseTest;
import config.ConfigReader;
import dataproviders.CategoryDataProvider;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.HomePage;

import java.util.List;

public class CategoryTest extends BaseTest {

    private WebDriver driver;

    @BeforeClass
    public void setUp() {
        driver = BaseTest.createDriver();
        driver.get(ConfigReader.getBaseUrl());
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test(
            priority = 1,
            description = "TC-04: Print all products found under each category",
            dataProvider = "categoryData",
            dataProviderClass = CategoryDataProvider.class
    )
    public void testCategory(String category) {
        driver.get(ConfigReader.getBaseUrl());

        HomePage homePage = new HomePage(driver);
        selectCategory(homePage, category);

        List<String> titles = homePage.getProductTitles();

        System.out.println("\n══════════════════════════════════════");
        System.out.println("  Category    : " + category);
        System.out.println("  Items found : " + titles.size());
        System.out.println("──────────────────────────────────────");
        for (int i = 0; i < titles.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + titles.get(i));
        }
        System.out.println("══════════════════════════════════════\n");

        Assert.assertFalse(titles.isEmpty(),
                " + DEFECT: Category '\" + category + \"' loaded 0 products on the UI. \"\n" +
                        "            + \"Either the category filter failed or AJAX did not complete in time.");
    }

    public static void selectCategory(HomePage homePage, String category) {
        switch (category) {
            case "Phones":   homePage.selectPhonesCategory();   break;
            case "Laptops":  homePage.selectLaptopsCategory();  break;
            case "Monitors": homePage.selectMonitorsCategory(); break;
            default: throw new IllegalArgumentException("Unknown category: " + category);
        }
    }

}
