package com.bullet.costcategorization.service;

import com.bullet.costcategorization.domain.Category;
import com.bullet.costcategorization.domain.LineItem;

import java.util.Optional;

@FunctionalInterface
public interface Categorizer {
    Optional<Category> categorize(LineItem lineItem);

    default Categorizer orElse(Categorizer next) {
        return new ChainedCategorizer(this, next);
    }
}
