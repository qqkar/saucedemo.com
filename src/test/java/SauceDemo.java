import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.util.concurrent.TimeUnit;

public class SauceDemo {

    static WebDriver driver;

    public void logIn(String username, String password) {
        WebElement loginInput = driver.findElement(By.id("user-name"));
        WebElement passwordInput = driver.findElement(By.id("password"));
        WebElement submitButton = driver.findElement(By.id("login-button"));
        loginInput.sendKeys(username);
        passwordInput.sendKeys(password);
        submitButton.click();
    }

    @BeforeAll
    static void setDriver() {
        driver = new ChromeDriver();
        driver.get("https://www.saucedemo.com/");
        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
    }

    @AfterAll
    static void closeDriver() {
        driver.close();
    }

    @Test
    void emptyLoginField() {
        WebElement sPasswordInput = driver.findElement(By.id("password"));
        WebElement sSubmitButton = driver.findElement(By.id("login-button"));
        sPasswordInput.sendKeys("secret_sauce");
        sSubmitButton.click();
        WebElement errorBox = driver.findElement(By.cssSelector(".error-message-container"));
        Assertions.assertEquals("Epic sadface: Username is required", errorBox.getText());
    }

    @Test
    void emptyPasswordField() {
        WebElement sLoginInput = driver.findElement(By.id("user-name"));
        WebElement sSubmitButton = driver.findElement(By.id("login-button"));
        sLoginInput.sendKeys("loginTest");
        sSubmitButton.click();
        WebElement errorBox = driver.findElement(By.cssSelector(".error-message-container"));
        Assertions.assertEquals("Epic sadface: Password is required", errorBox.getText());
    }

    @Test
    void logoAndDropDownField() {
        Assertions.assertTrue(driver.findElement(By.className("login_logo")).isDisplayed());
        logIn("standard_user", "secret_sauce");
        Assertions.assertTrue(driver.findElement(By.xpath("//div[@class = 'app_logo']")).isDisplayed());
        Select list = new Select(driver.findElement(By.className("product_sort_container")));
        list.selectByValue("hilo");
        Assertions.assertTrue(driver.findElement(By.xpath("//span[@class = 'active_option']")).isDisplayed());
    }

    @Test
    void homeToContact() {
        logIn("standard_user", "secret_sauce");
        driver.findElement(By.id("react-burger-menu-btn")).click();
        driver.findElement(By.xpath("//div[@class = 'bm-menu-wrap']"));
        driver.findElement(By.id("about_sidebar_link")).click();
        driver.findElement(By.xpath("(//a[@data-tc='Button'])[3]")).click();
        Assertions.assertTrue(driver.getTitle().contains("Contact Sales | Sauce Labs"));
    }

    @Test
    void loginToHome() {
        logIn("standard_user", "secret_sauce");
        Assertions.assertTrue(driver.findElement(By.xpath("//button[text()='Open Menu']")).isDisplayed());
    }

    @Test
    void addProductToBasket() {
        logIn("standard_user", "secret_sauce");
        driver.findElement(By.name("add-to-cart-test.allthethings()-t-shirt-(red)")).click();
        driver.findElement(By.xpath("//a[@class = 'shopping_cart_link']")).click();
        Assertions.assertAll(String.valueOf(true),
                () -> Assertions.assertTrue(driver.findElement(By.xpath("//span[text()='1']")).isDisplayed()),
                () -> Assertions.assertTrue(driver.findElement(By.id("remove-test.allthethings()-t-shirt-(red)")).isDisplayed()
                ));

    }

    @Test
    void addProductToBasketAndRemove() {
        logIn("standard_user", "secret_sauce");
        driver.findElement(By.name("add-to-cart-sauce-labs-bike-light")).click();
        driver.findElement(By.name("remove-sauce-labs-bike-light")).click();
        Assertions.assertTrue(driver.findElement(By.name("add-to-cart-sauce-labs-bike-light")).isDisplayed());
    }

    @Test
    void loginCheck() {
        logIn("standard_user", "secret_sauce");
        Assertions.assertTrue(driver.findElement(By.xpath("//span[text()='Products']")).isDisplayed());
    }
}

