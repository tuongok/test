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
    public void testLogin() {
        System.out.println("Running Test: testLogin");
        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");

        WebElement usernameField = driver.findElement(By.name("username"));
        usernameField.sendKeys("Admin");

        // Bạn có thể tự sửa mật khẩu ở dòng này để test:
        // Đúng: "admin123" -> Github báo Xanh (Success)
        // Sai: "mat_khau_sai" -> Github báo Đỏ (Fail)
        WebElement passwordField = driver.findElement(By.name("password"));
        passwordField.sendKeys("admin123");

        WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));
        loginButton.click();

        // Đợi một chút để URL thay đổi sau khi đăng nhập
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Kiểm tra xem URL có chứa chữ 'dashboard' không, chứng tỏ đăng nhập thành công
        boolean isDashboard = driver.getCurrentUrl().contains("dashboard");
        Assert.assertTrue(isDashboard, "Đăng nhập thất bại (có thể do sai mật khẩu)! URL hiện tại: " + driver.getCurrentUrl());
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
