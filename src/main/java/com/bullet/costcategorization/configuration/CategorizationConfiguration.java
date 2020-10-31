package com.bullet.costcategorization.configuration;

import com.bullet.costcategorization.domain.Category;
import com.bullet.costcategorization.domain.LineItem;
import com.bullet.costcategorization.service.CategorizationService;
import com.bullet.costcategorization.service.Categorizer;
import com.bullet.costcategorization.service.DefiniteCategorizer;
import io.vavr.Tuple2;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;

import java.io.File;

@Configuration
public class CategorizationConfiguration {

    /**
     *
     * @param filePublisher the publisher that sends messages to the CategorizationService
     * @param chainedCategorizer the Categorizers. You need to define your own chained Categorizers bean that fits your
     *                           needs. Not publishing mine on Github for privacy reasons.
     * @return a Flux with a tuple containing a categorized LineItem
     */
    @Bean
    public Flux<Tuple2<Category, LineItem>> getCategorizedLineItemFlux(
            @Autowired Publisher<Message<File>> filePublisher,
            @Autowired Categorizer chainedCategorizer) {
        return new CategorizationService(definiteCategorizer(chainedCategorizer), filePublisher).categorize();
    }


    private DefiniteCategorizer definiteCategorizer(Categorizer chainedCategorizer) {
        return new DefiniteCategorizer(chainedCategorizer);
    }
}
