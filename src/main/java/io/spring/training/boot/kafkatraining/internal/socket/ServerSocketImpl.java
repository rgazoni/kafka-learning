package io.spring.training.boot.kafkatraining.internal.socket;

import io.spring.training.boot.kafkatraining.internal.DataProcessor;
import io.spring.training.boot.kafkatraining.internal.header.HeaderModel;
import io.spring.training.boot.kafkatraining.internal.protocol.error.ProtocolError;
import io.spring.training.boot.kafkatraining.internal.protocol.error.ProtocolException;
import io.spring.training.boot.kafkatraining.internal.protocol.ProtocolFieldSizes;
import io.spring.training.boot.kafkatraining.internal.socket.config.SocketSettings;
import io.spring.training.boot.kafkatraining.internal.utils.ByteConverter;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

// His bean definition is presented on Main Config File: KafkaTrainingApplication
public class ServerSocketImpl extends ServerSocket implements io.spring.training.boot.kafkatraining.internal.socket.ServerSocket {
    private static final Logger logger = LoggerFactory.getLogger(ServerSocketImpl.class);
    private final SocketSettings settings;
    private final DataProcessor dataProcessor;
    private Socket clientSocket = null;

    @Getter
    private ServerSocket serverSocket = null;

    public ServerSocketImpl(SocketSettings settings, DataProcessor dataProcessor) throws IOException {
        this.settings = settings;
        this.dataProcessor = dataProcessor;
    }

    public void start(){
        logger.info("invoking start() method to init Socket system");
        try {
            logger.debug("creating a new serverSocket");
            serverSocket = new ServerSocket(settings.getPort());
            // Since the tester restarts your program quite often, setting SO_REUSEADDR
            // ensures that we don't run into 'Address already in use' errors
            serverSocket.setReuseAddress(true);

            acceptClientConn();

        } catch (IOException e) {
            logger.error("IOException on socket creation: {}", e.getMessage());
        }
    }

    public void acceptClientConn() {
        Thread thread = new Thread(() -> {
            logger.debug("server socket is now accepting connection from clients");
            while (!serverSocket.isClosed()) {
                try {
                    clientSocket = serverSocket.accept();
                    DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
                    logger.info("a client connected to socket with addr:port {}", clientSocket.getRemoteSocketAddress());
                    logger.info("client {} sent a message with buffer size of {} Kbs",
                            clientSocket.getRemoteSocketAddress(),
                            ByteConverter.toMB(clientSocket.getReceiveBufferSize()));

                    HeaderModel h = this.dataProcessor.parseInputData(dis);

                    byte[] buf = ByteBuffer
                            .allocate(24)
                            .order(ByteOrder.BIG_ENDIAN)
                            .putInt(h.messageSize())
                            .putInt(h.correlationId())
                            .array();

                    try (BufferedOutputStream out = new BufferedOutputStream(clientSocket.getOutputStream())) {
                        out.write(buf);
                    }

                } catch (IOException | ProtocolException e) {
                    ProtocolException p;
                    if (e.getClass().equals(IOException.class)) {
                        logger.error("I/O error handling client connection", e);
                        p = new ProtocolException(ProtocolError.UNKNOWN_SERVER_ERROR);
                    } else {
                        assert e instanceof ProtocolException;
                        p = (ProtocolException) e;
                    }
                    logger.error("protocol name error {}, with code {} and description: {}",
                            p.getErrorName(), p.getErrorCode(), p.getErrorDescription());
                    if (clientSocket != null) {
                        sendErrorCode(clientSocket, p.getErrorCode());
                    }
                } finally {
                    if (clientSocket != null) {
                        try {
                            clientSocket.close();
                        } catch (IOException e) {
                            logger.warn("Error closing client socket", e);
                        }
                    }
                }
            }
        }, "SocketServer-Thread");
        thread.start();
    }

    private void sendErrorCode(Socket client, short code) {
        byte[] buf = ByteBuffer
                .allocate(ProtocolFieldSizes.HEADER_MESSAGE_SIZE_BYTES +
                                ProtocolFieldSizes.HEADER_CORRELATION_ID_BYTES +
                                ProtocolFieldSizes.ERROR_CODE_BYTES)
                .order(ByteOrder.BIG_ENDIAN)
                .putInt(Integer.MAX_VALUE) //junk
                .putInt(Integer.MAX_VALUE) //junk
                .putShort(code)
                .array();
        try (var out = new BufferedOutputStream(client.getOutputStream())) {
            out.write(buf);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
