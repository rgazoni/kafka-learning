package io.spring.training.boot.kafkatraining.apiKeys.produce.v4;

import io.spring.training.boot.kafkatraining.apiKeys.produce.AbstractProducer;

public class ProduceRequestImpl extends AbstractProducer implements ProduceRequest {

    public ProduceRequestImpl(int version) {
        super(version);
    }

    public void hello() {
        System.out.println("Hello World!");
    }




}
