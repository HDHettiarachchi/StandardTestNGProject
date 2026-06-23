package listners;

import org.testng.ITestResult;
import org.testng.internal.annotations.IListeners;

public class TestListener implements IListeners {

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("\n▶ STARTING : " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("✅ PASSED   : " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("❌ FAILED   : " + result.getMethod().getMethodName());
        System.out.println("   Reason   : " + result.getThrowable().getMessage());

        Object testInstance = result.getInstance();
        WebDriver driver = getDriverFromInstance(testInstance);

        if (driver != null) {
            takeScreenshot(driver, result.getMethod().getMethodName());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("⏭ SKIPPED  : " + result.getMethod().getMethodName());
    }

    private WebDriver getDriverFromInstance(Object instance) {
        try {
            java.lang.reflect.Field field;
            Class<?> clazz = instance.getClass();

            // Walk up the class hierarchy to find the 'driver' field
            // Covers both direct BaseTest subclasses and CategoryTest (no extends)
            while (clazz != null) {
                try {
                    field = clazz.getDeclaredField("driver");
                    field.setAccessible(true);
                    return (WebDriver) field.get(instance);
                } catch (NoSuchFieldException e) {
                    clazz = clazz.getSuperclass();
                }
            }
            System.out.println("   Could not find driver field in class hierarchy.");
            return null;

        } catch (Exception e) {
            System.out.println("   Could not get driver: " + e.getMessage());
            return null;
        }
    }

    private void takeScreenshot(WebDriver driver, String testName) {
        try {
            String folderPath = "screenshots";
            Files.createDirectories(Paths.get(folderPath));

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String filePath  = folderPath + "/" + testName + "_" + timestamp + ".png";

            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(screenshot.toPath(), Paths.get(filePath));

            System.out.println("   📸 Screenshot saved: " + filePath);

        } catch (IOException e) {
            System.out.println("   Screenshot failed: " + e.getMessage());
        }
    }

}
