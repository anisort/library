package org.example.tests;

import org.example.base.BaseTest;
import org.example.pages.CatalogPage;
import org.example.pages.DeleteBookPage;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DeleteBookTest extends BaseTest {

    @Test
    public void deleteBookTest() {

        CatalogPage catalogPage = new CatalogPage(driver, wait);
        DeleteBookPage deleteBookPage = new DeleteBookPage(driver, wait);

        String title = "Test book updated";

        catalogPage.navigateToCatalogPage();
        catalogPage.navigateToCatalogPageByFilter("T");

        new Actions(driver).moveToElement(catalogPage.getImage(title)).perform();

        deleteBookPage.deleteBook(title);

        catalogPage.navigateToCatalogPageByFilter("T");
        Assert.assertTrue(catalogPage.isBookNotVisible(title), "Book should not be visible in catalog");

    }
}
