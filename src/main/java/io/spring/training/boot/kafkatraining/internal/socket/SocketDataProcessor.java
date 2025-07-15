package io.spring.training.boot.kafkatraining.internal.socket;

import org.springframework.stereotype.Service;

import java.io.DataInputStream;
import java.io.InputStream;

@Service
public class SocketDataProcessor {
    public SocketDataProcessor() {}

    public void parseInputData(DataInputStream raw) {

        Header h = new Header(raw);
        h.parse();



    }
}
