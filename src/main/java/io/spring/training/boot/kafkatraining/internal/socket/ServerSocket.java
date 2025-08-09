package io.spring.training.boot.kafkatraining.internal.socket;


import org.springframework.context.SmartLifecycle;

public interface ServerSocket extends SmartLifecycle {
   void readyForClientConnections();
}
