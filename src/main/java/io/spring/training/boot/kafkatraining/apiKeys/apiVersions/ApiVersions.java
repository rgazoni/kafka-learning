package io.spring.training.boot.kafkatraining.apiKeys.apiVersions;

import io.spring.training.boot.kafkatraining.internal.header.HeaderModel;

public interface ApiVersions {
    void execute(HeaderModel hm, byte[] body);
}
