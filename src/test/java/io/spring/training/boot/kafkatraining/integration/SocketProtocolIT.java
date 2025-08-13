//package io.spring.training.boot.kafkatraining.integration;
//
//import io.spring.training.boot.kafkatraining.internal.protocol.error.ProtocolError;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//import org.junit.jupiter.api.Timeout;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.IOException;
//import java.nio.ByteBuffer;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@Timeout(5)
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//public class SocketProtocolIT {
//    private static final Logger logger = LoggerFactory.getLogger(SocketProtocolIT.class);
//
//    @Disabled
//    void sendFixedCorrelationIdAndVerifyReturnConsistency() throws IOException {
//        // REQUEST
//        // Message Size
//        // API key
//        // API version
//        // Correlation id
//
//        // EXPECTED RESPONSE
//        // Message Size
//        // Correlation id
//
//        int correlationId = 7;
//
//        byte[] body = {
//                0x00, 0x17,
//                0x00, 0x02,
//                0x00, 0x08,
//                0x00, 0x0A
//        };
//        int messageSize = body.length;  // 8
//
//        ByteBuffer buf = ByteBuffer.allocate(4 + 2 + 2 + 4 + body.length);
//        buf.putInt(messageSize);        // size
//        buf.putShort((short)0);         // apiKey
//        buf.putShort((short)0);         // apiVersion
//        buf.putInt(correlationId);      // correlationId
//        buf.put(body);                  // body
//
//        int response;
//        try {
//            out.write(buf.array());
//            out.flush();
//
//            // ignores message size
//            in.readFully(new byte[4]);
//
//            response = in.readInt();
//
//            assertEquals(correlationId, response, "Correlation ID should match the request");
//        } catch (IOException e) {
//            logger.error("test error for sending or reading a message to local server on port 9092. e: {}", e.getMessage());
//        } finally {
//            out.close();
//        }
//    }
//
//    @Disabled
//    void sendNotSupportedApiKey() throws IOException {
//        // EXPECTED ERROR RESPONSE
//        // Message Size
//        // Correlation id
//        // ERROR_CODE
//
//        byte[] body = {
//                0x00, 0x17,
//                0x00, 0x02,
//                0x00, 0x08,
//                0x00, 0x0A
//        };
//        int messageSize = body.length;  // 8
//
//        byte[] buf = ByteBuffer.allocate(4 + 2 + 2 + 4 + body.length)
//                .putInt(messageSize)        // size
//                .putShort((short) 10)       // set an api key that does not exist
//                .putShort((short) 0)        // apiVersion
//                .putInt(7)           // correlationId
//                .put(body)
//                .array();
//
//        int response;
//        try {
//            out.write(buf);
//            out.flush();
//
//            // ignores message size and correlation id
//            in.readFully(new byte[8]);
//
//            response = in.readShort();
//
//            assertEquals(ProtocolError.INVALID_REQUEST.code(), response, "Mismatch error response and expected");
//
//        } catch (IOException e) {
//            logger.error("test error for sending or reading a message to local server on port 9092. e: {}", e.getMessage());
//        } finally {
//            out.close();
//        }
//
//    }
//
//    @Disabled
//    void sendNotSupportedApiVersion() throws IOException {
//        // EXPECTED ERROR RESPONSE
//        // Message Size
//        // Correlation id
//        // ERROR_CODE
//        //      UNSUPPORTED_VERSION
//
//        byte[] body = {
//                0x00, 0x17,
//                0x00, 0x02,
//                0x00, 0x08,
//                0x00, 0x0A
//        };
//
//        int messageSize = body.length;  // 8
//
//        byte[] buf = ByteBuffer.allocate(4 + 2 + 2 + 4 + body.length)
//                .putInt(messageSize)        // size
//                .putShort((short) 18)       // set produce api key
//                .putShort((short) 55)        // apiVersion unavailable
//                .putInt(7)           // correlationId
//                .put(body)
//                .array();
//
//        int response;
//        try {
//            out.write(buf);
//            out.flush();
//
//            // ignores message size and correlation id
//            in.readFully(new byte[4 + 4]);
//
//            response = in.readShort();
//
//            assertEquals(ProtocolError.UNSUPPORTED_VERSION.code(), response, "Mismatch error response and expected");
//
//        } catch (IOException e) {
//            logger.error("test error for sending or reading a message to local server on port 9092. e: {}", e.getCause());
//        } finally {
//            out.close();
//        }
//
//    }
//
//
//}
