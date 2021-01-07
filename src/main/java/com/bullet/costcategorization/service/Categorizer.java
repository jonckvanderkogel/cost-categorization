package com.bullet.costcategorization.service;

import com.bullet.costcategorization.domain.Category;
import com.bullet.costcategorization.domain.LineItem;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@FunctionalInterface
public interface Categorizer {
    Optional<Category> categorizeMaybe(LineItem lineItem);

    default Category categorize(LineItem li) {
        return categorizeMaybe(li)
                .orElseGet(() -> Category.REST);
    }

    default Categorizer orElse(Categorizer next) {
        return new ChainedCategorizer(this, next);
    }

    @RequiredArgsConstructor
    class ChainedCategorizer implements Categorizer {
        private final Categorizer current;
        private final Categorizer next;

        @Override
        public Optional<Category> categorizeMaybe(LineItem lineItem) {
            return current
                    .categorizeMaybe(lineItem)
                    .or(() -> next.categorizeMaybe(lineItem));
        }
    }
}
