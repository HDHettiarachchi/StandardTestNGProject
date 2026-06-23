package dataproviders;

import config.ConfigReader;
import org.testng.annotations.DataProvider;

public class SignUpDataProvider {

    @DataProvider(name = "signUpData")
    public static Object[][] signUpData() {
        return new Object[][] {
                { ConfigReader.getUsername(), ConfigReader.getPassword() }
        };
    }

    // Negative cases where site SHOULD reject and show an alert
    @DataProvider(name = "negativeSignUpData")
    public static Object[][] negativeSignUpData() {
        return new Object[][] {
                // { username, password, expectedAlertContains, scenario }
                { ConfigReader.getUsername(), ConfigReader.getPassword(),
                        "already exist",   "TC-01-N1: Already registered user"  },
                { "",                  "Password123!",
                        "Please fill out", "TC-01-N2: Empty username"           },
                { "testuser_qa_01",    "",
                        "Please fill out", "TC-01-N3: Empty password"           },
                { "",                  "",
                        "Please fill out", "TC-01-N4: Both fields empty"        },
                { "   ",               "Password123!",
                        "Please fill out", "TC-01-N5: Spaces only username"     },
                { "testuser_qa_01",    "   ",
                        "Please fill out", "TC-01-N6: Spaces only password"     },
        };
    }

    // Cases where site has no validation — we document what it does
    @DataProvider(name = "noValidationSignUpData")
    public static Object[][] noValidationSignUpData() {
        return new Object[][] {
                // { username, password, scenario, isBug }
                { "a".repeat(256),             "Password123!",
                        "TC-01-N7: Very long username (256 chars)",    true  },
                { "!@#$%^&*()",                "Password123!",
                        "TC-01-N8: Special characters in username",    true  },
                { "' OR '1'='1",              "Password123!",
                        "TC-01-N9: SQL injection in username",         true  },
                { "<script>alert(1)</script>", "Password123!",
                        "TC-01-N10: XSS script tag in username",       true  }
        };
    }

}
