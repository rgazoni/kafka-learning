package io.spring.training.boot.kafkatraining.apiKeys.produce;

import io.spring.training.boot.kafkatraining.apiKeys.RequestApiRedirector;
import io.spring.training.boot.kafkatraining.apiKeys.SupportedApiKeys;
import io.spring.training.boot.kafkatraining.internal.header.HeaderModel;
import org.springframework.stereotype.Component;

@Component
public class ProducerManager implements RequestApiRedirector {

    @Override
    public void redirectToVersion(HeaderModel hm, byte[] body) {
        ProduceRequest Version = SupportedRequestApiVersions.fromVersion(hm.requestApiVersion());
        Version.execute(hm, body);
    }

    @Override
    public short getResourceApiKey() {
        return SupportedApiKeys.PRODUCE.getApiKey();
    }
}
