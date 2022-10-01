package ru.netology;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {
    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void shouldSendFormSuccess() {
        WebElement form = driver.findElement(By.cssSelector("[action='/']"));
        form.findElement(By.cssSelector("[name=name]")).sendKeys("Нет Войне");
        form.findElement(By.cssSelector("[name=phone]")).sendKeys("+75550000000");
        form.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        form.findElement(By.cssSelector("[type=button]")).click();
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText();
        assertEquals(expected, actual.trim());
    }

    @Test
    void shouldGetErrorIfIncorrectNameOrSurname() {
        WebElement form = driver.findElement(By.cssSelector("[action='/']"));
        form.findElement(By.cssSelector("[name=name]")).sendKeys("123");
        form.findElement(By.cssSelector("[name=phone]")).sendKeys("+75550000000");
        form.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        form.findElement(By.cssSelector("[type=button]")).click();
        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actual = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub")).getText();
        assertEquals(expected, actual.trim());
    }

    @Test
    void shouldGetErrorIfIncorrectPhoneNumber() {
        WebElement form = driver.findElement(By.cssSelector("[action='/']"));
        form.findElement(By.cssSelector("[name=name]")).sendKeys("Нет Войне");
        form.findElement(By.cssSelector("[name=phone]")).sendKeys("75550000000");
        form.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        form.findElement(By.cssSelector("[type=button]")).click();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).getText();
        assertEquals(expected, actual.trim());
    }

    @Test
    void shouldGetErrorIfNameAndSurnameIsEmpty() {
        WebElement form = driver.findElement(By.cssSelector("[action='/']"));
        form.findElement(By.cssSelector("[name=name]")).sendKeys("");
        form.findElement(By.cssSelector("[name=phone]")).sendKeys("+75550000000");
        form.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        form.findElement(By.cssSelector("[type=button]")).click();
        String expected = "Поле обязательно для заполнения";
        String actual = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub")).getText();
        assertEquals(expected, actual.trim());
    }

    @Test
    void shouldGetErrorIfPhoneNumberIsEmpty() {
        WebElement form = driver.findElement(By.cssSelector("[action='/']"));
        form.findElement(By.cssSelector("[name=name]")).sendKeys("Нет Войне");
        form.findElement(By.cssSelector("[name=phone]")).sendKeys("");
        form.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        form.findElement(By.cssSelector("[type=button]")).click();
        String expected = "Поле обязательно для заполнения";
        String actual = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).getText();
        assertEquals(expected, actual.trim());
    }

    @Test
    void shouldGetErrorIfCheckboxIsNotChecked() {
        WebElement form = driver.findElement(By.cssSelector("[action='/']"));
        form.findElement(By.cssSelector("[name=name]")).sendKeys("Нет Войне");
        form.findElement(By.cssSelector("[name=phone]")).sendKeys("+75550000000");
        form.findElement(By.cssSelector("[type=button]")).click();
        boolean errorIsDisplayed = form.findElement(By.cssSelector("label.input_invalid")).isDisplayed();
        assertTrue(errorIsDisplayed);
    }
}
