package dataproviders;

import config.ConfigReader;
import org.testng.annotations.DataProvider;

public class LoginDataProvider {

    @DataProvider(name = "validLoginData")
    public static Object[][] validLoginData() {
        return new Object[][] {
                { ConfigReader.getUsername(), ConfigReader.getPassword() }
        };
    }

    @DataProvider(name = "pageTitleData")
    public static Object[][] pageTitleData() {
        return new Object[][] {
                { "STORE", "PRODUCT STORE" }
        };
    }

}
