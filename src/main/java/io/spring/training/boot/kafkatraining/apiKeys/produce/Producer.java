package io.spring.training.boot.kafkatraining.apiKeys.produce;

import io.spring.training.boot.kafkatraining.apiKeys.RequestStructure;
import io.spring.training.boot.kafkatraining.apiKeys.SupportedApiKeys;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Producer extends RequestStructure<ProduceRequest> {
    public Producer(List<ProduceRequest> versions) {
        super(SupportedApiKeys.PRODUCE, versions);
    }
}
