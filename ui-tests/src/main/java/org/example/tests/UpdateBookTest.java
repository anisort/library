package org.example.tests;

import org.example.base.BaseTest;
import org.example.pages.CatalogPage;
import org.example.pages.UpdateBookPage;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.Test;

public class UpdateBookTest extends BaseTest {

    @Test
    public void updateBookTest() {

        CatalogPage catalogPage = new CatalogPage(driver, wait);
        UpdateBookPage updateBookPage = new UpdateBookPage(driver, wait);

        String oldTitle = "Test book";
        String newTitle = "Test book updated";

        catalogPage.navigateToCatalogPage();
        catalogPage.navigateToCatalogPageByFilter("T");

        new Actions(driver).moveToElement(catalogPage.getImage(oldTitle)).perform();

        updateBookPage.clickUpdateNavigateButton(oldTitle);
        updateBookPage.changeTitle(newTitle);
        updateBookPage.clickUpdateButton();

        catalogPage.navigateToCatalogPageByFilter("T");
        Assert.assertTrue(catalogPage.isBookVisible(newTitle), "Book should be visible in catalog");

    }
}
