package io.spring.training.boot.kafkatraining.internal.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.IOException;

public class Header {
    private static final Logger logger = LoggerFactory.getLogger(Header.class);

    private DataInputStream raw = null;
    // 32-bit signed
    private int messageSize;
    // 32-bit signed
    private int correlationId;

    public Header(DataInputStream raw) {
        this.raw = raw;
        this.messageSize = 0;
        this.correlationId = 0;
    }

    public void parse() {
        try {
            logger.info("invoking method parse() from Header class");

            messageSize = this.raw.readInt();
            logger.info("message size content is of {}", messageSize);

            correlationId = this.raw.readInt();
            logger.info("message size content is of {}", correlationId);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
