package org.example.pages;

import org.example.base.BasePage;
import org.example.utils.helpers.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CatalogPage extends BasePage {

    private final By navigateToCatalogButtonLocator = By.id("navigate-catalog");
    private final By bookslistLocator = By.cssSelector(".book-item-container");

    public CatalogPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void navigateToCatalogPage() {
        driver.findElement(navigateToCatalogButtonLocator).click();
        WaitUtils.waitForUrlContains(wait, "http://localhost/catalog");
    }

    public void navigateToCatalogPageByFilter(String filter) {
        By filterLocator = By.xpath("//button[contains(text(),'" + filter + "')]");
        WaitUtils.waitForElementToBeClickableAndClick(wait, filterLocator);
        WaitUtils.waitForUrlContains(wait, "http://localhost/catalog");
    }

    public WebElement getImage(String title) {
        By imgLocator = By.cssSelector("img[alt='" + title + "']");
        return WaitUtils.waitForPresenceOfElementLocated(wait, imgLocator);
    }

    public boolean isBookVisible(String title) {
        new Actions(driver).moveToElement(getImage(title)).perform();
        By titleLocator = By.xpath("//div[contains(@class,'book-item-container')]//h4[text()='" + title + "']");
        return WaitUtils.waitForVisibilityOfElementLocated(wait, titleLocator);
    }

    public boolean isBookNotVisible(String title) {
        By titleLocator = By.xpath("//div[contains(@class,'book-item-container')]//h4[text()='" + title + "']");
        return WaitUtils.waitForInvisibilityOfElementLocated(wait, titleLocator);
    }

    public boolean isBooksInCatalog() {

        return driver.findElements(bookslistLocator).isEmpty();
    }

}
