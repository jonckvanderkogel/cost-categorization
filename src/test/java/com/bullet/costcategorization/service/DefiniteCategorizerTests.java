package com.bullet.costcategorization.service;

import com.bullet.costcategorization.domain.Category;
import com.bullet.costcategorization.domain.LineItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class DefiniteCategorizerTests {
    @Test
    public void shouldGetRestIfOthersDoNotYieldResult() {
        Categorizer firstCategorizer = (li) -> Optional.empty();
        Categorizer nextCategorizer = (li) -> Optional.empty();
        var definiteCategorizer = new DefiniteCategorizer(firstCategorizer.orElse(nextCategorizer));

        var result = definiteCategorizer.categorize(new LineItem());

        Assertions.assertEquals(Category.REST, result);
    }
}
