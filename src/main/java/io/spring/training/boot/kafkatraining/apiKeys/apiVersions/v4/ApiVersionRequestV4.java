package io.spring.training.boot.kafkatraining.apiKeys.apiVersions.v4;

import io.spring.training.boot.kafkatraining.apiKeys.apiVersions.ApiVersions;
import io.spring.training.boot.kafkatraining.internal.header.HeaderModel;

public class ApiVersionRequestV4 implements ApiVersions {
    @Override
    public void execute(HeaderModel hm, byte[] body) {
        System.out.println("Inside Api version v4");
    }
}
