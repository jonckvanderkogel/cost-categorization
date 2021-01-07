package com.bullet.costcategorization.service;

import com.bullet.costcategorization.domain.Category;
import com.bullet.costcategorization.domain.LineItem;
import io.vavr.Tuple2;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class CategorizationService {
    private final Categorizer categorizer;

    public Flux<Tuple2<Category, LineItem>> categorize(Flux<LineItem> lineItemFlux) {
        return lineItemFlux
                .map(li -> new Tuple2<>(categorizer.categorize(li), li));
    }
}
