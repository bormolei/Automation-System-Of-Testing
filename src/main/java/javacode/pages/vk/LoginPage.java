package javacode.pages.vk;

import org.junit.BeforeClass;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import ru.sbtqa.tag.pagefactory.PageFactory;

/**
 * Created by Юра on 09.01.2019.
 */
public class LoginPage {
    WebDriver driver;

    @FindBy(id = "index_email")
    private WebElement login;

    @FindBy(id = "index_pass")
    private WebElement password;

    @FindBy(id = "index_login_button")
    private WebElement loginButton;

    public LoginPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    public void login(String userName, String userPassword){
        login.sendKeys(userName);
        password.sendKeys(userPassword);
        loginButton.click();
    }


}
