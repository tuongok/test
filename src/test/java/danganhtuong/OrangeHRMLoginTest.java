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
        Assert.assertTrue(isDashboard, "Đăng nhập thành công nhưng không thấy trang dashboard! URL hiện tại: " + driver.getCurrentUrl());
    }

    @Test
    public void testLoginFailure() {
        System.out.println("Running Test: testLoginFailure");
        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");

        WebElement usernameField = driver.findElement(By.name("username"));
        usernameField.sendKeys("Admin");

        // Nhập sai mật khẩu
        WebElement passwordField = driver.findElement(By.name("password"));
        passwordField.sendKeys("wrong_password");

        WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));
        loginButton.click();

        // Đợi thông báo lỗi xuất hiện
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Kiểm tra xem hệ thống có hiển thị thông báo lỗi "Invalid credentials" hay không
        WebElement errorMessage = driver.findElement(By.xpath("//p[contains(@class, 'oxd-alert-content-text')]"));
        Assert.assertTrue(errorMessage.isDisplayed(), "Không thấy thông báo lỗi hiển thị!");
        
        // Cố tình sửa chữ "Invalid credentials" thành "Sai mật khẩu rồi" để Github báo LỖI (Đỏ)
        Assert.assertEquals(errorMessage.getText(), "Sai mật khẩu rồi", "Nội dung thông báo lỗi không đúng!");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
