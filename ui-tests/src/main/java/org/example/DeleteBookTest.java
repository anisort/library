package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

public class DeleteBookTest {
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeClass
    public void setUp() {

        System.setProperty("webdriver.chrome.driver", "D:\\University6\\Practice\\chromedriver\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();

        Login.login(driver);
    }

    @Test
    public void deleteBookTest() {

        WebElement navigateLink = driver.findElement(By.xpath("//a[contains(text(),'Catalog')]"));
        navigateLink.click();
        wait.until(ExpectedConditions.urlContains("http://localhost:4200/catalog"));

        WebElement navigateButton = driver.findElement(By.xpath("//button[contains(text(),' T ')]"));
        navigateButton.click();
        wait.until(ExpectedConditions.urlContains("http://localhost:4200/catalog"));

        By imgLocator = By.cssSelector("img[alt='Test book updated']");
        WebElement img = wait.until(ExpectedConditions.presenceOfElementLocated(imgLocator));

        new Actions(driver).moveToElement(img).perform();

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.confirm = function(){ return true; }");

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(@class, 'overlay')][.//h4[normalize-space(text())='Test book updated']]//button[contains(@class, 'delete-btn')]"))).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("img[alt='Test book updated']")));

    }

    @AfterClass
    public void tearDown() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.quit();
    }
}
