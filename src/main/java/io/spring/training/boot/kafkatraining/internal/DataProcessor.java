package io.spring.training.boot.kafkatraining.internal;

import io.spring.training.boot.kafkatraining.internal.apiKeys.ApiRedirectorService;
import io.spring.training.boot.kafkatraining.internal.apiKeys.produce.ProducerManager;
import io.spring.training.boot.kafkatraining.internal.apiKeys.produce.v4.ProduceRequest;
import io.spring.training.boot.kafkatraining.internal.apiKeys.produce.v4.ProduceRequestImpl;
import io.spring.training.boot.kafkatraining.internal.header.Header;
import io.spring.training.boot.kafkatraining.internal.header.HeaderModel;
import lombok.AccessLevel;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.DataInputStream;
import java.io.IOException;

@Getter
@Service
public class DataProcessor {
    @Getter(AccessLevel.NONE)
    private static final Logger logger = LoggerFactory.getLogger(DataProcessor.class);
    private DataInputStream dataInputStream = null;

    private final ApiRedirectorService apiRedirectorService;

    public DataProcessor(ApiRedirectorService apiRedirectorService) {
        this.apiRedirectorService = apiRedirectorService;
    }

    public HeaderModel parseInputData(DataInputStream raw) {
        // Assign data coming from a client
        this.dataInputStream = raw;

        HeaderModel hm = Header.getInstance().parse(dataInputStream);
        byte[] body;
        try {
            body = raw.readAllBytes();
        } catch (IOException e) {
            // TODO see this err
            throw new RuntimeException(e);
        }

        this.apiRedirectorService.redirect(hm, body);

        return hm;
    }



}
