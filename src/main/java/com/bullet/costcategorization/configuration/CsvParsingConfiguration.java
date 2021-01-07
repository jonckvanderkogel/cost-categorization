package com.bullet.costcategorization.configuration;

import com.bullet.costcategorization.domain.LineItem;
import com.bullet.costcategorization.service.CsvParseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

@Configuration
public class CsvParsingConfiguration {

    @Bean
    public Flux<LineItem> parseRawLines(@Autowired TransactionLineCsvRoute camelRoute) {
        return new CsvParseService().parseRawLines(camelRoute.getRawLinesFlux());
    }
}
