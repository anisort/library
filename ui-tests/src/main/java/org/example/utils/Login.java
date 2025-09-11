package org.example.utils;

import org.example.pages.LoginPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Login {

    public static void login(WebDriver driver, WebDriverWait wait) {

        LoginPage loginPage = new LoginPage(driver, wait);

        loginPage.navigateToLoginPage();
        loginPage.enterUsername("admin");
        loginPage.enterPassword("Admin123#");
        loginPage.clickLoginButton();
    }
}
