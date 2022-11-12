package tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataHelper;
import data.SQLHelper;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import page.CreditPage;
import page.MainPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.*;

public class BuyInCreditTest {

    MainPage mainPage = open("http://localhost:8080", MainPage.class);

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void shouldOpen() {
        mainPage.creditPurchase();
    }

    @AfterEach
    void clearAll() {
        SQLHelper.clearDBTables();
    }


    @Test
    void positiveScriptBankApprovalTheRequest() {
        CreditPage creditPage = new CreditPage();
        creditPage.inputData(DataHelper.getApproveCardNumber(), DataHelper.getMonth(),
                DataHelper.getYear(1), DataHelper.getOwnerEng(), DataHelper.getCvc());
        creditPage.send();
        creditPage.alertMassageApprove();

        String expectedStatus = "APPROVED";

        assertNotNull(SQLHelper.getBankIdFromCreditEntity());
        assertNotNull(SQLHelper.getCreditIdFromOrderEntity());
        assertEquals(expectedStatus, SQLHelper.getStatusFromCreditRequestEntity());
        assertEquals(SQLHelper.getBankIdFromCreditEntity(), SQLHelper.getCreditIdFromOrderEntity());
    }

    @Test
    void negativeScriptSendEmptyForm() {
        CreditPage creditPage = new CreditPage();
        creditPage.send();
        creditPage.validationCardNumber("Неверный формат");
        creditPage.validationMonth("Неверный формат");
        creditPage.validationYear("Неверный формат");
        creditPage.validationOwner("Поле обязательно для заполнения");
        creditPage.validationCvc("Неверный формат");

        assertNull(SQLHelper.getStatusFromCreditRequestEntity());
    }

    @Test
    void negativeScriptBankDeniedTheRequest() {
        CreditPage creditPage = new CreditPage();
        creditPage.inputData(DataHelper.getDeclinedCardNumber(), DataHelper.getMonth(),
                DataHelper.getYear(1), DataHelper.getOwnerEng(), DataHelper.getCvc());
        creditPage.send();
        creditPage.alertMessageDeclined();

        String expectedStatus = "DECLINED";

        assertEquals(expectedStatus, SQLHelper.getStatusFromCreditRequestEntity());
        assertEquals(SQLHelper.getBankIdFromCreditEntity(), SQLHelper.getCreditIdFromOrderEntity());
    }

    @Test
    void negativeScriptSendUnregisteredCard() {
        CreditPage creditPage = new CreditPage();
        creditPage.inputData(DataHelper.getUnregisteredCardNumber(), DataHelper.getMonth(),
                DataHelper.getYear(1), DataHelper.getOwnerEng(), DataHelper.getCvc());
        creditPage.send();
        creditPage.alertMessageDeclined();

        String expectedStatus = "DECLINED";

        assertEquals(expectedStatus, SQLHelper.getStatusFromCreditRequestEntity());
        assertEquals(SQLHelper.getBankIdFromCreditEntity(), SQLHelper.getCreditIdFromOrderEntity());
    }

    @Test
    void negativeScriptEmptyFieldMonth() {
        CreditPage creditPage = new CreditPage();
        creditPage.inputData(DataHelper.getApproveCardNumber(), DataHelper.emptyField(),
                DataHelper.getYear(1), DataHelper.getOwnerEng(), DataHelper.getCvc());
        creditPage.send();
        creditPage.validationMonth("Неверный формат");

        assertNull(SQLHelper.getStatusFromCreditRequestEntity());
    }


    @Test
    void negativeScriptInvalidValueMonth() {
        CreditPage creditPage = new CreditPage();
        creditPage.inputData(DataHelper.getApproveCardNumber(), DataHelper.thirteenthMonth(),
                DataHelper.getYear(1), DataHelper.getOwnerEng(), DataHelper.getCvc());
        creditPage.send();
        creditPage.validationMonth("Неверно указан срок действия карты");

        assertNull(SQLHelper.getStatusFromCreditRequestEntity());
    }

    @Test
    void negativeScriptPastMonth() {
        CreditPage creditPage = new CreditPage();
        creditPage.inputData(DataHelper.getApproveCardNumber(), DataHelper.pastMonth(),
                DataHelper.getYear(0), DataHelper.getOwnerEng(), DataHelper.getCvc());
        creditPage.send();
        creditPage.validationMonth("Неверно указан срок действия карты");

        assertNull(SQLHelper.getStatusFromCreditRequestEntity());
    }

    @Test
    void negativeScriptIncompleteValueMonth() {
        CreditPage creditPage = new CreditPage();
        creditPage.inputData(DataHelper.getApproveCardNumber(), DataHelper.missingNumber(),
                DataHelper.getYear(1), DataHelper.getOwnerEng(), DataHelper.getCvc());
        creditPage.send();
        creditPage.validationMonth("Неверный формат");

        assertNull(SQLHelper.getStatusFromCreditRequestEntity());
    }

    @Test
    void negativeScriptEmptyFieldYear() {
        CreditPage creditPage = new CreditPage();
        creditPage.inputData(DataHelper.getApproveCardNumber(), DataHelper.getMonth(),
                "", DataHelper.getOwnerEng(), DataHelper.getCvc());
        creditPage.send();
        creditPage.validationYear("Неверный формат");

        assertNull(SQLHelper.getStatusFromCreditRequestEntity());
    }

    @Test
    void negativeScriptPastYear() {
        CreditPage creditPage = new CreditPage();
        creditPage.inputData(DataHelper.getApproveCardNumber(), DataHelper.getMonth(),
                DataHelper.getYear(-1), DataHelper.getOwnerEng(), DataHelper.getCvc());
        creditPage.send();
        creditPage.validationYear("Истёк срок действия карты");

        assertNull(SQLHelper.getStatusFromCreditRequestEntity());
    }

    @Test
    void negativeScriptInvalidValueYear() {
        CreditPage creditPage = new CreditPage();
        creditPage.inputData(DataHelper.getApproveCardNumber(), DataHelper.getMonth(),
                DataHelper.getYear(6), DataHelper.getOwnerEng(), DataHelper.getCvc());
        creditPage.send();
        creditPage.validationYear("Неверно указан срок действия карты");

        assertNull(SQLHelper.getStatusFromCreditRequestEntity());
    }

    @Test
    void negativeScriptIncompleteValueYear() {
        CreditPage creditPage = new CreditPage();
        creditPage.inputData(DataHelper.getApproveCardNumber(), DataHelper.getMonth(),
                DataHelper.missingNumber(), DataHelper.getOwnerEng(), DataHelper.getCvc());
        creditPage.send();
        creditPage.validationYear("Неверный формат");

        assertNull(SQLHelper.getStatusFromCreditRequestEntity());
    }

    @Test
    void negativeScriptEmptyFieldOwner() {
        CreditPage creditPage = new CreditPage();
        creditPage.inputData(DataHelper.getApproveCardNumber(), DataHelper.getMonth(),
                DataHelper.getYear(1), DataHelper.emptyField(), DataHelper.getCvc());
        creditPage.send();
        creditPage.validationOwner("Поле обязательно для заполнения");

        assertNull(SQLHelper.getStatusFromCreditRequestEntity());
    }

    @Test
    void negativeScriptInputNumberInFieldOwner() {
        CreditPage creditPage = new CreditPage();
        creditPage.inputData(DataHelper.getApproveCardNumber(), DataHelper.getMonth(),
                DataHelper.getYear(1), DataHelper.ownerNameNumber(), DataHelper.getCvc());
        creditPage.send();
        creditPage.validationOwner("Неверный формат");

        assertNull(SQLHelper.getStatusFromCreditRequestEntity());
    }

    @Test
    void negativeScriptInputSymbolInFieldOwner() {
        CreditPage creditPage = new CreditPage();
        creditPage.inputData(DataHelper.getApproveCardNumber(), DataHelper.getMonth(),
                DataHelper.getYear(1), DataHelper.randomSymbol(), DataHelper.getCvc());
        creditPage.send();
        creditPage.validationOwner("Неверный формат");

        assertNull(SQLHelper.getStatusFromCreditRequestEntity());
    }

    @Test
    void negativeScriptInputCyrillicLettersInFieldOwner() {
        CreditPage creditPage = new CreditPage();
        creditPage.inputData(DataHelper.getApproveCardNumber(), DataHelper.getMonth(),
                DataHelper.getYear(1), DataHelper.getOwnerRus(), DataHelper.getCvc());
        creditPage.send();
        creditPage.validationOwner("Неверный формат");

        assertNull(SQLHelper.getStatusFromCreditRequestEntity());
    }

    @Test
    void negativeScriptInput28SymbolInFieldOwner() {
        CreditPage creditPage = new CreditPage();
        creditPage.inputData(DataHelper.getApproveCardNumber(), DataHelper.getMonth(),
                DataHelper.getYear(1), DataHelper.get28SymbolOwner(), DataHelper.getCvc());
        creditPage.send();
        creditPage.validationOwner("Неверный формат");

        assertNull(SQLHelper.getStatusFromCreditRequestEntity());
    }

    @Test
    void negativeScriptEmptyFieldCvc() {
        CreditPage creditPage = new CreditPage();
        creditPage.inputData(DataHelper.getApproveCardNumber(), DataHelper.getMonth(),
                DataHelper.getYear(1), DataHelper.getOwnerEng(), DataHelper.emptyField());
        creditPage.send();
        creditPage.validationCvc("Неверный формат");

        assertNull(SQLHelper.getStatusFromCreditRequestEntity());
    }

    @Test
    void negativeScriptIncompleteValueCvc() {
        CreditPage creditPage = new CreditPage();
        creditPage.inputData(DataHelper.getApproveCardNumber(), DataHelper.getMonth(),
                DataHelper.getYear(1), DataHelper.getOwnerEng(), DataHelper.missingNumberCVC());
        creditPage.send();
        creditPage.validationCvc("Неверный формат");

        assertNull(SQLHelper.getStatusFromCreditRequestEntity());
    }

    @Test
    void negativeScriptIncompleteValueCardNumber() {
        CreditPage creditPage = new CreditPage();
        creditPage.inputData(DataHelper.missingNumberCard(), DataHelper.getMonth(),
                DataHelper.getYear(1), DataHelper.getOwnerEng(), DataHelper.getCvc());
        creditPage.send();
        creditPage.validationCardNumber("Неверный формат");

        assertNull(SQLHelper.getStatusFromCreditRequestEntity());
    }
}

