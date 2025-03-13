import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.List;

public class HotelBookingTest {
    WebDriver driver;

    @BeforeClass
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");
        driver = new ChromeDriver();
        driver.get("https://your-github-hosted-url/home");
    }

    @Test(priority = 1)
    public void userLoginAndBookHotel() {
        driver.findElement(By.id("userUsername")).sendKeys("testuser");
        driver.findElement(By.id("userPassword")).sendKeys("password");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Search Hotel
        driver.findElement(By.id("searchHotel")).sendKeys("Grand Palace");
        driver.findElement(By.id("searchButton")).click();

        // Book Now
        driver.findElement(By.cssSelector(".bookNow")).click();
        driver.findElement(By.id("name")).sendKeys("Rama");
        driver.findElement(By.id("email")).sendKeys("rama@example.com");
        driver.findElement(By.id("phone")).sendKeys("9876543210");
        driver.findElement(By.id("date")).sendKeys("2025-03-14");
        driver.findElement(By.id("days")).sendKeys("2");
        driver.findElement(By.id("confirmBooking")).click();

        WebElement message = driver.findElement(By.id("bookingMessage"));
        Assert.assertEquals(message.getText(), "Booking Successful!");
    }

    @Test(priority = 2)
    public void adminCancelBooking() {
        driver.get("https://your-github-hosted-url/adminDashboard");
        driver.findElement(By.id("adminUsername")).sendKeys("admin");
        driver.findElement(By.id("adminPassword")).sendKeys("admin123");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Count bookings before cancellation
        List<WebElement> bookingsBefore = driver.findElements(By.xpath("//table/tr"));
        int countBefore = bookingsBefore.size();

        // Cancel Booking
        driver.findElement(By.id("cancelBooking")).click();
        WebElement cancelMsg = driver.findElement(By.id("cancelMessage"));
        Assert.assertEquals(cancelMsg.getText(), "Booking Cancelled Successfully!");

        // Verify booking count reduced by 1
        List<WebElement> bookingsAfter = driver.findElements(By.xpath("//table/tr"));
        int countAfter = bookingsAfter.size();
        Assert.assertEquals(countAfter, countBefore - 1);
    }

    @Test(priority = 3)
    public void verifyCancelledBookingRemoved() {
        driver.get("https://your-github-hosted-url/adminDashboard");

        // Verify the cancelled booking is not in the active list
        List<WebElement> bookingsAfter = driver.findElements(By.xpath("//table/tr"));
        boolean isBookingStillPresent = false;

        for (WebElement row : bookingsAfter) {
            if (row.getText().contains("Rama") && row.getText().contains("Grand Palace")) {
                isBookingStillPresent = true;
                break;
            }
        }
        Assert.assertFalse(isBookingStillPresent, "Cancelled booking is still present!");
    }

    @AfterClass
    public void teardown() {
        driver.quit();
    }
}