package com.bullet.costcategorization.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.FluxMessageChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.file.dsl.Files;
import reactor.core.publisher.Flux;

import java.io.File;

@Configuration
public class FileReadingConfiguration {

    @Bean
    public FluxMessageChannel fileInputChannel() {
        return new FluxMessageChannel();
    }

    @Bean
    public IntegrationFlow fileReadingFlow() {
        return IntegrationFlows
                .from(Files.inboundAdapter(new File("."))
                                .patternFilter("*.csv"),
                        e -> e.poller(Pollers.fixedDelay(1000)))
                .channel("fileInputChannel")
                .get();
    }

    @Bean
    public Flux<File> fileFlux() {
        return Flux.from(fileInputChannel())
                .map(message -> (File) message.getPayload());
    }
}
