package io.spring.training.boot.kafkatraining.apiKeys;

import io.spring.training.boot.kafkatraining.internal.header.HeaderModel;
import io.spring.training.boot.kafkatraining.internal.protocol.error.ProtocolError;
import io.spring.training.boot.kafkatraining.internal.protocol.error.ProtocolException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ApiKeyHandler {
    private final Map<Short, RequestType> handlers;

    public ApiKeyHandler(List<RequestType> handlerList) {
        this.handlers = handlerList.stream()
                .collect(Collectors.toMap(RequestType::getApiKey, Function.identity()));
    }

    public void redirect(HeaderModel hm, byte[] body) {
        RequestType handler = handlers.get(hm.requestApiKey());
        if (handler == null) {
            throw new ProtocolException(ProtocolError.INVALID_REQUEST);
        }
        handler.redirectToVersion(hm, body);
    }

}
