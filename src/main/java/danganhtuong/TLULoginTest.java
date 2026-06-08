package danganhtuong;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class TLULoginTest {
    public static void main(String[] args) {
        // Khởi tạo WebDriver object cho Chrome (Selenium 4.6+ tự động quản lý driver)
        WebDriver driver = new ChromeDriver();

        try {
            // Phóng to cửa sổ trình duyệt
            driver.manage().window().maximize();

            // Mở trang web của trường
            System.out.println("Đang mở trang https://sinhvien1.tlu.edu.vn/ ...");
            driver.get("https://sinhvien1.tlu.edu.vn/");

            // Thiết lập WebDriverWait (chờ tối đa 15 giây) do trang dùng Angular tải động
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

            // Tìm và nhập tài khoản
            // Chờ cho đến khi ô nhập tài khoản xuất hiện (thường là id="username")
            System.out.println("Đang tìm ô nhập tài khoản...");
            WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
            usernameField.clear();
            usernameField.sendKeys("2351067120");

            // Tìm và nhập mật khẩu (thường là id="password")
            System.out.println("Đang nhập mật khẩu...");
            WebElement passwordField = driver.findElement(By.id("password"));
            passwordField.clear();
            passwordField.sendKeys("Danganhtuong1230");

            // Tìm và bấm nút đăng nhập
            System.out.println("Đang bấm nút đăng nhập...");
            // Thử tìm nút có type='submit' hoặc chứa chữ 'Đăng nhập'
            WebElement loginButton;
            try {
                loginButton = driver.findElement(By.cssSelector("button[type='submit']"));
            } catch (Exception e) {
                loginButton = driver.findElement(By.xpath("//button[contains(translate(text(), 'ĐĂNG NHẬP', 'đăng nhập'), 'đăng nhập')]"));
            }
            loginButton.click();

            System.out.println("Đã gửi yêu cầu đăng nhập. Dừng 10 giây để xem kết quả...");
            // Dừng 10 giây để bạn xem quá trình đăng nhập diễn ra
            Thread.sleep(10000);

        } catch (Exception e) {
            System.out.println("Không thể hoàn thành tự động hóa do cấu trúc web có thể khác với dự kiến.");
            System.out.println("Lỗi chi tiết: " + e.getMessage());
        } finally {
            // Bạn có thể bỏ comment dòng dưới nếu muốn tự động đóng trình duyệt
            // driver.quit();
        }
    }
}
