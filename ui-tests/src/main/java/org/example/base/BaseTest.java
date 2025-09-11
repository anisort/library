package org.example.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.example.utils.Login;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.time.Duration;

public class BaseTest {

    protected WebDriver driver;
    protected WebDriverWait wait;

    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get("http://localhost/home");
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
        Login.login(driver, wait);
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }

}
