package io.spring.training.boot.kafkatraining.apiKeys.produce.v0;

import io.spring.training.boot.kafkatraining.apiKeys.produce.ProduceRequest;
import io.spring.training.boot.kafkatraining.internal.header.HeaderModel;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ProduceRequestV0 implements ProduceRequest {
    private final short PRODUCE_REQUEST_VERSION = 0;

    public void execute(HeaderModel hm, byte[] body) {
        System.out.println("Hello World!");
    }

    @Override
    public short getVersion() {
        return PRODUCE_REQUEST_VERSION;
    }
}
