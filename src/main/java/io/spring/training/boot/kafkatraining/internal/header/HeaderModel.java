package io.spring.training.boot.kafkatraining.internal.header;

import lombok.Builder;

@Builder
public record HeaderModel(
        int messageSize,
        short requestApiKey,
        short requestApiVersion,
        int correlationId,
        String clientId,
        String[] TAG_ARRAY
) {}
