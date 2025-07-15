package io.spring.training.boot.kafkatraining.internal.socket;

import io.spring.training.boot.kafkatraining.internal.utils.ByteConverter;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

@Service
public class SocketInitialization implements SocketInitializationInterface {
    private static final Logger logger = LoggerFactory.getLogger(SocketInitialization.class);

    @PostConstruct
    public void initialize(){
        logger.info("invoking initialize() method to init Socket system");

        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        int port = 9092;
        try {
            logger.debug("creating a new serverSocket");
            serverSocket = new ServerSocket(port);
            // Since the tester restarts your program quite often, setting SO_REUSEADDR
            // ensures that we don't run into 'Address already in use' errors
            serverSocket.setReuseAddress(true);

            logger.debug("server socket is now accepting connection from clients");
            clientSocket = serverSocket.accept();
            logger.info("a client connected to socket with addr:port {}", clientSocket.getRemoteSocketAddress());
            logger.info("client {} sent a message with buffer size of {} Kbs",
                    clientSocket.getRemoteSocketAddress(),
                    ByteConverter.toMB(clientSocket.getReceiveBufferSize()));

            //InputStream a = clientSocket.getInputStream();
            //logger.info("{}", a.readAllBytes());

            DataInputStream in = new DataInputStream(clientSocket.getInputStream());
            SocketDataProcessor socketData = new SocketDataProcessor();
            socketData.parseInputData(in);

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

}
