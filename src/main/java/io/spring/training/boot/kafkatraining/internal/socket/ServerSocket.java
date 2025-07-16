package io.spring.training.boot.kafkatraining.internal.socket;


public interface ServerSocket {
   void start();
   void acceptClientConn();
}
