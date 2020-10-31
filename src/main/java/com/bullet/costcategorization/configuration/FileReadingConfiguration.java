package com.bullet.costcategorization.configuration;

import org.reactivestreams.Publisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.file.dsl.Files;
import org.springframework.messaging.Message;

import java.io.File;

@Configuration
public class FileReadingConfiguration {

    @Bean
    public Publisher<Message<File>> fileReadingFlow() {
        return IntegrationFlows
                .from(Files.inboundAdapter(new File("."))
                                .patternFilter("*.csv"),
                        e -> e.poller(Pollers.fixedDelay(1000)))
                .channel("processFileChannel")
                .toReactivePublisher();
    }
}
