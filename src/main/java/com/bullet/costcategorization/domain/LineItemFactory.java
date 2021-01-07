package com.bullet.costcategorization.domain;

import lombok.SneakyThrows;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LineItemFactory {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getInstance(Locale.forLanguageTag("nl-NL"));

    @SneakyThrows
    public static LineItem parseStringData(String date,
                                           String description,
                                           String transactionType,
                                           String amount,
                                           String statement) {
        LocalDate parsedDate = LocalDate.parse(date, FORMATTER);
        LineItem.TransactionType parsedTransactionType = LineItem.TransactionType.fromString(transactionType);

        double parsedAmount = NUMBER_FORMAT.parse(amount).doubleValue();

        return new LineItem(parsedDate, description, parsedTransactionType, parsedAmount, statement);
    }
}
