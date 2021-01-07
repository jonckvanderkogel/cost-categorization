package com.bullet.costcategorization.service;

import com.bullet.costcategorization.domain.Category;
import com.bullet.costcategorization.domain.LineItem;
import com.bullet.costcategorization.domain.LineItemFactory;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import io.vavr.Tuple2;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.adapter.JdkFlowAdapter;
import reactor.core.publisher.Flux;

import java.util.Optional;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static util.FileProcessing.streamLinesFromFile;

public class CategorizationServiceTests {
    private final static CSVParser CSV_PARSER = new CSVParserBuilder()
            .withSeparator(';')
            .build();

    @Test
    public void shouldEmitCategorizedLineItem() {
        Categorizer firstCategorizer = (li) -> li.getDescription().equals("Foo") ? Optional.of(Category.MORTGAGE) : Optional.empty();
        Categorizer nextCategorizer = (li) -> li.getDescription().equals("Bar") ? Optional.of(Category.INSURANCE) : Optional.empty();

        var rawLinesPublisher = new SubmissionPublisher<LineItem>();

        var categorizationService = new CategorizationService(firstCategorizer.orElse(nextCategorizer));

        var callback = new Callback();
        categorizationService
                .categorize(JdkFlowAdapter.flowPublisherToFlux(rawLinesPublisher))
                .subscribe(new TestSubscriber(callback));

        CsvParseService csvParseService = new CsvParseService();

        csvParseService
                .parseRawLines(Flux.fromStream(
                        streamLinesFromFile("test.csv")
                                .skip(1)
                        )
                )
                .doOnNext(rawLinesPublisher::submit)
                .doOnComplete(() -> rawLinesPublisher.close())
                .subscribe();

        await().atMost(2, TimeUnit.SECONDS).untilAsserted(
                () -> {
                    assertEquals(Category.INSURANCE, callback.getCategory());
                }
        );
    }

    private static class Callback {
        Tuple2<Category, LineItem> tuple;

        public void callMe(Tuple2<Category, LineItem> tuple) {
            this.tuple = tuple;
        }

        public Category getCategory() {
            if (tuple != null) {
                return tuple._1;
            } else {
                return Category.REST;
            }
        }
    }

    private static class TestSubscriber implements Subscriber<Tuple2<Category, LineItem>> {
        private Callback callback;

        public TestSubscriber(Callback callback) {
            this.callback = callback;
        }

        @Override
        public void onSubscribe(Subscription s) {
            s.request(1);
        }

        @Override
        public void onNext(Tuple2<Category, LineItem> tuple) {
            callback.callMe(tuple);
        }

        @Override
        public void onError(Throwable t) {
        }

        @Override
        public void onComplete() {
        }
    }
}
