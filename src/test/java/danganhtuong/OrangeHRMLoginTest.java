package danganhtuong;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;

public class OrangeHRMLoginTest {
    private WebDriver driver;

    @BeforeMethod
    public void setUp() {
        // Cấu hình WebDriverManager để tự tải ChromeDriver
        WebDriverManager.chromedriver().setup();
        
        // Cấu hình ChromeOptions để chạy Headless (cần thiết khi chạy trên GitHub Actions)
        ChromeOptions options = new ChromeOptions();
        
        // Nếu có biến môi trường CI (do GitHub Actions tự set), thì chạy ở chế độ Headless
        String ci = System.getenv("CI");
        if (ci != null && ci.equals("true")) {
            options.addArguments("--headless");
            options.addArguments("--disable-gpu");
            options.addArguments("--window-size=1920,1080");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
        } else {
            // Chạy bình thường trên máy tính của bạn
            options.addArguments("--start-maximized");
        }

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Test
    public void testLoginSuccess() {
        System.out.println("Running Test: testLoginSuccess");
        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");

        WebElement usernameField = driver.findElement(By.name("username"));
        usernameField.sendKeys("Admin");

        // Nhập ĐÚNG mật khẩu
        WebElement passwordField = driver.findElement(By.name("password"));
        passwordField.sendKeys("admin123");

        WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));
        loginButton.click();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        boolean isDashboard = driver.getCurrentUrl().contains("dashboard");
        Assert.assertTrue(isDashboard, "Đăng nhập ĐÚNG nhưng thất bại! URL hiện tại: " + driver.getCurrentUrl());
    }

    @Test
    public void testLoginFailure() {
        System.out.println("Running Test: testLoginFailure");
        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");

        WebElement usernameField = driver.findElement(By.name("username"));
        usernameField.sendKeys("Admin");

        // Nhập SAI mật khẩu
        WebElement passwordField = driver.findElement(By.name("password"));
        passwordField.sendKeys("mat_khau_sai_ne");

        WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));
        loginButton.click();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        WebElement errorMessage = driver.findElement(By.xpath("//p[contains(@class, 'oxd-alert-content-text')]"));
        Assert.assertTrue(errorMessage.isDisplayed(), "Không thấy thông báo lỗi!");
        Assert.assertEquals(errorMessage.getText(), "Invalid credentials", "Nội dung báo lỗi không đúng!");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
