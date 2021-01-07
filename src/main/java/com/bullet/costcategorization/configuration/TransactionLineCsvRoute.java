package com.bullet.costcategorization.configuration;

import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.reactive.streams.api.CamelReactiveStreamsService;
import org.apache.camel.impl.ThrottlingInflightRoutePolicy;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class TransactionLineCsvRoute extends RouteBuilder {
    private final CamelReactiveStreamsService camelRs;

    @Override
    public void configure() {
        ThrottlingInflightRoutePolicy policy = new ThrottlingInflightRoutePolicy();
        policy.setMaxInflightExchanges(10);

        from("file:input/?include=.*\\.csv&move=successImport&moveFailed=failImport")
                .routePolicy(policy)
                .split().tokenize("\n", 1)
                .streaming()
                .choice()
                .when(simple("${property.CamelSplitIndex} > 0"))
                .to("reactive-streams:rawLines")
                .otherwise()
                .stop();
    }

    public Flux<String> getRawLinesFlux() {
        Publisher<String> lineItems = camelRs.fromStream("rawLines", String.class);

        return Flux.from(lineItems);
    }
}
