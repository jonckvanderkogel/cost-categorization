package com.bullet.costcategorization.websocket;

import com.bullet.costcategorization.domain.Category;
import com.bullet.costcategorization.domain.LineItem;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.Tuple2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.format.DateTimeFormatter;

@Slf4j
@RequiredArgsConstructor
public class LineItemHandler implements WebSocketHandler {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final Flux<Tuple2<Category, LineItem>> categorizedLineItemsflux;
    private final DateTimeFormatter dateTimeFormatter;

    @Override
    public Mono<Void> handle(WebSocketSession webSocketSession) {
        return webSocketSession
                .send(categorizedLineItemsflux
                        .filter(p -> !p._1.equals(Category.SAVINGS)) // filter out savings as it screws up the overview
                        .map(this::transformMessage)
                        .map(webSocketSession::textMessage))
                .and(webSocketSession
                        .receive()
                        .map(WebSocketMessage::getPayloadAsText)
                        .log()
                );
    }

    private String transformMessage(Tuple2<Category, LineItem> message) {
        LineItemDTO dto = new LineItemDTO(message, dateTimeFormatter);
        try {
            return objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            return "Error while serializing to JSON";
        }
    }
}
