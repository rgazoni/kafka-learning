package io.spring.training.boot.kafkatraining.apiKeys.apiVersions;

import io.spring.training.boot.kafkatraining.apiKeys.RequestStructure;
import io.spring.training.boot.kafkatraining.apiKeys.SupportedApiKeys;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApiVersions extends RequestStructure<ApiVersionsRequest> {
    public ApiVersions(List<ApiVersionsRequest> versions) {
        super(SupportedApiKeys.API_VERSIONS, versions);
    }
}
