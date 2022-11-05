package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataHelper;
import data.SQLHelper;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.*;

import page.DayTrip;

import java.sql.SQLException;

public class CardPaymentTest {

    private final DayTrip cardPayment = new DayTrip();

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
        open("http://localhost:8080");
        cardPayment.debitPurchase();
    }

    @AfterEach
    void clearAll() {
        SQLHelper.clearDBTables();
    }


    @Test
    void positiveScriptBankApprovalTheRequest() throws SQLException {
        cardPayment.inputData(DataHelper.getApproveCardNumber(), DataHelper.getMonth(),
                DataHelper.getYear(1), DataHelper.getOwnerEng(), DataHelper.getCvc());
        cardPayment.send();
        cardPayment.alertMassageApprove();

        String expectedStatus = "APPROVED";

        assertNotNull(SQLHelper.getTransactionIdFromPaymentEntity());
        assertNotNull(SQLHelper.getPaymentIdFromOrderEntity());
        assertEquals(expectedStatus, SQLHelper.getStatusFromPaymentEntity());
        assertEquals(SQLHelper.getTransactionIdFromPaymentEntity(), SQLHelper.getPaymentIdFromOrderEntity());
    }

    @Test
    void negativeScriptSendEmptyForm() throws SQLException {
        cardPayment.send();
        cardPayment.validationCardNumber("Неверный формат");
        cardPayment.validationMonth("Неверный формат");
        cardPayment.validationYear("Неверный формат");
        cardPayment.validationOwner("Поле обязательно для заполнения");
        cardPayment.validationCvc("Неверный формат");

        assertNull(SQLHelper.getStatusFromPaymentEntity());
    }

    @Test
    void negativeScriptBankDeniedTheRequest() throws SQLException {
        cardPayment.inputData(DataHelper.getDeclinedCardNumber(), DataHelper.getMonth(),
                DataHelper.getYear(1), DataHelper.getOwnerEng(), DataHelper.getCvc());
        cardPayment.send();
        cardPayment.alertMessageDeclined();

        String expectedStatus = "DECLINED";

        assertNotNull(SQLHelper.getTransactionIdFromPaymentEntity());
        assertNotNull(SQLHelper.getPaymentIdFromOrderEntity());
        assertEquals(expectedStatus, SQLHelper.getStatusFromPaymentEntity());
        assertEquals(SQLHelper.getTransactionIdFromPaymentEntity(), SQLHelper.getPaymentIdFromOrderEntity());
    }

    @Test
    void negativeScriptSendUnregisteredCard() throws SQLException {
        cardPayment.inputData(DataHelper.getUnregisteredCardNumber(), DataHelper.getMonth(),
                DataHelper.getYear(1), DataHelper.getOwnerEng(), DataHelper.getCvc());
        cardPayment.send();
        cardPayment.alertMessageDeclined();

        String expectedStatus = "DECLINED";

        assertNotNull(SQLHelper.getTransactionIdFromPaymentEntity());
        assertNotNull(SQLHelper.getPaymentIdFromOrderEntity());
        assertEquals(expectedStatus, SQLHelper.getStatusFromPaymentEntity());
        assertEquals(SQLHelper.getTransactionIdFromPaymentEntity(), SQLHelper.getPaymentIdFromOrderEntity());
    }

    @Test
    void negativeScriptEmptyFieldMonth() throws SQLException {
        cardPayment.inputData(DataHelper.getApproveCardNumber(), "",
                DataHelper.getYear(1), DataHelper.getOwnerEng(), DataHelper.getCvc());
        cardPayment.send();
        cardPayment.validationMonth("Неверный формат");

        assertNull(SQLHelper.getStatusFromPaymentEntity());
    }


    @Test
    void negativeScriptInvalidValueMonth() throws SQLException {
        cardPayment.inputData(DataHelper.getApproveCardNumber(), "13",
                DataHelper.getYear(1), DataHelper.getOwnerEng(), DataHelper.getCvc());
        cardPayment.send();
        cardPayment.validationMonth("Неверно указан срок действия карты");

        assertNull(SQLHelper.getStatusFromPaymentEntity());
    }

    @Test
    void negativeScriptPastMonth() throws SQLException {
        cardPayment.inputData(DataHelper.getApproveCardNumber(), "09",
                DataHelper.getYear(0), DataHelper.getOwnerEng(), DataHelper.getCvc());
        cardPayment.send();
        cardPayment.validationMonth("Неверно указан срок действия карты");

        assertNull(SQLHelper.getStatusFromPaymentEntity());
    }

    @Test
    void negativeScriptIncompleteValueMonth() throws SQLException {
        cardPayment.inputData(DataHelper.getApproveCardNumber(), "1",
                DataHelper.getYear(1), DataHelper.getOwnerEng(), DataHelper.getCvc());
        cardPayment.send();
        cardPayment.validationMonth("Неверный формат");

        assertNull(SQLHelper.getStatusFromPaymentEntity());
    }

    @Test
    void negativeScriptEmptyFieldYear() throws SQLException {
        cardPayment.inputData(DataHelper.getApproveCardNumber(), DataHelper.getMonth(),
                "", DataHelper.getOwnerEng(), DataHelper.getCvc());
        cardPayment.send();
        cardPayment.validationYear("Неверный формат");

        assertNull(SQLHelper.getStatusFromPaymentEntity());
    }

    @Test
    void negativeScriptPastYear() throws SQLException {
        cardPayment.inputData(DataHelper.getApproveCardNumber(), DataHelper.getMonth(),
                DataHelper.getYear(-1), DataHelper.getOwnerEng(), DataHelper.getCvc());
        cardPayment.send();
        cardPayment.validationYear("Истёк срок действия карты");

        assertNull(SQLHelper.getStatusFromPaymentEntity());
    }

    @Test
    void negativeScriptInvalidValueYear() throws SQLException {
        cardPayment.inputData(DataHelper.getApproveCardNumber(), DataHelper.getMonth(),
                DataHelper.getYear(6), DataHelper.getOwnerEng(), DataHelper.getCvc());
        cardPayment.send();
        cardPayment.validationYear("Неверно указан срок действия карты");

        assertNull(SQLHelper.getStatusFromPaymentEntity());
    }

    @Test
    void negativeScriptIncompleteValueYear() throws SQLException {
        cardPayment.inputData(DataHelper.getApproveCardNumber(), DataHelper.getMonth(),
                "2", DataHelper.getOwnerEng(), DataHelper.getCvc());
        cardPayment.send();
        cardPayment.validationYear("Неверный формат");

        assertNull(SQLHelper.getStatusFromPaymentEntity());
    }

    @Test
    void negativeScriptEmptyFieldOwner() throws SQLException {
        cardPayment.inputData(DataHelper.getApproveCardNumber(), DataHelper.getMonth(),
                DataHelper.getYear(1), "", DataHelper.getCvc());
        cardPayment.send();
        cardPayment.validationOwner("Поле обязательно для заполнения");

        assertNull(SQLHelper.getStatusFromPaymentEntity());
    }

    @Test
    void negativeScriptInputNumberInFieldOwner() throws SQLException {
        cardPayment.inputData(DataHelper.getApproveCardNumber(), DataHelper.getMonth(),
                DataHelper.getYear(1), "4", DataHelper.getCvc());
        cardPayment.send();
        cardPayment.validationOwner("Неверный формат");

        assertNull(SQLHelper.getStatusFromPaymentEntity());
    }

    @Test
    void negativeScriptInputSymbolInFieldOwner() throws SQLException {
        cardPayment.inputData(DataHelper.getApproveCardNumber(), DataHelper.getMonth(),
                DataHelper.getYear(1), "!№;%:?()/+-.,[]", DataHelper.getCvc());
        cardPayment.send();
        cardPayment.validationOwner("Неверный формат");

        assertNull(SQLHelper.getStatusFromPaymentEntity());
    }

    @Test
    void negativeScriptInputCyrillicLettersInFieldOwner() throws SQLException {
        cardPayment.inputData(DataHelper.getApproveCardNumber(), DataHelper.getMonth(),
                DataHelper.getYear(1), DataHelper.getOwnerRus(), DataHelper.getCvc());
        cardPayment.send();
        cardPayment.validationOwner("Неверный формат");

        assertNull(SQLHelper.getStatusFromPaymentEntity());
    }

    @Test
    void negativeScriptInput28SymbolInFieldOwner() throws SQLException {
        cardPayment.inputData(DataHelper.getApproveCardNumber(), DataHelper.getMonth(),
                DataHelper.getYear(1), DataHelper.get28SymbolOwner(), DataHelper.getCvc());
        cardPayment.send();
        cardPayment.validationOwner("Неверный формат");

        assertNull(SQLHelper.getStatusFromPaymentEntity());
    }

    @Test
    void negativeScriptEmptyFieldCvc() throws SQLException {
        cardPayment.inputData(DataHelper.getApproveCardNumber(), DataHelper.getMonth(),
                DataHelper.getYear(1), DataHelper.getOwnerEng(), "");
        cardPayment.send();
        cardPayment.validationCvc("Неверный формат");

        assertNull(SQLHelper.getStatusFromPaymentEntity());
    }

    @Test
    void negativeScriptIncompleteValueCvc() throws SQLException {
        cardPayment.inputData(DataHelper.getApproveCardNumber(), DataHelper.getMonth(),
                DataHelper.getYear(1), DataHelper.getOwnerEng(), "22");
        cardPayment.send();
        cardPayment.validationCvc("Неверный формат");

        assertNull(SQLHelper.getStatusFromPaymentEntity());
    }

    @Test
    void negativeScriptIncompleteValueCardNumber() throws SQLException {
        cardPayment.inputData("4444 4444 4444 444", DataHelper.getMonth(),
                DataHelper.getYear(1), DataHelper.getOwnerEng(), DataHelper.getCvc());
        cardPayment.send();
        cardPayment.validationCardNumber("Неверный формат");

        assertNull(SQLHelper.getStatusFromPaymentEntity());
    }
}
