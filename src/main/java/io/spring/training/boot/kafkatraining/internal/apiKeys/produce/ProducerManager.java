package io.spring.training.boot.kafkatraining.internal.apiKeys.produce;

import io.spring.training.boot.kafkatraining.internal.apiKeys.RequestApiRedirector;
import io.spring.training.boot.kafkatraining.internal.apiKeys.produce.v4.ProduceRequestImpl;
import io.spring.training.boot.kafkatraining.internal.header.HeaderModel;
import org.springframework.stereotype.Component;

@Component
public class ProducerManager implements RequestApiRedirector {

    @Override
    public void redirectToVersion(HeaderModel hm, byte[] body) {
        ProduceRequestImpl a = new ProduceRequestImpl(0);
        a.hello();
    }

    @Override
    public short getVersion() {
        return 0;
    }
}
