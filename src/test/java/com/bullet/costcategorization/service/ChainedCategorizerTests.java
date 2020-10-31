package com.bullet.costcategorization.service;

import com.bullet.costcategorization.domain.Category;
import com.bullet.costcategorization.domain.LineItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class ChainedCategorizerTests {

    @Test
    public void shouldTakeNextWhenFirstReturnsEmpty() {
        Categorizer firstCategorizer = (li) -> Optional.empty();
        Categorizer nextCategorizer = (li) -> Optional.of(Category.INSURANCE);

        var chainedCategorizer = firstCategorizer.orElse(nextCategorizer);

        var result = chainedCategorizer.categorize(new LineItem());

        Assertions.assertEquals(Category.INSURANCE, result.get());
    }

    @Test
    public void shouldIgnoreNextIfFirstYieldsResult() {
        Categorizer firstCategorizer = (li) -> Optional.of(Category.MORTGAGE);
        Categorizer nextCategorizer = (li) -> Optional.of(Category.INSURANCE);

        var chainedCategorizer = firstCategorizer.orElse(nextCategorizer);

        var result = chainedCategorizer.categorize(new LineItem());

        Assertions.assertEquals(Category.MORTGAGE, result.get());
    }
}
