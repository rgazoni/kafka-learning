package io.spring.training.boot.kafkatraining.internal.protocol.error;

public class ProtocolException extends RuntimeException {
    private final ProtocolError error;

    public ProtocolException(ProtocolError error) {
        super(error.name());
        this.error = error;
    }

    public String getErrorName() {
        return error.name();
    }

    public short getErrorCode() {
        return error.code();
    }

    public String getErrorDescription() {
       return error.description();
    }

}
