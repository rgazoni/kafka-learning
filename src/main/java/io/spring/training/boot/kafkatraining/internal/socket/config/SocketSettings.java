package io.spring.training.boot.kafkatraining.internal.socket.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;

@ConfigurationProperties(prefix = "application.socket")
public class SocketSettings {
    private static final Logger log = LoggerFactory.getLogger(SocketSettings.class);

    @Getter
    private int port = 9092;
    private final Environment env;

    @Autowired
    public SocketSettings(Environment env) {
        this.env = env;
    }

    @PostConstruct
    public void warnIfDefault() {
        if (!env.containsProperty("application.socket.port")) {
            log.warn("No 'application.socket.port' configured – defaulting to {}", port);
        } else {
            log.info("Socket port defined as {}", port);
        }
    }
}
