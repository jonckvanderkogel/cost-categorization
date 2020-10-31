package com.bullet.costcategorization.service;

import com.bullet.costcategorization.domain.Category;
import com.bullet.costcategorization.domain.LineItem;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefiniteCategorizer {
    private final Categorizer categorizer;

    public Category categorize(LineItem li) {
        return categorizer
                .categorize(li)
                .orElseGet(() -> Category.REST);
    }
}
