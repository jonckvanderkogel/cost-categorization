package com.bullet.costcategorization.service;

import com.bullet.costcategorization.domain.Category;
import com.bullet.costcategorization.domain.LineItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

public class ChainedCategorizerTests {

    private LineItem produceLineItem() {
        return new LineItem(
                LocalDate.of(2021, 1, 7),
                "foo bar",
                LineItem.TransactionType.BIJ,
                1.5,
                "bar foo"
        );
    }

    @Test
    public void shouldTakeNextWhenFirstReturnsEmpty() {
        Categorizer firstCategorizer = (li) -> Optional.empty();
        Categorizer nextCategorizer = (li) -> Optional.of(Category.INSURANCE);

        var chainedCategorizer = firstCategorizer.orElse(nextCategorizer);

        var result = chainedCategorizer.categorize(produceLineItem());

        Assertions.assertEquals(Category.INSURANCE, result);
    }

    @Test
    public void shouldIgnoreNextIfFirstYieldsResult() {
        Categorizer firstCategorizer = (li) -> Optional.of(Category.MORTGAGE);
        Categorizer nextCategorizer = (li) -> Optional.of(Category.INSURANCE);

        var chainedCategorizer = firstCategorizer.orElse(nextCategorizer);

        var result = chainedCategorizer.categorize(produceLineItem());

        Assertions.assertEquals(Category.MORTGAGE, result);
    }
}
