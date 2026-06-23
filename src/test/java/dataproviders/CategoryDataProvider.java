package dataproviders;

import org.testng.annotations.DataProvider;

public class CategoryDataProvider {

    @DataProvider(name = "categoryData")
    public static Object[][] categoryData() {
        return new Object[][] {
                { "Phones"   },
                { "Laptops"  },
                { "Monitors" }
        };
    }

}
