package io.spring.training.boot.kafkatraining.apiKeys;

import lombok.Getter;

@Getter
public enum SupportedApiKeys {
    PRODUCE((short)0),
    API_VERSIONS((short)18);

    private final short keyOrdinal;

    SupportedApiKeys(short keyOrdinal) {
        this.keyOrdinal = keyOrdinal;
    }

    public short getApiKey() {
        return (short) 5;
    }
}
