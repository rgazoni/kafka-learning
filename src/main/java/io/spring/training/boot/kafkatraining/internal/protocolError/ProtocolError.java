package io.spring.training.boot.kafkatraining.internal.protocolError;

public enum ProtocolError {
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
        switch(this) {
            case UNSUPPORTED_VERSION:
                return "The version of API is not supported.";
            case INVALID_REQUEST:
                return "This most likely occurs because of a request being malformed by " +
                        "the client library or the message was sent to an incompatible broker.";

            // Leave this for rest, both fall under the same return
            case UNKNOWN_SERVER_ERROR:
            default:
                return "The server experienced an unexpected error when processing the request.";
        }
    }

}
