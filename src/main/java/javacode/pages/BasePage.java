package javacode.pages;


import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.ru.Дано;
import cucumber.api.java.ru.Тогда;
import javacode.pages.vk.LoginPage;
import javacode.utils.Props;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.sbtqa.tag.pagefactory.*;
import ru.sbtqa.tag.pagefactory.annotations.ActionTitle;

import javax.xml.ws.Action;


/**
 * Created by Юра on 09.01.2019.
 */
public class BasePage extends Page {
    static WebDriver driver;

    public BasePage() {
        PageFactory.initElements(PageFactory.getWebDriver(), this);
    }

    @After
    private void exit(){
        driver.quit();
        driver.close();
    }
    public void openVkPage() throws Throwable {
        BasePage.this.pageIsOpen();
    }

    @Дано("^открыта страница вк$")
    public void pageIsOpen() throws Throwable {
        openVkPage();
    }

    @ActionTitle("^залогиниться в вк$")
    public void logInVk() throws Throwable {
        LoginPage login = new LoginPage(driver);
        login.login(Props.getTestProperty("userName"), Props.getTestProperty("userPassword"));
    }
}
