package tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataHelper;
import data.SQLHelper;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import page.MainPage;
import page.PaymentPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.*;


public class CardPaymentTest {

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
        mainPage.debitPurchase();
    }

    @AfterEach
    void clearAll() {
        SQLHelper.clearDBTables();
    }


    @Test
    void positiveScriptBankApprovalTheRequest() {
        PaymentPage paymentPage = new PaymentPage();
        paymentPage.inputData(DataHelper.getApproveCardNumber(), DataHelper.getMonth(),
                DataHelper.getYear(1), DataHelper.getOwnerEng(), DataHelper.getCvc());
        paymentPage.send();
        paymentPage.alertMassageApprove();

        String expectedStatus = "APPROVED";

        assertNotNull(SQLHelper.getTransactionIdFromPaymentEntity());
        assertNotNull(SQLHelper.getPaymentIdFromOrderEntity());
        assertEquals(expectedStatus, SQLHelper.getStatusFromPaymentEntity());
        assertEquals(SQLHelper.getTransactionIdFromPaymentEntity(), SQLHelper.getPaymentIdFromOrderEntity());
    }

    @Test
    void negativeScriptSendEmptyForm() {
        PaymentPage paymentPage = new PaymentPage();
        paymentPage.send();
        paymentPage.validationCardNumber("Неверный формат");
        paymentPage.validationMonth("Неверный формат");
        paymentPage.validationYear("Неверный формат");
        paymentPage.validationOwner("Поле обязательно для заполнения");
        paymentPage.validationCvc("Неверный формат");

        assertNull(SQLHelper.getStatusFromPaymentEntity());
    }

    @Test
    void negativeScriptBankDeniedTheRequest() {
        PaymentPage paymentPage = new PaymentPage();
        paymentPage.inputData(DataHelper.getDeclinedCardNumber(), DataHelper.getMonth(),
                DataHelper.getYear(1), DataHelper.getOwnerEng(), DataHelper.getCvc());
        paymentPage.send();
        paymentPage.alertMessageDeclined();

        String expectedStatus = "DECLINED";

        assertNotNull(SQLHelper.getTransactionIdFromPaymentEntity());
        assertNotNull(SQLHelper.getPaymentIdFromOrderEntity());
        assertEquals(expectedStatus, SQLHelper.getStatusFromPaymentEntity());
        assertEquals(SQLHelper.getTransactionIdFromPaymentEntity(), SQLHelper.getPaymentIdFromOrderEntity());
    }

    @Test
    void negativeScriptSendUnregisteredCard() {
        PaymentPage paymentPage = new PaymentPage();
        paymentPage.inputData(DataHelper.getUnregisteredCardNumber(), DataHelper.getMonth(),
                DataHelper.getYear(1), DataHelper.getOwnerEng(), DataHelper.getCvc());
        paymentPage.send();
        paymentPage.alertMessageDeclined();

        String expectedStatus = "DECLINED";

        assertNotNull(SQLHelper.getTransactionIdFromPaymentEntity());
        assertNotNull(SQLHelper.getPaymentIdFromOrderEntity());
        assertEquals(expectedStatus, SQLHelper.getStatusFromPaymentEntity());
        assertEquals(SQLHelper.getTransactionIdFromPaymentEntity(), SQLHelper.getPaymentIdFromOrderEntity());
    }

    @Test
    void negativeScriptEmptyFieldMonth() {
        PaymentPage paymentPage = new PaymentPage();
        paymentPage.inputData(DataHelper.getApproveCardNumber(), DataHelper.emptyField(),
                DataHelper.getYear(1), DataHelper.getOwnerEng(), DataHelper.getCvc());
        paymentPage.send();
        paymentPage.validationMonth("Неверный формат");

        assertNull(SQLHelper.getStatusFromPaymentEntity());
    }


    @Test
    void negativeScriptInvalidValueMonth() {
        PaymentPage paymentPage = new PaymentPage();
        paymentPage.inputData(DataHelper.getApproveCardNumber(), DataHelper.thirteenthMonth(),
                DataHelper.getYear(1), DataHelper.getOwnerEng(), DataHelper.getCvc());
        paymentPage.send();
        paymentPage.validationMonth("Неверно указан срок действия карты");

        assertNull(SQLHelper.getStatusFromPaymentEntity());
    }

    @Test
    void negativeScriptPastMonth() {
        PaymentPage paymentPage = new PaymentPage();
        paymentPage.inputData(DataHelper.getApproveCardNumber(), DataHelper.pastMonth(),
                DataHelper.getYear(0), DataHelper.getOwnerEng(), DataHelper.getCvc());
        paymentPage.send();
        paymentPage.validationMonth("Неверно указан срок действия карты");

        assertNull(SQLHelper.getStatusFromPaymentEntity());
    }

    @Test
    void negativeScriptIncompleteValueMonth() {
        PaymentPage paymentPage = new PaymentPage();
        paymentPage.inputData(DataHelper.getApproveCardNumber(), DataHelper.missingNumber(),
                DataHelper.getYear(1), DataHelper.getOwnerEng(), DataHelper.getCvc());
        paymentPage.send();
        paymentPage.validationMonth("Неверный формат");

        assertNull(SQLHelper.getStatusFromPaymentEntity());
    }

    @Test
    void negativeScriptEmptyFieldYear() {
        PaymentPage paymentPage = new PaymentPage();
        paymentPage.inputData(DataHelper.getApproveCardNumber(), DataHelper.getMonth(),
                DataHelper.emptyField(), DataHelper.getOwnerEng(), DataHelper.getCvc());
        paymentPage.send();
        paymentPage.validationYear("Неверный формат");

        assertNull(SQLHelper.getStatusFromPaymentEntity());
    }

    @Test
    void negativeScriptPastYear() {
        PaymentPage paymentPage = new PaymentPage();
        paymentPage.inputData(DataHelper.getApproveCardNumber(), DataHelper.getMonth(),
                DataHelper.getYear(-1), DataHelper.getOwnerEng(), DataHelper.getCvc());
        paymentPage.send();
        paymentPage.validationYear("Истёк срок действия карты");

        assertNull(SQLHelper.getStatusFromPaymentEntity());
    }

    @Test
    void negativeScriptInvalidValueYear() {
        PaymentPage paymentPage = new PaymentPage();
        paymentPage.inputData(DataHelper.getApproveCardNumber(), DataHelper.getMonth(),
                DataHelper.getYear(6), DataHelper.getOwnerEng(), DataHelper.getCvc());
        paymentPage.send();
        paymentPage.validationYear("Неверно указан срок действия карты");

        assertNull(SQLHelper.getStatusFromPaymentEntity());
    }

    @Test
    void negativeScriptIncompleteValueYear() {
        PaymentPage paymentPage = new PaymentPage();
        paymentPage.inputData(DataHelper.getApproveCardNumber(), DataHelper.getMonth(),
                DataHelper.missingNumber(), DataHelper.getOwnerEng(), DataHelper.getCvc());
        paymentPage.send();
        paymentPage.validationYear("Неверный формат");

        assertNull(SQLHelper.getStatusFromPaymentEntity());
    }

    @Test
    void negativeScriptEmptyFieldOwner() {
        PaymentPage paymentPage = new PaymentPage();
        paymentPage.inputData(DataHelper.getApproveCardNumber(), DataHelper.getMonth(),
                DataHelper.getYear(1), DataHelper.emptyField(), DataHelper.getCvc());
        paymentPage.send();
        paymentPage.validationOwner("Поле обязательно для заполнения");

        assertNull(SQLHelper.getStatusFromPaymentEntity());
    }

    @Test
    void negativeScriptInputNumberInFieldOwner() {
        PaymentPage paymentPage = new PaymentPage();
        paymentPage.inputData(DataHelper.getApproveCardNumber(), DataHelper.getMonth(),
                DataHelper.getYear(1), DataHelper.ownerNameNumber(), DataHelper.getCvc());
        paymentPage.send();
        paymentPage.validationOwner("Неверный формат");

        assertNull(SQLHelper.getStatusFromPaymentEntity());
    }

    @Test
    void negativeScriptInputSymbolInFieldOwner() {
        PaymentPage paymentPage = new PaymentPage();
        paymentPage.inputData(DataHelper.getApproveCardNumber(), DataHelper.getMonth(),
                DataHelper.getYear(1), DataHelper.randomSymbol(), DataHelper.getCvc());
        paymentPage.send();
        paymentPage.validationOwner("Неверный формат");

        assertNull(SQLHelper.getStatusFromPaymentEntity());
    }

    @Test
    void negativeScriptInputCyrillicLettersInFieldOwner() {
        PaymentPage paymentPage = new PaymentPage();
        paymentPage.inputData(DataHelper.getApproveCardNumber(), DataHelper.getMonth(),
                DataHelper.getYear(1), DataHelper.getOwnerRus(), DataHelper.getCvc());
        paymentPage.send();
        paymentPage.validationOwner("Неверный формат");

        assertNull(SQLHelper.getStatusFromPaymentEntity());
    }

    @Test
    void negativeScriptInput28SymbolInFieldOwner() {
        PaymentPage paymentPage = new PaymentPage();
        paymentPage.inputData(DataHelper.getApproveCardNumber(), DataHelper.getMonth(),
                DataHelper.getYear(1), DataHelper.get28SymbolOwner(), DataHelper.getCvc());
        paymentPage.send();
        paymentPage.validationOwner("Неверный формат");

        assertNull(SQLHelper.getStatusFromPaymentEntity());
    }

    @Test
    void negativeScriptEmptyFieldCvc() {
        PaymentPage paymentPage = new PaymentPage();
        paymentPage.inputData(DataHelper.getApproveCardNumber(), DataHelper.getMonth(),
                DataHelper.getYear(1), DataHelper.getOwnerEng(), DataHelper.emptyField());
        paymentPage.send();
        paymentPage.validationCvc("Неверный формат");

        assertNull(SQLHelper.getStatusFromPaymentEntity());
    }

    @Test
    void negativeScriptIncompleteValueCvc() {
        PaymentPage paymentPage = new PaymentPage();
        paymentPage.inputData(DataHelper.getApproveCardNumber(), DataHelper.getMonth(),
                DataHelper.getYear(1), DataHelper.getOwnerEng(), DataHelper.missingNumberCVC());
        paymentPage.send();
        paymentPage.validationCvc("Неверный формат");

        assertNull(SQLHelper.getStatusFromPaymentEntity());
    }

    @Test
    void negativeScriptIncompleteValueCardNumber() {
        PaymentPage paymentPage = new PaymentPage();
        paymentPage.inputData(DataHelper.missingNumberCard(), DataHelper.getMonth(),
                DataHelper.getYear(1), DataHelper.getOwnerEng(), DataHelper.getCvc());
        paymentPage.send();
        paymentPage.validationCardNumber("Неверный формат");

        assertNull(SQLHelper.getStatusFromPaymentEntity());
    }
}
