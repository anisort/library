package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Login {

    public static void login(WebDriver driver) {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get("http://localhost:4200/home");
        WebElement navigateButton = driver.findElement(By.xpath("//button[contains(text(),'Login')]"));
        navigateButton.click();
        wait.until(ExpectedConditions.urlContains("http://auth:8080/login"));

        WebElement usernameField = driver.findElement(By.id("username"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.xpath("//button[contains(text(),'Sign in')]"));
        usernameField.sendKeys("admin");
        passwordField.sendKeys("Test100#");
        loginButton.click();
        wait.until(ExpectedConditions.urlContains("http://localhost:4200/home"));
    }
}
