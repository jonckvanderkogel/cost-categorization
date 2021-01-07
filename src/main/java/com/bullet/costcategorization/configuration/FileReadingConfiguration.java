package com.bullet.costcategorization.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

@Configuration
public class FileReadingConfiguration {
    @Bean
    public Flux<String> rawLinesFlux(@Autowired TransactionLineCsvRoute camelRoute) {
        return camelRoute.getRawLinesFlux();
    }
}
