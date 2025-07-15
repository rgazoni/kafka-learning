package io.spring.training.boot.kafkatraining.internal.apiKeys.produce;

import lombok.Getter;

@Getter
public class AbstractProducer {
    protected int version;

    public AbstractProducer(int version) {
        this.version = version;
    }
}
