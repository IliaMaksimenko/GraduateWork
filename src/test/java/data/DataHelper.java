package data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.*;

public class DataHelper {

    static Faker fakerRus = new Faker(new Locale("ru"));
    static Faker fakerEng = new Faker(new Locale("en"));

    private DataHelper() {
    }

    public static String getApproveCardNumber() {
        return "4444 4444 4444 4441";
    }

    public static String getDeclinedCardNumber() {
        return "4444 4444 4444 4442";
    }

    public static String getUnregisteredCardNumber() {
        return "4444 4444 4444 4444";
    }

    public static String getMonth() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("MM"));
    }

    public static String getYear(int shift) {
        return (LocalDate.now().plusYears(shift).format(DateTimeFormatter.ofPattern("yyyy"))).substring(2);
    }

    public static String getOwnerRus() {
        return fakerRus.name().fullName();
    }

    public static String getOwnerEng() {
        return fakerEng.name().fullName();
    }

    public static String getCvc() {
        return String.valueOf(fakerRus.number().numberBetween(100, 999));
    }

    public static String get28SymbolOwner() {
        int leftLimit = 97;
        int rightLimit = 122;
        int targetStringLength = 28;
        Random random = new Random();
        return random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
