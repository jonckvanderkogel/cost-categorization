package com.bullet.costcategorization.service;

import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.function.Function;

@FunctionalInterface
public interface Transformer<T,U> {
    Optional<U> transformMaybe(T payload);

    default U transform(T payload, Function<T,U> defaultFun) {
        return transformMaybe(payload)
                .orElseGet(() -> defaultFun.apply(payload));
    }

    default Transformer<T,U> orElse(Transformer<T,U> next) {
        return new ChainedTransformer<>(this, next);
    }

    @RequiredArgsConstructor
    class ChainedTransformer<T,U> implements Transformer<T,U> {
        private final Transformer<T,U> current;
        private final Transformer<T,U> next;

        @Override
        public Optional<U> transformMaybe(T payload) {
            return current
                    .transformMaybe(payload)
                    .or(() -> next.transformMaybe(payload));
        }
    }
}
