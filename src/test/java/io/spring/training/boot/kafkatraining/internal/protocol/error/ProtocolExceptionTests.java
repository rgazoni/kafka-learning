package io.spring.training.boot.kafkatraining.internal.protocol.error;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

public class ProtocolExceptionTests {

    @Test
    public void ctor_shouldThrownErrorPassedByArgument() {
        assertThatExceptionOfType(ProtocolException.class)
                .isThrownBy(() -> { throw new ProtocolException(ProtocolError.INVALID_REQUEST);})
                .extracting(ProtocolException::getErrorCode)
                .isEqualTo(ProtocolError.INVALID_REQUEST.code());
    }

}
