package org.example.tests;

import org.example.base.BaseTest;
import org.example.pages.CatalogPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GetAllBooksTest extends BaseTest {

    @Test
    public void testGetAllBooks() {
        CatalogPage catalogPage = new CatalogPage(driver, wait);
        catalogPage.navigateToCatalogPage();
        Assert.assertFalse(catalogPage.isBooksInCatalog(), "List should not be empty");
    }

}
