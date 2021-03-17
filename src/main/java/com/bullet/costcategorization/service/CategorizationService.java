package com.bullet.costcategorization.service;

import com.bullet.costcategorization.domain.Category;
import com.bullet.costcategorization.domain.LineItem;
import io.vavr.Tuple2;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class CategorizationService {
    private final Transformer<LineItem, Category> transformer;

    public Flux<Tuple2<Category, LineItem>> categorize(Flux<LineItem> lineItemFlux) {
        return lineItemFlux
                .map(li -> new Tuple2<>(transformer.transform(li, (l) -> Category.REST), li));
    }
}
