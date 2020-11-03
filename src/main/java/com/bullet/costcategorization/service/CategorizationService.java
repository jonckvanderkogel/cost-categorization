package com.bullet.costcategorization.service;

import com.bullet.costcategorization.domain.Category;
import com.bullet.costcategorization.domain.LineItem;
import com.opencsv.bean.CsvToBeanBuilder;
import io.vavr.Tuple2;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.stream.BaseStream;

@RequiredArgsConstructor
public class CategorizationService {
    private final DefiniteCategorizer categorizer;
    private final Flux<File> fileFlux;

    public Flux<Tuple2<Category, LineItem>> categorize() {
        return readLineItems()
                .map(li -> new Tuple2<>(categorizer.categorize(li), li));
    }

    private Flux<LineItem> readLineItems() {
        return fileFlux
                .flatMap(message -> Flux.using(
                        () -> new CsvToBeanBuilder<LineItem>(createFileReader(message)).withSkipLines(1)
                                .withSeparator(';')
                                .withType(LineItem.class)
                                .build()
                                .stream(),
                        Flux::fromStream,
                        BaseStream::close)
                );
    }

    private FileReader createFileReader(File file) {
        try {
            return new FileReader(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
