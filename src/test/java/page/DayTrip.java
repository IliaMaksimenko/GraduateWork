package page;

import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$$;

public class DayTrip {

    private final SelenideElement buyTour = $$("[class=\"button__text\"]").findBy(text("Купить"));
    private final SelenideElement buyTourInCredit = $$("[class=\"button__text\"]").findBy(text("Купить в кредит"));
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


    public void debitPurchase() {
        buyTour.click();
    }

    public void creditPurchase() {
        buyTourInCredit.click();
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
        validationCardNumber.shouldBe(visible).shouldHave(text(massage));
    }

    public void validationMonth(String massage) {
        validationMonth.shouldBe(visible).shouldHave(text(massage));
    }

    public void validationYear(String massage) {
        validationYear.shouldBe(visible).shouldHave(text(massage));
    }

    public void validationOwner(String massage) {
        validationOwner.shouldBe(visible).shouldHave(text(massage));
    }

    public void validationCvc(String massage) {
        validationCvc.shouldBe(visible).shouldHave(text(massage));
    }
}
