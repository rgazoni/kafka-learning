package io.spring.training.boot.kafkatraining;

import io.spring.training.boot.kafkatraining.internal.protocolError.ProtocolError;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class KafkaTrainingApplicationTests {
    private static final Logger logger = LoggerFactory.getLogger(KafkaTrainingApplicationTests.class);

    private Socket clientSocket;
    private DataInputStream in;
    private BufferedOutputStream out;

    @BeforeEach
    void connectToSocketServer() {
        try {
            this.clientSocket = new Socket("127.0.0.1", 9092);
            this.clientSocket.setSoTimeout(1000_000);
            out = new BufferedOutputStream(this.clientSocket.getOutputStream());
            in = new DataInputStream(this.clientSocket.getInputStream());
        } catch (IOException e) {
            logger.error("test error for connecting to local server on port 9092. e: {}", e.getMessage());
        }
    }

    @Test
    void sendFixedCorrelationIdAndVerifyReturnConsistency() throws IOException {
        // REQUEST
        // Message Size
        // API key
        // API version
        // Correlation id

        // EXPECTED RESPONSE
        // Message Size
        // Correlation id

        int correlationId = 7;

        byte[] body = {
                0x00, 0x17,
                0x00, 0x02,
                0x00, 0x08,
                0x00, 0x0A
        };
        int messageSize = body.length;  // 8

        ByteBuffer buf = ByteBuffer.allocate(4 + 2 + 2 + 4 + body.length);
        buf.putInt(messageSize);        // size
        buf.putShort((short)0);         // apiKey
        buf.putShort((short)0);         // apiVersion
        buf.putInt(correlationId);      // correlationId
        buf.put(body);                  // body

        int response;
        try {
            out.write(buf.array());
            out.flush();

            // ignores message size
            in.readFully(new byte[4]);

            response = in.readInt();

            assertEquals(correlationId, response, "Correlation ID should match the request");
        } catch (IOException e) {
            logger.error("test error for sending or reading a message to local server on port 9092. e: {}", e.getMessage());
        } finally {
            out.close();
        }
    }

    @Test
    void sendNotSupportedApiKey() throws IOException {
        // EXPECTED ERROR RESPONSE
        // Message Size
        // Correlation id
        // ERROR_CODE

        byte[] body = {
                0x00, 0x17,
                0x00, 0x02,
                0x00, 0x08,
                0x00, 0x0A
        };
        int messageSize = body.length;  // 8

        byte[] buf = ByteBuffer.allocate(4 + 2 + 2 + 4 + body.length)
                .putInt(messageSize)        // size
                .putShort((short) 10)       // set a api key that does not exists
                .putShort((short) 0)        // apiVersion
                .putInt(7)           // correlationId
                .put(body)
                .array();

        int response;
        try {
            out.write(buf);
            out.flush();

            // ignores message size and correlation id
            in.readFully(new byte[8]);

            response = in.readShort();

            assertEquals(ProtocolError.INVALID_REQUEST.code(), response, "Mismatch error response and expected");

        } catch (IOException e) {
            logger.error("test error for sending or reading a message to local server on port 9092. e: {}", e.getMessage());
        } finally {
            out.close();
        }

    }

    @AfterEach
    void closeConnToSocketServer() {
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            logger.error("test error for closing connection to local server on port 9092. e: {}", e.getMessage());
        }
    }
}
