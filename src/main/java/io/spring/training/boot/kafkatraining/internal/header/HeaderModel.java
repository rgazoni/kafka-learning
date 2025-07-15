package io.spring.training.boot.kafkatraining.internal.header;

import lombok.Builder;

@Builder
public record HeaderModel(
        int messageSize,
        int correlationId
) {
}
