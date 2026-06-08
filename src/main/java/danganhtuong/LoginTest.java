package danganhtuong;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class LoginTest {
    public static void main(String[] args) {
        // Step 1 & 2: Initialize WebDriver object for Chrome (Selenium 4.6+ handles drivers natively)
        WebDriver driver = new ChromeDriver();

        try {
            // Step 3: Maximize the browser window
            driver.manage().window().maximize();

            // Step 4: Open OrangeHRM login page
            driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");

            // Wait briefly to ensure the page has loaded and title is available
            Thread.sleep(3000);

            // Step 5: Print the title of the web page to the console
            System.out.println("Print Title : " + driver.getTitle());

            // Step 6: Print the current URL of the web page to the console
            System.out.println("Print the Current URL : " + driver.getCurrentUrl());

            // Step 7: Nhập tên đăng nhập
            WebElement usernameField = driver.findElement(By.name("username"));
            usernameField.sendKeys("Admin");

            // Step 8: Nhập mật khẩu
            WebElement passwordField = driver.findElement(By.name("password"));
            passwordField.sendKeys("admin123");

            // Step 9: Bấm nút đăng nhập
            WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));
            loginButton.click();

            // Dừng 5 giây để bạn xem quá trình đăng nhập diễn ra
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // Step 7: Close the browser after execution
            // Đã comment lại lệnh này để trình duyệt không tự đóng theo yêu cầu
            // driver.quit();
        }
    }
}
