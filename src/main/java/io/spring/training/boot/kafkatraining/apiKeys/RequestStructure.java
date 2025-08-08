package io.spring.training.boot.kafkatraining.apiKeys;

import io.spring.training.boot.kafkatraining.internal.header.HeaderModel;
import io.spring.training.boot.kafkatraining.internal.protocol.error.ProtocolError;
import io.spring.training.boot.kafkatraining.internal.protocol.error.ProtocolException;

import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

abstract public class RequestStructure<I extends MinimalContractForRequest> implements RequestType, Versionable<I> {
    private final SupportedApiKeys API_KEY;
    private final Map<Short, I> versionHandler;

    protected RequestStructure(SupportedApiKeys apiKey, List<I> versions) {
        API_KEY = apiKey;
        this.versionHandler = versions.stream()
                .collect(Collectors.toMap(I::getVersion, Function.identity()));
    }

    public void redirectToVersion(HeaderModel hm, byte[] body) {
        I version = fromVersion(hm.requestApiVersion());
        version.execute(hm, body);
    }

    public short getApiKey() {
        return API_KEY.getApiKey();
    }

    public I fromVersion(short v) {
        I version = this.versionHandler.get(v);
        if (version == null) {
            throw new ProtocolException(ProtocolError.UNSUPPORTED_VERSION);
        }
        return version;
    }

    public SortedSet<Short> getRangeOfSupportedVersions() {
        List<Short> versions = versionHandler.values()
                .stream()
                .map(I::getVersion)
                .toList();

        return new TreeSet<>(versions);
    }
}
