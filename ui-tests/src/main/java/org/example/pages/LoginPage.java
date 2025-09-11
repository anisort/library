package org.example.pages;

import org.example.base.BasePage;
import org.example.utils.helpers.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage extends BasePage {

    private final By navigateLoginButtonLocator = By.id("navigate-login-btn");
    private final By usernameFieldLocator = By.id("username");
    private final By passwordFieldLocator = By.id("password");
    private final By loginButtonLocator = By.id("login-btn");

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void navigateToLoginPage() {
        driver.findElement(navigateLoginButtonLocator).click();
        WaitUtils.waitForUrlContains(wait, "http://localhost/auth/login");
    }

    public void enterUsername(String username) {
        driver.findElement(usernameFieldLocator).sendKeys(username);
    }

    public void enterPassword(String password) {
        driver.findElement(passwordFieldLocator).sendKeys(password);
    }

    public void clickLoginButton() {
        driver.findElement(loginButtonLocator).click();
        WaitUtils.waitForUrlContains(wait, "http://localhost/home");
    }
}
