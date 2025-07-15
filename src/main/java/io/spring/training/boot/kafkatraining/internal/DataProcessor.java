package io.spring.training.boot.kafkatraining.internal;

import io.spring.training.boot.kafkatraining.internal.header.Header;
import io.spring.training.boot.kafkatraining.internal.header.HeaderModel;
import org.springframework.stereotype.Service;

import java.io.DataInputStream;

@Service
public class DataProcessor {
    public DataProcessor() {}

    public HeaderModel parseInputData(DataInputStream raw) {

        Header h = new Header(raw);
        return h.parse();



    }
}
