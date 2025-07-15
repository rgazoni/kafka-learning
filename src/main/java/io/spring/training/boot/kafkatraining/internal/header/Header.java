package io.spring.training.boot.kafkatraining.internal.header;

import lombok.AccessLevel;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.IOException;

@Getter
public class Header {
    @Getter(AccessLevel.NONE)
    private static final Logger logger = LoggerFactory.getLogger(Header.class);
    @Getter(AccessLevel.NONE)
    private DataInputStream raw = null;

    // 32-bit signed
    private int messageSize;
    // 32-bit signed
    private int correlationId;
    // 16-bit signed
    private short requestApiKey;
    // 16-bit signed
    private short requestApiVersion;
    // Nullable-string
    private String clientId;
    // Compact-array
    private String[] TAG_ARRAY;

    public Header(DataInputStream raw) {
        this.raw = raw;
        this.messageSize = 0;
        this.correlationId = 0;
        this.clientId = "";
        this.requestApiKey = 0;
        this.requestApiVersion = 0;
        this.TAG_ARRAY = new String[]{};
    }

    /**
     * --------- HEADER STRUCTURE -----------
     * request_api_key - INT16 - The API key for the request
     * request_api_version - INT16 - he version of the API for the request
     * correlation_id - INT32 - A unique identifier for the request
     * client_id - NULLABLE_STRING - The client ID for the request
     * TAG_BUFFER - COMPACT_ARRAY - Optional tagged fields
     */
    public HeaderModel parse() {
        try {
            logger.info("invoking method parse() from Header class");

            // Accessing message size
            messageSize = this.raw.readInt();
            logger.info("message_size content is of {}", messageSize);

            requestApiKey = this.raw.readShort();
            logger.info("request_api_key is of {}", requestApiKey);

            requestApiVersion = this.raw.readShort();
            logger.info("request_api_version is of {}", requestApiVersion);

            correlationId = this.raw.readInt();
            logger.info("correlation_id content is of {}", correlationId);

            return HeaderModel.builder()
                    .messageSize(messageSize)
                    .requestApiKey(requestApiKey)
                    .requestApiVersion(requestApiVersion)
                    .correlationId(correlationId)
                    .build();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
