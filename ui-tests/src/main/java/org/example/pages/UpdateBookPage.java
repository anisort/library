package org.example.pages;

import org.example.base.BasePage;
import org.example.utils.helpers.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class UpdateBookPage extends BasePage {

    private final By titleFieldLocator= By.id("title");
    private final By updateButtonLocator = By.id("submit-btn");

    public UpdateBookPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void clickUpdateNavigateButton(String title) {
        WaitUtils.waitForElementToBeClickableAndClick(wait, By.xpath("//div[contains(@class, 'overlay')][.//h4[normalize-space(text())='" + title + "']]//button[contains(@class, 'update-btn')]"));
        WaitUtils.waitForUrlMatches(wait, "^http://localhost/book-form/\\d+$");
    }

    public void changeTitle(String title) {
        WebElement titleField = driver.findElement(titleFieldLocator);
        titleField.clear();
        titleField.sendKeys(title);
    }

    public void clickUpdateButton() {
        WaitUtils.waitForElementToBeClickableAndClick(wait, updateButtonLocator);
        WaitUtils.waitForUrlContains(wait, "http://localhost/catalog");
    }


}
