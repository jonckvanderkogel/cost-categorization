package com.bullet.costcategorization.service;

import com.bullet.costcategorization.domain.LineItem;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.function.Predicate;

import static com.bullet.costcategorization.domain.LineItem.TransactionType.AF;

public class CsvParseServiceTests {
    @Test
    public void shouldParseValidLine() {
        String validLine = "\"20201029\";\"Bar\";\"NL12INGB000123456\";\"\";\"BA\";\"Af\";\"12,34\";\"Betaalautomaat\";\"FooBar\";\"\"";

        Flux<String> rawLinesFlux = Flux.just(validLine);

        Predicate<LineItem> lineItemPredicate = (li) -> AF.equals(li.getTransactionType())
                && Double.valueOf(12.34).equals(li.getAmount())
                && LocalDate.of(2020, 10, 29).equals(li.getDate());

        StepVerifier
                .create(new CsvParseService().parseRawLines(rawLinesFlux))
                .expectNextMatches(lineItemPredicate)
                .expectComplete()
                .verify();
    }
}
