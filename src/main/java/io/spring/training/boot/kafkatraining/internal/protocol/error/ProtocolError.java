package io.spring.training.boot.kafkatraining.internal.protocol.error;

public enum ProtocolError {
    NONE((short)0),
    UNSUPPORTED_VERSION((short)35),
    UNKNOWN_SERVER_ERROR((short)-1),
    INVALID_REQUEST((short) 42)
    ;

    private final short code;
    ProtocolError(short code) {
        this.code = code;
    }
    public short code() {
        return code;
    }

    public String description() {
        return switch (this) {
            case UNSUPPORTED_VERSION -> "The version of API is not supported.";
            case INVALID_REQUEST -> "This most likely occurs because of a request being malformed by " +
                    "the client library or the message was sent to an incompatible broker.";

            // Leave this for rest, both fall under the same return
            default -> "The server experienced an unexpected error when processing the request.";
        };
    }

}
