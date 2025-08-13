package io.spring.training.boot.kafkatraining;

import io.spring.training.boot.kafkatraining.internal.socket.config.SocketSettings;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles(value = "test")
@EnableConfigurationProperties({SocketSettings.class})
class KafkaTrainingApplicationTests {}
