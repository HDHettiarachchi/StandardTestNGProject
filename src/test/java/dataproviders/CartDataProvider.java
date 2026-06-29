package dataproviders;

import config.ConfigReader;
import org.testng.annotations.DataProvider;

public class CartDataProvider {

    @DataProvider(name = "orderData")
    public static Object[][] orderData() {
        return new Object[][] {
                { "HasikalaSQA", "Sri Lanka", "Matara", "0123456789", "6", "2026" }
        };
    }

    // Login credentials reused inside CartTest helpers
    @DataProvider(name = "cartLoginData")
    public static Object[][] cartLoginData() {
        return new Object[][] {
                { ConfigReader.getUsername(), ConfigReader.getPassword() }
        };
    }

}
