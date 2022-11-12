package page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$;

public class MainPage {
    private final SelenideElement header = $$("h2").findBy(text("Путешествие дня"));
    private final SelenideElement buyTour = $$("[class=\"button__text\"]").findBy(text("Купить"));
    private final SelenideElement buyTourInCredit = $$("[class=\"button__text\"]").findBy(text("Купить в кредит"));

    public MainPage(){
        header.shouldBe(visible);
    }

    public void debitPurchase() {
        buyTour.click();
        new PaymentPage();
    }

    public void creditPurchase() {
        buyTourInCredit.click();
        new CreditPage();
    }

}
