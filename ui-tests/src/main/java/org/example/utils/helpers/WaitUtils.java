package org.example.utils.helpers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WaitUtils {

    public static void waitForUrlContains(WebDriverWait wait , String url) {
        wait.until(ExpectedConditions.urlContains(url));
    }

    public static void waitForUrlMatches(WebDriverWait wait , String url) {
        wait.until(ExpectedConditions.urlMatches(url));
    }

    public static void waitForElementToBeClickableAndClick(WebDriverWait wait, By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    public static WebElement waitForPresenceOfElementLocated(WebDriverWait wait, By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public static boolean waitForVisibilityOfElementLocated(WebDriverWait wait, By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
    }

    public static boolean waitForInvisibilityOfElementLocated(WebDriverWait wait, By locator) {
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }
}
