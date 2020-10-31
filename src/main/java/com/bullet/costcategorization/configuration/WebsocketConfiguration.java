package com.bullet.costcategorization.configuration;

import com.bullet.costcategorization.domain.Category;
import com.bullet.costcategorization.domain.LineItem;
import com.bullet.costcategorization.websocket.LineItemHandler;
import io.vavr.Tuple2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import reactor.core.publisher.Flux;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class WebsocketConfiguration {

    @Bean
    public HandlerMapping webSocketHandlerMapping(@Autowired Flux<Tuple2<Category, LineItem>> lineItemFlux) {
        Map<String, WebSocketHandler> urlMap = new HashMap<>();
        urlMap.put("/websocket", new LineItemHandler(lineItemFlux, DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        SimpleUrlHandlerMapping handlerMapping = new SimpleUrlHandlerMapping();
        handlerMapping.setOrder(1);
        handlerMapping.setUrlMap(urlMap);
        return handlerMapping;
    }

    @Bean
    public WebSocketHandlerAdapter handlerAdapter() {
        return new WebSocketHandlerAdapter();
    }
}
