package io.spring.training.boot.kafkatraining.apiKeys;

import io.spring.training.boot.kafkatraining.internal.header.HeaderModel;

public interface RequestApiRedirector {
    public void redirectToVersion(HeaderModel hm, byte[] body);
    short getVersion();
}
