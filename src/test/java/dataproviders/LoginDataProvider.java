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

    @DataProvider(name = "negativeLoginData")
    public static Object[][] negativeLoginData() {
        return new Object[][]{
                {"HasikalaSQATest2", "Pwd12345!",
                        "User does not exist.", "TC-02-N1: Unregistered User"},
                { ConfigReader.getUsername(), "wrongpassword",
                        "Wrong password.",      "TC-02-N2: Wrong password"             },
                { "",                         "",
                        "Please fill out",      "TC-02-N3: Both fields empty"          },
                { "",                         ConfigReader.getPassword(),
                        "Please fill out",      "TC-02-N4: Empty username"             },
                { ConfigReader.getUsername(), "",
                        "Please fill out",      "TC-02-N5: Empty password"             }
        };
    }

    @DataProvider(name = "pageTitleData")
    public static Object[][] pageTitleData() {
        return new Object[][] {
                { "STORE", "PRODUCT STORE" }
        };
    }

}
