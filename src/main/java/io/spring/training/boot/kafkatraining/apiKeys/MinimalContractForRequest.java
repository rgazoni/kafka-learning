package io.spring.training.boot.kafkatraining.apiKeys;

import io.spring.training.boot.kafkatraining.internal.header.HeaderModel;

public interface MinimalContractForRequest {
    void execute(HeaderModel hm, byte[] body);
    short getVersion();
}
