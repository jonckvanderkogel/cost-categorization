package com.bullet.costcategorization.service;

import com.bullet.costcategorization.domain.Category;
import com.bullet.costcategorization.domain.LineItem;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChainedTransformersTests {

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
        Transformer<LineItem, Category> firstTransformer = (li) -> Optional.empty();
        Transformer<LineItem, Category> nextTransformer = (li) -> Optional.of(Category.INSURANCE);

        var chainedTransformer = firstTransformer.orElse(nextTransformer);

        var result = chainedTransformer.transform(produceLineItem(), (li) -> Category.REST);

        assertEquals(Category.INSURANCE, result);
    }

    @Test
    public void shouldIgnoreNextIfFirstYieldsResult() {
        Transformer<LineItem, Category> firstTransformer = (li) -> Optional.of(Category.MORTGAGE);
        Transformer<LineItem, Category> nextTransformer = (li) -> Optional.of(Category.INSURANCE);

        var chainedTransformer = firstTransformer.orElse(nextTransformer);

        var result = chainedTransformer.transform(produceLineItem(), (li) -> Category.REST);

        assertEquals(Category.MORTGAGE, result);
    }
}
