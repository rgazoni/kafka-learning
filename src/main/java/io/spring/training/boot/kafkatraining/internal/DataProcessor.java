package io.spring.training.boot.kafkatraining.internal;

import io.spring.training.boot.kafkatraining.apiKeys.ApiKeyHandler;
import io.spring.training.boot.kafkatraining.internal.header.Header;
import io.spring.training.boot.kafkatraining.internal.header.HeaderModel;
import io.spring.training.boot.kafkatraining.internal.protocolError.ProtocolError;
import io.spring.training.boot.kafkatraining.internal.protocolError.ProtocolException;
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

    private final ApiKeyHandler apiRedirectorService;

    public DataProcessor(ApiKeyHandler apiRedirectorService) {
        this.apiRedirectorService = apiRedirectorService;
    }

    public HeaderModel parseInputData(DataInputStream raw) {
        this.dataInputStream = raw;

        HeaderModel hm = Header.getInstance().parse(dataInputStream);

        byte[] body = new byte[4];
        try {
            dataInputStream.readFully(body);
        } catch (IOException e) {
            logger.error("I/O error handling client connection", e);
            throw new ProtocolException(ProtocolError.UNKNOWN_SERVER_ERROR);
        }

        this.apiRedirectorService.redirect(hm, body);

        return hm;
    }



}
