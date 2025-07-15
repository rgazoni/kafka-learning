package io.spring.training.boot.kafkatraining;

import io.spring.training.boot.kafkatraining.internal.socket.ServerSocketImpl;
import io.spring.training.boot.kafkatraining.internal.socket.config.SocketSettings;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

@SpringBootApplication
@EnableConfigurationProperties({SocketSettings.class})
public class KafkaTrainingApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaTrainingApplication.class, args);
    }

    // By default, this bean will be a Singleton, as we wanted to
    @Bean(initMethod = "start")
    public ServerSocketImpl socketServer(SocketSettings settings) throws IOException {
        return new ServerSocketImpl(settings);
    }
}
