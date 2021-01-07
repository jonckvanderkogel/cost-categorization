package com.bullet.costcategorization.service;

import com.bullet.costcategorization.domain.Category;
import com.bullet.costcategorization.domain.LineItem;
import com.bullet.costcategorization.domain.LineItemParser;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import io.vavr.Tuple2;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class CategorizationService {
    private final DefiniteCategorizer categorizer;
    private final Flux<String> rawLinesFlux;

    private final static CSVParser CSV_PARSER = new CSVParserBuilder()
            .withSeparator(';')
            .build();

    public Flux<Tuple2<Category, LineItem>> categorize() {
        return parseRawLines()
                .map(li -> new Tuple2<>(categorizer.categorize(li), li));
    }

    private Flux<LineItem> parseRawLines() {
        return rawLinesFlux
                .map(this::parseLine)
                .map(a -> LineItemParser.parseStringData(a[0], a[1], a[5], a[6], a[8]));
    }


    @SneakyThrows
    private String[] parseLine(String rawLine) {
        return CSV_PARSER.parseLine(rawLine);
    }
}
