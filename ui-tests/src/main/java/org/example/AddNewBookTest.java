package org.example;

import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;

import java.io.File;
import java.time.Duration;

public class AddNewBookTest {

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
    public void addNewBookTest(){

        WebElement navigateLink = driver.findElement(By.xpath("//a[contains(text(),'Catalog')]"));
        navigateLink.click();
        wait.until(ExpectedConditions.urlContains("http://localhost:4200/catalog"));

        WebElement addNewBookButton = driver.findElement(By.xpath("//button[contains(text(), 'Add new book')]"));
        addNewBookButton.click();
        wait.until(ExpectedConditions.urlContains("http://localhost:4200/book-form"));

        WebElement fileInput = driver.findElement(By.id("file"));
        File imageFile = new File("D:\\University6\\Practice\\images.jfif");
        fileInput.sendKeys(imageFile.getAbsolutePath());

        driver.findElement(By.id("title")).sendKeys("Test book");

        driver.findElement(By.id("author")).sendKeys("Test author");

        driver.findElement(By.id("summary")).sendKeys("Test summary");

        driver.findElement(By.id("link")).sendKeys("Test link");

        WebElement createButton = driver.findElement(By.xpath("//button[contains(text(), 'Create')]"));
        wait.until(ExpectedConditions.elementToBeClickable(createButton)).click();
        wait.until(ExpectedConditions.urlContains("http://localhost:4200/catalog"));

        WebElement navigateButton = driver.findElement(By.xpath("//button[contains(text(),'T')]"));
        navigateButton.click();
        wait.until(ExpectedConditions.urlContains("http://localhost:4200/catalog"));

        By imgLocator = By.cssSelector("img[alt='Test book']");
        WebElement img = wait.until(ExpectedConditions.presenceOfElementLocated(imgLocator));

        new Actions(driver).moveToElement(img).perform();

        By titleLocator = By.xpath("//div[contains(@class,'book-item-container')]//h4[text()='Test book']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(titleLocator));

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
