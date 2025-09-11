package org.example.tests;

import org.example.base.BaseTest;
import org.example.pages.AddNewBookPage;
import org.example.pages.CatalogPage;
import org.example.utils.helpers.WaitUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AddNewBookTest extends BaseTest {

    @Test
    public void addNewBookTest(){

        CatalogPage catalogPage = new CatalogPage(driver, wait);
        AddNewBookPage addNewBookPage = new AddNewBookPage(driver, wait);

        String path = "img/test.png";
        String title = "Test book";
        String author = "Test author";
        String summary = "Test summary";
        String link = "Test link";

        catalogPage.navigateToCatalogPage();
        WaitUtils.waitForUrlContains(wait, "http://localhost/catalog");

        addNewBookPage.navigateToAddNewBookPage();
        addNewBookPage.uploadImage(path);
        addNewBookPage.enterTitle(title);
        addNewBookPage.enterAuthor(author);
        addNewBookPage.enterSummary(summary);
        addNewBookPage.enterLink(link);
        addNewBookPage.clickCreateButton();

        catalogPage.navigateToCatalogPageByFilter("T");
        Assert.assertTrue(catalogPage.isBookVisible(title), "Book should be visible in catalog");

    }
}
