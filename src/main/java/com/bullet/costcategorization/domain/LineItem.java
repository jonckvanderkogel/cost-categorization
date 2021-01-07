package com.bullet.costcategorization.domain;

import io.vavr.Lazy;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

/*
0 - "Datum"
1 - "Naam / Omschrijving"
2 - "Rekening"
3 - "Tegenrekening"
4 - "Code"
5 - "Af Bij"
6 - "Bedrag (EUR)"
7 - "Mutatiesoort"
8 - "Mededelingen"
9 - "Saldo na mutatie"
10 - "Tag"
 */
@ToString
@Getter
@RequiredArgsConstructor
public class LineItem {
    private final LocalDate date;
    private final String description;
    private final TransactionType transactionType;
    private final double amount;
    private final String statement;

    public enum TransactionType {
        AF,BIJ,UNKNOWN;

        private static final Lazy<Map<String, TransactionType>> stringToEnum = Lazy.of(() -> Stream.of(values())
                .collect(toMap(Object::toString, Function.identity())));

        public static TransactionType fromString(final String name) {
            return stringToEnum.get().getOrDefault(name.toUpperCase(), UNKNOWN);
        }
    }
}
