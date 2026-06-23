package dataproviders;

import org.testng.annotations.DataProvider;

public class ProductDataProvider {

    // Reuses the same category set — ProductTest iterates
    // every product inside each category row
    @DataProvider(name = "categoryData")
    public static Object[][] categoryData() {
        return new Object[][] {
                { "Phones",   new String[]{"Samsung", "Nokia", "HTC", "Sony", "Apple", "iPhone", "Nexus"} },
                { "Laptops",  new String[]{"Sony", "Apple", "Dell", "Samsung", "Lenovo"}                  },
                { "Monitors", new String[]{"Apple", "ASUS", "Monitor"}                                    }
        };
    }

}
