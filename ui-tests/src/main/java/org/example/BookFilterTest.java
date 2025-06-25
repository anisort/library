package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class BookFilterTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeClass
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "D:\\University6\\Practice\\chromedriver\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();

        Login.login(driver);
        driver.get("http://localhost:4200/my-library");
    }

    @Test
    public void testFilterStatusesSequentially() {

        Map<String, String> filterToStatus = new HashMap<>();
        filterToStatus.put("To read", "to-read");
        filterToStatus.put("Reading", "reading");
        filterToStatus.put("Read", "read");
        filterToStatus.put("Favorite", "favorite");

        for (Map.Entry<String, String> entry : filterToStatus.entrySet()) {
            String filterText = entry.getKey();
            String expectedStatusClass = entry.getValue();

            WebElement filterButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(@class,'selector')]//button[normalize-space()='" + filterText + "']")));
            filterButton.click();

            WebElement firstBook = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".my-book-list app-book-item:first-child")));

            boolean statusFound = !firstBook.findElements(By.cssSelector("p." + expectedStatusClass)).isEmpty();

            Assert.assertTrue(statusFound, "After selecting the '" + filterText + "' filter, the first book should have the status '" + expectedStatusClass + "'");

        }
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}
