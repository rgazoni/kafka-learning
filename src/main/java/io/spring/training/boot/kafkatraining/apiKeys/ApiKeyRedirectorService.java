package io.spring.training.boot.kafkatraining.apiKeys;

import io.spring.training.boot.kafkatraining.internal.header.HeaderModel;
import io.spring.training.boot.kafkatraining.internal.protocolError.ProtocolError;
import io.spring.training.boot.kafkatraining.internal.protocolError.ProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ApiKeyRedirectorService {
    private final Map<Short, RequestApiRedirector> handlers;

    @Autowired
    public ApiKeyRedirectorService(List<RequestApiRedirector> handlerList) {
        this.handlers = handlerList.stream()
                .collect(Collectors.toMap(RequestApiRedirector::getResourceApiKey, Function.identity()));
    }

    public void redirect(HeaderModel hm, byte[] body) {
        RequestApiRedirector handler = handlers.get(hm.requestApiKey());
        if (handler == null) {
            throw new ProtocolException(ProtocolError.INVALID_REQUEST);
        }
        handler.redirectToVersion(hm, body);
    }

}
