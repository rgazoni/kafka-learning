package io.spring.training.boot.kafkatraining.integration;

import io.spring.training.boot.kafkatraining.internal.socket.ServerSocketImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Timeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@Timeout(5)
//public class SocketProtocolIT {
//    private static final Logger logger = LoggerFactory.getLogger(SocketProtocolIT.class);
//
//    private final ServerSocketImpl server;
//
//    public SocketProtocolIT(ServerSocketImpl server) {
//        this.server = server;
//    }
//
//    @BeforeAll
//    static void beforeAll() {
//        server.start();
//    }
//
//    @AfterAll
//    static void afterAll() {
//        server.stopServer();
//    }
//
//
//}
