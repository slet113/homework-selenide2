package ru.netology;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;


public class RegistrationTest {
    @BeforeEach
    void setUp() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999/");
    }

    private String generateDate(long addDays, String pattern) {
        return LocalDate.now().plusDays(addDays).format(DateTimeFormatter.ofPattern(pattern));
    }

    @Test
    void standardRegistration() {
        $("[placeholder='Город']").setValue("Якутск");
        $("[placeholder='Дата встречи']").doubleClick();
        $("[placeholder='Дата встречи']").sendKeys(Keys.BACK_SPACE);
        String currentDate = generateDate(3, "dd.MM.yyyy");
        $("[placeholder='Дата встречи']").setValue(currentDate);
        $("[name='name']").setValue("Котов Андрей");
        $("[name='phone']").setValue("+79536988552");
        $("[data-test-id='agreement']").click();
        $x("//div/button").click();
        $(".notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.text("Встреча успешно забронирована на " + currentDate));
    }

    @Test
    void registrationWithADash() {
        $("[placeholder='Город']").setValue("Рязань");
        $("[placeholder='Дата встречи']").doubleClick();
        $("[placeholder='Дата встречи']").sendKeys(Keys.BACK_SPACE);
        String currentDate = generateDate(3, "dd.MM.yyyy");
        $("[placeholder='Дата встречи']").setValue(currentDate);
        $("[name='name']").setValue("Котов-Рогов Андрей");
        $("[name='phone']").setValue("+79536988552");
        $("[data-test-id='agreement']").click();
        $x("//div/button").click();
        $(".notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.text("Встреча успешно забронирована на " + currentDate));
    }

    @Test
    void registration5DaysInAdvance() {
        $("[placeholder='Город']").setValue("Самара");
        $("[placeholder='Дата встречи']").doubleClick();
        $("[placeholder='Дата встречи']").sendKeys(Keys.BACK_SPACE);
        String currentDate = generateDate(5, "dd.MM.yyyy");
        $("[placeholder='Дата встречи']").setValue(currentDate);
        $("[name='name']").setValue("Котов-Рогов Андрей");
        $("[name='phone']").setValue("+79536988552");
        $("[data-test-id='agreement']").click();
        $x("//div/button").click();
        $(".notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.text("Встреча успешно забронирована на " + currentDate));
    }

    @Test
    void theCityIsNotAnAdministrativeSubject() {
        $("[placeholder='Город']").setValue("Нижневартовск");
        $("[placeholder='Дата встречи']").doubleClick();
        $("[placeholder='Дата встречи']").sendKeys(Keys.BACK_SPACE);
        String currentDate = generateDate(3, "dd.MM.yyyy");
        $("[placeholder='Дата встречи']").setValue(currentDate);
        $("[name='name']").setValue("Котов-Рогов Андрей");
        $("[name='phone']").setValue("+79536988552");
        $("[data-test-id='agreement']").click();
        $x("//div/button").click();
        $x("//div//span[contains(text(), 'Доставка в выбранный город недоступна')]").shouldBe(visible);
    }

    @Test
    void emptyCityField() {
        $("[placeholder='Город']").setValue("");
        $("[placeholder='Дата встречи']").doubleClick();
        $("[placeholder='Дата встречи']").sendKeys(Keys.BACK_SPACE);
        String currentDate = generateDate(3, "dd.MM.yyyy");
        $("[placeholder='Дата встречи']").setValue(currentDate);
        $("[name='name']").setValue("Котов-Рогов Андрей");
        $("[name='phone']").setValue("+79536988552");
        $("[data-test-id='agreement']").click();
        $x("//div/button").click();
        $x("//div//span[contains(text(), 'Поле обязательно для заполнения')]").shouldBe(visible);
    }

    @Test
    void registrationForADateLessThan3Days() {
        $("[placeholder='Город']").setValue("Москва");
        $("[placeholder='Дата встречи']").doubleClick();
        $("[placeholder='Дата встречи']").sendKeys(Keys.BACK_SPACE);
        String currentDate = generateDate(1, "dd.MM.yyyy");
        $("[placeholder='Дата встречи']").setValue(currentDate);
        $("[name='name']").setValue("Котов-Рогов Андрей");
        $("[name='phone']").setValue("+79536988552");
        $("[data-test-id='agreement']").click();
        $x("//div/button").click();
        $x("//div//span[contains(text(), 'Заказ на выбранную дату невозможен')]").shouldBe(visible);
    }

    @Test
    void nameFieldIsEmpty() {
        $("[placeholder='Город']").setValue("Москва");
        $("[placeholder='Дата встречи']").doubleClick();
        $("[placeholder='Дата встречи']").sendKeys(Keys.BACK_SPACE);
        String currentDate = generateDate(3, "dd.MM.yyyy");
        $("[placeholder='Дата встречи']").setValue(currentDate);
        $("[name='name']").setValue("");
        $("[name='phone']").setValue("+79536988552");
        $("[data-test-id='agreement']").click();
        $x("//div/button").click();
        $x("//div//span[contains(text(), 'Поле обязательно для заполнения')]").shouldBe(visible);
    }

    @Test
    void theNameFieldIsFilledWithEnglishLetters() {
        $("[placeholder='Город']").setValue("Москва");
        $("[placeholder='Дата встречи']").doubleClick();
        $("[placeholder='Дата встречи']").sendKeys(Keys.BACK_SPACE);
        String currentDate = generateDate(3, "dd.MM.yyyy");
        $("[placeholder='Дата встречи']").setValue(currentDate);
        $("[name='name']").setValue("Sletkova Ekaterina");
        $("[name='phone']").setValue("+79536988552");
        $("[data-test-id='agreement']").click();
        $x("//div/button").click();
        $x("//div//span[contains(text(), 'Имя и Фамилия указаные неверно.')]").shouldBe(visible);
    }

    @Test
    void invalidCharacterInFieldName() {
        $("[placeholder='Город']").setValue("Москва");
        $("[placeholder='Дата встречи']").doubleClick();
        $("[placeholder='Дата встречи']").sendKeys(Keys.BACK_SPACE);
        String currentDate = generateDate(3, "dd.MM.yyyy");
        $("[placeholder='Дата встречи']").setValue(currentDate);
        $("[name='name']").setValue("Романов,Мишуков Игорь");
        $("[name='phone']").setValue("+79536988552");
        $("[data-test-id='agreement']").click();
        $x("//div/button").click();
        $x("//div//span[contains(text(), 'Имя и Фамилия указаные неверно.')]").shouldBe(visible);
    }

    @Test
    void thereIsNoSignPlusInThePhoneField() {
        $("[placeholder='Город']").setValue("Москва");
        $("[placeholder='Дата встречи']").doubleClick();
        $("[placeholder='Дата встречи']").sendKeys(Keys.BACK_SPACE);
        String currentDate = generateDate(3, "dd.MM.yyyy");
        $("[placeholder='Дата встречи']").setValue(currentDate);
        $("[name='name']").setValue("Романов Игорь");
        $("[name='phone']").setValue("89536988552");
        $("[data-test-id='agreement']").click();
        $x("//div/button").click();
        $x("//div//span[contains(text(), 'Телефон указан неверно.')]").shouldBe(visible);
    }

    @Test
    void extraNumberInThePhoneField() {
        $("[placeholder='Город']").setValue("Москва");
        $("[placeholder='Дата встречи']").doubleClick();
        $("[placeholder='Дата встречи']").sendKeys(Keys.BACK_SPACE);
        String currentDate = generateDate(3, "dd.MM.yyyy");
        $("[placeholder='Дата встречи']").setValue(currentDate);
        $("[name='name']").setValue("Романов Игорь");
        $("[name='phone']").setValue("+779536988552");
        $("[data-test-id='agreement']").click();
        $x("//div/button").click();
        $x("//div//span[contains(text(), 'Телефон указан неверно.')]").shouldBe(visible);
    }

    @Test
    void phoneFieldIsEmpty() {
        $("[placeholder='Город']").setValue("Москва");
        $("[placeholder='Дата встречи']").doubleClick();
        $("[placeholder='Дата встречи']").sendKeys(Keys.BACK_SPACE);
        String currentDate = generateDate(3, "dd.MM.yyyy");
        $("[placeholder='Дата встречи']").setValue(currentDate);
        $("[name='name']").setValue("Романов Игорь");
        $("[name='phone']").setValue("");
        $("[data-test-id='agreement']").click();
        $x("//div/button").click();
        $x("//div//span[contains(text(), 'Поле обязательно для заполнения')]").shouldBe(visible);
    }

    @Test
    void noCheckbox() {
        $("[placeholder='Город']").setValue("Москва");
        $("[placeholder='Дата встречи']").doubleClick();
        $("[placeholder='Дата встречи']").sendKeys(Keys.BACK_SPACE);
        String currentDate = generateDate(3, "dd.MM.yyyy");
        $("[placeholder='Дата встречи']").setValue(currentDate);
        $("[name='name']").setValue("Романов Игорь");
        $("[name='phone']").setValue("+79536988592");
        $x("//div/button").click();
        $x("//div//span[contains(text(), 'Я соглашаюсь с условиями обработки')]").shouldBe(visible);
    }
}