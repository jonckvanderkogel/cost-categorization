package com.bullet.costcategorization.domain;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;
import com.opencsv.bean.CsvNumber;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

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
public class LineItem {
    @CsvBindByPosition(position = 0)
    @CsvDate("yyyyMMdd")
    private LocalDate date;

    @CsvBindByPosition(position = 1)
    private String description;

    @CsvBindByPosition(position = 5)
    private TransactionType transactionType;

    @CsvBindByPosition(position = 6, locale = "nl-NL")
    @CsvNumber("#,##")
    private double amount;

    @CsvBindByPosition(position = 8)
    private String statement;

    public enum TransactionType {
        AF,BIJ;
    }
}
