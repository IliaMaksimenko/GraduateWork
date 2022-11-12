package page;

import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$$;

public class PaymentPage {
    private final SelenideElement header = $$("h3").findBy(text("Оплата по карте"));

    private final SelenideElement inputCardNumber = $$(".input").findBy(exactText("Номер карты")).$(".input__control");
    private final SelenideElement inputMonth = $$(".input").findBy(exactText("Месяц")).$(".input__control");
    private final SelenideElement inputYears = $$(".input").findBy(exactText("Год")).$(".input__control");
    private final SelenideElement inputOwner = $$(".input").findBy(exactText("Владелец")).$(".input__control");
    private final SelenideElement inputCvc = $$(".input").findBy(exactText("CVC/CVV")).$(".input__control");
    private final SelenideElement buttonProceed = $$(".button").find(exactText("Продолжить"));

    private final SelenideElement validationCardNumber = $$("[class=\"input__top\"]").findBy(exactText("Номер карты")).parent().$(".input__sub");
    private final SelenideElement validationMonth = $$("[class=\"input__top\"]").findBy(exactText("Месяц")).parent().$(".input__sub");
    private final SelenideElement validationYear = $$("[class=\"input__top\"]").findBy(exactText("Год")).parent().$(".input__sub");
    private final SelenideElement validationOwner = $$("[class=\"input__top\"]").findBy(exactText("Владелец")).parent().$(".input__sub");
    private final SelenideElement validationCvc = $$("[class=\"input__top\"]").findBy(exactText("CVC/CVV")).parent().$(".input__sub");

    private final SelenideElement alertMassageApprove = $$(".notification__title").findBy(exactText("Успешно"));
    private final SelenideElement alertMessageDeclined = $$(".notification__title").findBy(exactText("Ошибка"));
    private final SelenideElement closerAlert = $$(".notification").findBy(visible).$(".notification__closer");

    public PaymentPage() {
        header.shouldBe(visible);
    }

    public void inputData(String cardNumber, String month, String year, String owner, String cvc) {
        inputCardNumber.setValue(cardNumber);
        inputMonth.setValue(month);
        inputYears.setValue(year);
        inputOwner.setValue(owner);
        inputCvc.setValue(cvc);
    }

    public void send() {
        buttonProceed.click();
    }

    public void close() {
        closerAlert.click();
    }

    public void alertMassageApprove() {
        alertMassageApprove.shouldBe(visible, Duration.ofSeconds(20));
    }

    public void alertMessageDeclined() {
        alertMessageDeclined.shouldBe(visible, Duration.ofSeconds(20));
    }

    public void validationCardNumber(String massage) {
        validationCardNumber.shouldHave(text(massage)).shouldBe(visible);
    }

    public void validationMonth(String massage) {
        validationMonth.shouldHave(text(massage)).shouldBe(visible);
    }

    public void validationYear(String massage) {
        validationYear.shouldHave(text(massage)).shouldBe(visible);
    }

    public void validationOwner(String massage) {
        validationOwner.shouldHave(text(massage)).shouldBe(visible);
    }

    public void validationCvc(String massage) {
        validationCvc.shouldHave(text(massage)).shouldBe(visible);
    }
}
