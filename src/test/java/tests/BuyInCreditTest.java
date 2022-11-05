package tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataHelper;
import data.SQLHelper;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import page.DayTrip;

import java.sql.SQLException;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.*;

public class BuyInCreditTest {
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
        cardPayment.creditPurchase();
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

        assertNotNull(SQLHelper.getBankIdFromCreditEntity());
        assertNotNull(SQLHelper.getCreditIdFromOrderEntity());
        assertEquals(expectedStatus, SQLHelper.getStatusFromCreditRequestEntity());
        assertEquals(SQLHelper.getBankIdFromCreditEntity(), SQLHelper.getCreditIdFromOrderEntity());
    }

    @Test
    void negativeScriptSendEmptyForm() throws SQLException {
        cardPayment.send();
        cardPayment.validationCardNumber("Неверный формат");
        cardPayment.validationMonth("Неверный формат");
        cardPayment.validationYear("Неверный формат");
        cardPayment.validationOwner("Поле обязательно для заполнения");
        cardPayment.validationCvc("Неверный формат");

        assertNull(SQLHelper.getStatusFromCreditRequestEntity());
    }

    @Test
    void negativeScriptBankDeniedTheRequest() throws SQLException {
        cardPayment.inputData(DataHelper.getDeclinedCardNumber(), DataHelper.getMonth(),
                DataHelper.getYear(1), DataHelper.getOwnerEng(), DataHelper.getCvc());
        cardPayment.send();
        cardPayment.alertMessageDeclined();

        String expectedStatus = "DECLINED";

        assertEquals(expectedStatus, SQLHelper.getStatusFromCreditRequestEntity());
        assertEquals(SQLHelper.getBankIdFromCreditEntity(), SQLHelper.getCreditIdFromOrderEntity());
    }

    @Test
    void negativeScriptSendUnregisteredCard() throws SQLException {
        cardPayment.inputData(DataHelper.getUnregisteredCardNumber(), DataHelper.getMonth(),
                DataHelper.getYear(1), DataHelper.getOwnerEng(), DataHelper.getCvc());
        cardPayment.send();
        cardPayment.alertMessageDeclined();

        String expectedStatus = "DECLINED";

        assertEquals(expectedStatus, SQLHelper.getStatusFromCreditRequestEntity());
        assertEquals(SQLHelper.getBankIdFromCreditEntity(), SQLHelper.getCreditIdFromOrderEntity());
    }

    @Test
    void negativeScriptEmptyFieldMonth() throws SQLException {
        cardPayment.inputData(DataHelper.getApproveCardNumber(), "",
                DataHelper.getYear(1), DataHelper.getOwnerEng(), DataHelper.getCvc());
        cardPayment.send();
        cardPayment.validationMonth("Неверный формат");

        assertNull(SQLHelper.getStatusFromCreditRequestEntity());
    }


    @Test
    void negativeScriptInvalidValueMonth() throws SQLException {
        cardPayment.inputData(DataHelper.getApproveCardNumber(), "13",
                DataHelper.getYear(1), DataHelper.getOwnerEng(), DataHelper.getCvc());
        cardPayment.send();
        cardPayment.validationMonth("Неверно указан срок действия карты");

        assertNull(SQLHelper.getStatusFromCreditRequestEntity());
    }

    @Test
    void negativeScriptPastMonth() throws SQLException {
        cardPayment.inputData(DataHelper.getApproveCardNumber(), "09",
                DataHelper.getYear(0), DataHelper.getOwnerEng(), DataHelper.getCvc());
        cardPayment.send();
        cardPayment.validationMonth("Неверно указан срок действия карты");

        assertNull(SQLHelper.getStatusFromCreditRequestEntity());
    }

    @Test
    void negativeScriptIncompleteValueMonth() throws SQLException {
        cardPayment.inputData(DataHelper.getApproveCardNumber(), "1",
                DataHelper.getYear(1), DataHelper.getOwnerEng(), DataHelper.getCvc());
        cardPayment.send();
        cardPayment.validationMonth("Неверный формат");

        assertNull(SQLHelper.getStatusFromCreditRequestEntity());
    }

    @Test
    void negativeScriptEmptyFieldYear() throws SQLException {
        cardPayment.inputData(DataHelper.getApproveCardNumber(), DataHelper.getMonth(),
                "", DataHelper.getOwnerEng(), DataHelper.getCvc());
        cardPayment.send();
        cardPayment.validationYear("Неверный формат");

        assertNull(SQLHelper.getStatusFromCreditRequestEntity());
    }

    @Test
    void negativeScriptPastYear() throws SQLException {
        cardPayment.inputData(DataHelper.getApproveCardNumber(), DataHelper.getMonth(),
                DataHelper.getYear(-1), DataHelper.getOwnerEng(), DataHelper.getCvc());
        cardPayment.send();
        cardPayment.validationYear("Истёк срок действия карты");

        assertNull(SQLHelper.getStatusFromCreditRequestEntity());
    }

    @Test
    void negativeScriptInvalidValueYear() throws SQLException {
        cardPayment.inputData(DataHelper.getApproveCardNumber(), DataHelper.getMonth(),
                DataHelper.getYear(6), DataHelper.getOwnerEng(), DataHelper.getCvc());
        cardPayment.send();
        cardPayment.validationYear("Неверно указан срок действия карты");

        assertNull(SQLHelper.getStatusFromCreditRequestEntity());
    }

    @Test
    void negativeScriptIncompleteValueYear() throws SQLException {
        cardPayment.inputData(DataHelper.getApproveCardNumber(), DataHelper.getMonth(),
                "2", DataHelper.getOwnerEng(), DataHelper.getCvc());
        cardPayment.send();
        cardPayment.validationYear("Неверный формат");

        assertNull(SQLHelper.getStatusFromCreditRequestEntity());
    }

    @Test
    void negativeScriptEmptyFieldOwner() throws SQLException {
        cardPayment.inputData(DataHelper.getApproveCardNumber(), DataHelper.getMonth(),
                DataHelper.getYear(1), "", DataHelper.getCvc());
        cardPayment.send();
        cardPayment.validationOwner("Поле обязательно для заполнения");

        assertNull(SQLHelper.getStatusFromCreditRequestEntity());
    }

    @Test
    void negativeScriptInputNumberInFieldOwner() throws SQLException {
        cardPayment.inputData(DataHelper.getApproveCardNumber(), DataHelper.getMonth(),
                DataHelper.getYear(1), "4", DataHelper.getCvc());
        cardPayment.send();
        cardPayment.validationOwner("Неверный формат");

        assertNull(SQLHelper.getStatusFromCreditRequestEntity());
    }

    @Test
    void negativeScriptInputSymbolInFieldOwner() throws SQLException {
        cardPayment.inputData(DataHelper.getApproveCardNumber(), DataHelper.getMonth(),
                DataHelper.getYear(1), "!№;%:?()/+-.,[]", DataHelper.getCvc());
        cardPayment.send();
        cardPayment.validationOwner("Неверный формат");

        assertNull(SQLHelper.getStatusFromCreditRequestEntity());
    }

    @Test
    void negativeScriptInputCyrillicLettersInFieldOwner() throws SQLException {
        cardPayment.inputData(DataHelper.getApproveCardNumber(), DataHelper.getMonth(),
                DataHelper.getYear(1), DataHelper.getOwnerRus(), DataHelper.getCvc());
        cardPayment.send();
        cardPayment.validationOwner("Неверный формат");

        assertNull(SQLHelper.getStatusFromCreditRequestEntity());
    }

    @Test
    void negativeScriptInput28SymbolInFieldOwner() throws SQLException {
        cardPayment.inputData(DataHelper.getApproveCardNumber(), DataHelper.getMonth(),
                DataHelper.getYear(1), DataHelper.get28SymbolOwner(), DataHelper.getCvc());
        cardPayment.send();
        cardPayment.validationOwner("Неверный формат");

        assertNull(SQLHelper.getStatusFromCreditRequestEntity());
    }

    @Test
    void negativeScriptEmptyFieldCvc() throws SQLException {
        cardPayment.inputData(DataHelper.getApproveCardNumber(), DataHelper.getMonth(),
                DataHelper.getYear(1), DataHelper.getOwnerEng(), "");
        cardPayment.send();
        cardPayment.validationCvc("Неверный формат");

        assertNull(SQLHelper.getStatusFromCreditRequestEntity());
    }

    @Test
    void negativeScriptIncompleteValueCvc() throws SQLException {
        cardPayment.inputData(DataHelper.getApproveCardNumber(), DataHelper.getMonth(),
                DataHelper.getYear(1), DataHelper.getOwnerEng(), "22");
        cardPayment.send();
        cardPayment.validationCvc("Неверный формат");

        assertNull(SQLHelper.getStatusFromCreditRequestEntity());
    }

    @Test
    void negativeScriptIncompleteValueCardNumber() throws SQLException {
        cardPayment.inputData("4444 4444 4444 444", DataHelper.getMonth(),
                DataHelper.getYear(1), DataHelper.getOwnerEng(), DataHelper.getCvc());
        cardPayment.send();
        cardPayment.validationCardNumber("Неверный формат");

        assertNull(SQLHelper.getStatusFromCreditRequestEntity());
    }
}

