package io.spring.training.boot.kafkatraining.apiKeys.produce;

import io.spring.training.boot.kafkatraining.internal.header.HeaderModel;

public interface ProduceRequest {
    void execute(HeaderModel hm, byte[] body);
}
