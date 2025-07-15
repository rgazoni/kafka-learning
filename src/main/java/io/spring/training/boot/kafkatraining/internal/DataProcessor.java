package io.spring.training.boot.kafkatraining.internal;

import org.springframework.stereotype.Service;

import java.io.DataInputStream;

@Service
public class DataProcessor {
    public DataProcessor() {}

    public void parseInputData(DataInputStream raw) {

        Header h = new Header(raw);
        h.parse();



    }
}
