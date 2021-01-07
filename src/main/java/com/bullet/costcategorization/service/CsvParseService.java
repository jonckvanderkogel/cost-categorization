package com.bullet.costcategorization.service;

import com.bullet.costcategorization.domain.LineItem;
import com.bullet.costcategorization.domain.LineItemFactory;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import lombok.SneakyThrows;
import reactor.core.publisher.Flux;

public class CsvParseService {
    private final static CSVParser CSV_PARSER = new CSVParserBuilder()
            .withSeparator(';')
            .build();

    public Flux<LineItem> parseRawLines(Flux<String> rawLines) {
        return rawLines
                .map(this::parseLine)
                .map(a -> LineItemFactory.parseStringData(a[0], a[1], a[5], a[6], a[8]));
    }

    @SneakyThrows
    private String[] parseLine(String rawLine) {
        return CSV_PARSER.parseLine(rawLine);
    }
}
