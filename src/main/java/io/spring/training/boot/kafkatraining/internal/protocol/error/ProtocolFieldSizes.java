package io.spring.training.boot.kafkatraining.internal.protocol.error;

public class ProtocolFieldSizes {
    private ProtocolFieldSizes() {}

    // Header
    public static final int HEADER_MESSAGE_SIZE_BYTES = Integer.BYTES;
    public static final int HEADER_CORRELATION_ID_BYTES = Integer.BYTES;

    // ERROR
    public static final int ERROR_CODE_BYTES = Short.BYTES;

}
