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
import org.springframework.context.SmartLifecycle;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.atomic.AtomicBoolean;

// His bean definition is presented on Main Config File: KafkaTrainingApplication
public class ServerSocketImpl implements io.spring.training.boot.kafkatraining.internal.socket.ServerSocket {
    private static final Logger logger = LoggerFactory.getLogger(ServerSocketImpl.class);

    private final SocketSettings settings;
    private final DataProcessor dataProcessor;

    private Thread readyThread;

    @Getter
    private ServerSocket serverSocket = null;

    private final AtomicBoolean running = new AtomicBoolean(false);

    public ServerSocketImpl(SocketSettings settings, DataProcessor dataProcessor) throws IOException {
        this.settings = settings;
        this.dataProcessor = dataProcessor;
    }

    @Override
    public void start() {
        if (running.compareAndSet(false, true)) {
            logger.info("invoking start() method to init Socket system");
            try {
                logger.debug("creating a new serverSocket on port {}", settings.getPort());
                serverSocket = new ServerSocket();
                // Since the tester restarts your program quite often, setting SO_REUSEADDR
                // ensures that we don't run into 'Address already in use' errors
                serverSocket.setReuseAddress(true);
                serverSocket.bind(new InetSocketAddress(settings.getPort()));

                logger.debug("creating Thread for accepting connections from clients");
                readyThread = new Thread(this::readyForClientConnections, "SocketServer-Thread");
                readyThread.start();

                logger.info("server socket is ready and accepting connections");
            } catch (IOException e) {
                throw new RuntimeException("failed to start server", e);
            }
        }
    }

    @Override
    public void stop() {
        if (running.compareAndSet(true, false)) {
            try {
                serverSocket.close();
                readyThread.join(500);
            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    @Override
    public boolean isRunning() {
        return running.get();
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop(Runnable callback) {
        stop();
        // this is merely for shutdown lifecycle purposes
        callback.run();
    }

    @Override
    // this also handles lifecycle purposes, if it has a greater number
    // it will start later, otherwise earlier
    public int getPhase() {
        return 0;
    }

    public void readyForClientConnections() {

        while (running.get()) {

            try (Socket client = serverSocket.accept()) {
                DataInputStream in = new DataInputStream(client.getInputStream());
                BufferedOutputStream out = new BufferedOutputStream(client.getOutputStream());

                try {
                    logger.info("a client connected to socket with addr:port {}", client.getRemoteSocketAddress());
                    logger.info("client {} sent a message with buffer size of {} Kbs",
                            client.getRemoteSocketAddress(),
                            ByteConverter.toMB(client.getReceiveBufferSize()));

                    byte[] buf = handleClient(in);

                    out.write(buf);

                } catch (ProtocolException p) {
                    logger.error("protocol name error {}, with code {} and description: {}",
                            p.getErrorName(), p.getErrorCode(), p.getErrorDescription());

                    sendErrorCode(client, p.getErrorCode());

                } catch (IOException e) {
                    logger.error("I/O error handling client connection", e);
                    ProtocolException p = new ProtocolException(ProtocolError.UNKNOWN_SERVER_ERROR);

                    logger.error("protocol name error {}, with code {} and description: {}",
                            p.getErrorName(), p.getErrorCode(), p.getErrorDescription());

                    sendErrorCode(client, p.getErrorCode());

                } finally {
                    try {
                        out.flush();
                    } catch (IOException ignore) {
                        logger.warn("error closing client socket");
                    }
                }
            } catch (IOException e) {
                logger.warn("client handling failed early", e);
            }
        }
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

    private byte[] handleClient(DataInputStream in) {
        HeaderModel h = this.dataProcessor.parseInputData(in);

        return ByteBuffer
                .allocate(24)
                .order(ByteOrder.BIG_ENDIAN)
                .putInt(h.messageSize())
                .putInt(h.correlationId())
                .array();

    }
}
