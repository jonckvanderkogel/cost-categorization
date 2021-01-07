package com.bullet.costcategorization.controller;

import com.bullet.costcategorization.domain.Category;
import com.bullet.costcategorization.domain.LineItem;
import io.vavr.Tuple2;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@RestController
public class LineItemController {
    private final Flux<Tuple2<Category, LineItem>> categorizedLineItemsFlux;

    @GetMapping(value = "/lineItems", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Tuple2<Category, LineItem>> lineItems() {
        return categorizedLineItemsFlux;
    }
}
