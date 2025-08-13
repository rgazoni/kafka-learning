package io.spring.training.boot.kafkatraining.support;

import io.spring.training.boot.kafkatraining.internal.protocol.ProtocolFieldSizes;

import java.nio.ByteBuffer;

public class Samples {

    public static byte[] smallBody() {
        return new byte[]{0,23, 0,2, 0,8, 0,10};
    }

    static byte[] headerModel(int messageSize, short apiKey, short apiVersion, int correlationId) {

        ByteBuffer buffer = ByteBuffer.allocate(
                ProtocolFieldSizes.HEADER_MESSAGE_SIZE_BYTES +
                        ProtocolFieldSizes.HEADER_REQ_API_KEY_BYTES +
                        ProtocolFieldSizes.HEADER_REQ_API_VERSION_BYTES +
                        ProtocolFieldSizes.HEADER_CORRELATION_ID_BYTES
        );

        buffer
                .putInt(messageSize)
                .putShort(apiKey)
                .putShort(apiVersion)
                .putInt(correlationId);

        return buffer.array();
    }

}
