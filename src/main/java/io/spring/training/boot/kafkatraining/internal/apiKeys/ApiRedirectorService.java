package io.spring.training.boot.kafkatraining.internal.apiKeys;

import io.spring.training.boot.kafkatraining.internal.header.HeaderModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ApiRedirectorService {
    private final Map<Short, RequestApiRedirector> handlers;

    @Autowired
    public ApiRedirectorService(List<RequestApiRedirector> handlerList) {
        // build a map version -> handler
        this.handlers = handlerList.stream()
                .collect(Collectors.toMap(RequestApiRedirector::getVersion, Function.identity()));
    }

    public void redirect(HeaderModel hm, byte[] body) {
        RequestApiRedirector handler =
                handlers.get(hm.requestApiKey());
        if (handler == null) {
            // TODO understand err
            throw new RuntimeException("");
        }
        handler.redirectToVersion(hm, body);
    }

}
