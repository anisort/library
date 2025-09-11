package org.example.pages;

import org.example.base.BasePage;
import org.example.utils.helpers.ResourcesUtils;
import org.example.utils.helpers.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AddNewBookPage extends BasePage {

    private final By navigateAddNewBookButtonLocator = By.id("add-new-book-btn");
    private final By fileUploadFieldLocator = By.id("file");
    private final By titleFieldLocator = By.id("title");
    private final By authorFieldLocator= By.id("author");
    private final By summaryFieldLocator= By.id("summary");
    private final By linkFieldLocator= By.id("link");
    private final By createButtonLocator = By.id("submit-btn");

    public AddNewBookPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void navigateToAddNewBookPage() {
        driver.findElement(navigateAddNewBookButtonLocator).click();
        WaitUtils.waitForUrlContains(wait, "http://localhost/book-form");
    }

    public void uploadImage(String path) {
        driver.findElement(fileUploadFieldLocator).sendKeys(ResourcesUtils.getResourceFilePath(path)); // File
    }

    public void enterTitle(String title) {
        driver.findElement(titleFieldLocator).sendKeys(title);
    }

    public void enterAuthor(String author) {
        driver.findElement(authorFieldLocator).sendKeys(author);
    }

    public void enterSummary(String summary) {
        driver.findElement(summaryFieldLocator).sendKeys(summary);
    }

    public void enterLink(String link) {
        driver.findElement(linkFieldLocator).sendKeys(link);
    }

    public void clickCreateButton() {
        WaitUtils.waitForElementToBeClickableAndClick(wait, createButtonLocator);
        WaitUtils.waitForUrlContains(wait, "http://localhost/catalog");
    }
}
