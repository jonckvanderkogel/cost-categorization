package com.bullet.costcategorization.service;

import com.bullet.costcategorization.domain.Category;
import com.bullet.costcategorization.domain.LineItem;
import io.vavr.Tuple2;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Predicate;

import static com.bullet.costcategorization.domain.Category.INSURANCE;

public class CategorizationServiceTests {

    @Test
    public void shouldEmitCategorizedLineItem() {
        Categorizer firstCategorizer = (li) -> li.getDescription().equals("Foo") ? Optional.of(Category.MORTGAGE) : Optional.empty();
        Categorizer nextCategorizer = (li) -> li.getDescription().equals("Bar") ? Optional.of(INSURANCE) : Optional.empty();

        var categorizationService = new CategorizationService(firstCategorizer.orElse(nextCategorizer));

        Flux<LineItem> lineItemFlux = Flux.just(new LineItem(
                LocalDate.of(2021, 1, 20),
                "Bar",
                LineItem.TransactionType.AF,
                12.34,
                "FooBar"
        ));

        Predicate<Tuple2<Category, LineItem>> categoryPredicate = (t) -> INSURANCE.equals(t._1());

        StepVerifier
                .create(categorizationService.categorize(lineItemFlux))
                .expectNextMatches(categoryPredicate)
                .expectComplete()
                .verify();
    }
}
