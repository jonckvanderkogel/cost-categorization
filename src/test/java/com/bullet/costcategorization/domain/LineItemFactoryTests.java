package com.bullet.costcategorization.domain;

import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import static com.bullet.costcategorization.domain.LineItem.TransactionType.AF;
import static org.junit.jupiter.api.Assertions.*;

public class LineItemFactoryTests {
    @Test
    public void shouldParseValidData() {
        LineItem li = LineItemFactory.parseStringData(
                "20210125",
                "Foo",
                "Af",
                "12,34",
                "Bar");

        assertEquals(LocalDate.of(2021, 1, 25), li.getDate());
        assertEquals(AF, li.getTransactionType());
        assertEquals(12.34, li.getAmount());
    }

    @Test
    public void expectExceptionOnInvalidDateFormat() {
        DateTimeParseException exception = assertThrows(
                DateTimeParseException.class,
                () -> LineItemFactory.parseStringData(
                        "foo",
                        "Foo",
                        "bla",
                        "12,34",
                        "Bar")
        );

        String expectedMessage = "Text 'foo' could not be parsed at index 0";

        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    public void expectExceptionOnInvalidNumberFormat() {
        ParseException exception = assertThrows(
                ParseException.class,
                () -> LineItemFactory.parseStringData(
                        "20210125",
                        "Foo",
                        "bla",
                        "foo",
                        "Bar")
        );

        String expectedMessage = "Unparseable number: \"foo\"";

        assertTrue(exception.getMessage().contains(expectedMessage));
    }
}
