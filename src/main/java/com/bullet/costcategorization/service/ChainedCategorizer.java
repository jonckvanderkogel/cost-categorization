package com.bullet.costcategorization.service;

import com.bullet.costcategorization.domain.Category;
import com.bullet.costcategorization.domain.LineItem;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class ChainedCategorizer implements Categorizer {
    private final Categorizer current;
    private final Categorizer next;

    @Override
    public Optional<Category> categorize(LineItem lineItem) {
        return current
                .categorize(lineItem)
                .or(() -> next.categorize(lineItem));
    }
}
