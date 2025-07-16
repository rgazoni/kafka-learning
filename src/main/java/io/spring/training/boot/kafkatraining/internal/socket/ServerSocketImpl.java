package io.spring.training.boot.kafkatraining.internal.socket;

import io.spring.training.boot.kafkatraining.internal.DataProcessor;
import io.spring.training.boot.kafkatraining.internal.header.HeaderModel;
import io.spring.training.boot.kafkatraining.internal.socket.config.SocketSettings;
import io.spring.training.boot.kafkatraining.internal.utils.ByteConverter;
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
    private Socket clientSocket = null;
    private ServerSocket serverSocket = null;

    private final DataProcessor dataProcessor;

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
        } finally {
            try {
                if (clientSocket != null) {
                    logger.info("closing socket");
                    clientSocket.close();
                }
            } catch (IOException e) {
                logger.error("IOException on closing socket: {}", e.getMessage());
            }
        }
    }

    public void acceptClientConn() {
        Thread thread = new Thread(() -> {
            boolean running = true;
            logger.debug("server socket is now accepting connection from clients");
            while (running && !serverSocket.isClosed()) {
                try (
                        Socket clientSocket = serverSocket.accept();
                        DataInputStream dis = new DataInputStream(clientSocket.getInputStream())
                ) {
                    logger.info("a client connected to socket with addr:port {}", clientSocket.getRemoteSocketAddress());
                    logger.info("client {} sent a message with buffer size of {} Kbs",
                            clientSocket.getRemoteSocketAddress(),
                            ByteConverter.toMB(clientSocket.getReceiveBufferSize()));

                    HeaderModel h = this.dataProcessor.parseInputData(dis);

                    byte[] buf = ByteBuffer
                            .allocate(24)
                            .order(ByteOrder.BIG_ENDIAN)    // network order
                            .putInt(h.messageSize())
                            .putInt(h.correlationId())
                            .array();

                    try (BufferedOutputStream out = new BufferedOutputStream(clientSocket.getOutputStream())) {
                        out.write(buf);
                    }

                } catch (IOException e) {
                    if (running) {
                        logger.error("I/O error handling client connection", e);
                    }
                }
            }
        }, "SocketServer-Thread");
        thread.start();
    }
}
