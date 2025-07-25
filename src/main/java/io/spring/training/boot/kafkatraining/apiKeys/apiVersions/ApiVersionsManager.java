package io.spring.training.boot.kafkatraining.apiKeys.apiVersions;

import io.spring.training.boot.kafkatraining.apiKeys.RequestApiRedirector;
import io.spring.training.boot.kafkatraining.apiKeys.SupportedApiKeys;
import io.spring.training.boot.kafkatraining.internal.header.HeaderModel;

public class ApiVersionsManager implements RequestApiRedirector {
    @Override
    public void redirectToVersion(HeaderModel hm, byte[] body) {
        System.out.println("hello");
    }

    @Override
    public short getResourceApiKey() {
        return SupportedApiKeys.API_VERSIONS.getApiKey();
    }
}
