package com.bullet.costcategorization.service;

import com.bullet.costcategorization.domain.Category;
import com.bullet.costcategorization.domain.LineItem;
import io.vavr.Tuple2;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.adapter.JdkFlowAdapter;

import java.io.File;
import java.util.Optional;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CategorizationServiceTests {

    @Test
    public void shouldEmitCategorizedLineItem() {
        Categorizer firstCategorizer = (li) -> li.getDescription().equals("Foo") ? Optional.of(Category.MORTGAGE) : Optional.empty();
        Categorizer nextCategorizer = (li) -> li.getDescription().equals("Bar") ? Optional.of(Category.INSURANCE) : Optional.empty();
        var definiteCategorizer = new DefiniteCategorizer(firstCategorizer.orElse(nextCategorizer));

        var filePublisher = new SubmissionPublisher<File>();

        var categorizationService = new CategorizationService(definiteCategorizer, JdkFlowAdapter.flowPublisherToFlux(filePublisher));

        var callback = new Callback();
        categorizationService.categorize().subscribe(new TestSubscriber(callback));

        filePublisher.submit(new File("src/test/resources/test.csv"));
        filePublisher.close();

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

    private static class TestSubscriber implements Subscriber<Tuple2<Category,LineItem>> {
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
