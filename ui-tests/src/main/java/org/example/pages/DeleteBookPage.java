package org.example.pages;

import org.example.base.BasePage;
import org.example.utils.helpers.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DeleteBookPage extends BasePage {

    public DeleteBookPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void deleteBook(String title) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.confirm = function(){ return true; }");
        WaitUtils.waitForElementToBeClickableAndClick(wait, By.xpath("//div[contains(@class, 'overlay')][.//h4[normalize-space(text())='" + title + "']]//button[contains(@class, 'delete-btn')]"));
    }
}
