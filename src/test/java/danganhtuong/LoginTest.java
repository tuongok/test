package danganhtuong;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;

public class LoginTest {
    private WebDriver driver;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        String ci = System.getenv("CI");
        if (ci != null && ci.equals("true")) {
            options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1080", "--no-sandbox", "--disable-dev-shm-usage");
        }
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Test
    public void testLoginSuccess() {
        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
        driver.findElement(By.name("username")).sendKeys("Admin");
        driver.findElement(By.name("password")).sendKeys("admin123");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        
        try { Thread.sleep(3000); } catch (Exception e) {}
        
        Assert.assertTrue(driver.getCurrentUrl().contains("dashboard"), "Đăng nhập phải thành công!");
    }

    @Test
    public void testLoginFail() {
        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
        driver.findElement(By.name("username")).sendKeys("Admin");
        
        driver.findElement(By.name("password")).sendKeys("admin1230");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        
        try { Thread.sleep(3000); } catch (Exception e) {}
        
        // Dòng này sẽ gây ra lỗi (Đỏ) vì sai mật khẩu thì không thể vào dashboard được
        Assert.assertTrue(driver.getCurrentUrl().contains("dashboard"), "Cố tình tạo lỗi: Không vào được dashboard do sai mật khẩu!");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) driver.quit();
    }
}
