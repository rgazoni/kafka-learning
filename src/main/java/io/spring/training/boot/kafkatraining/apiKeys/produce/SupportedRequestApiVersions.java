package io.spring.training.boot.kafkatraining.apiKeys.produce;

import io.spring.training.boot.kafkatraining.apiKeys.produce.v0.ProduceRequestV0;
import io.spring.training.boot.kafkatraining.internal.protocolError.ProtocolError;
import io.spring.training.boot.kafkatraining.internal.protocolError.ProtocolException;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public enum SupportedRequestApiVersions {

    V0((short) 0, new ProduceRequestV0());

    private final ProduceRequest objectVersion;
    private final short version;

    SupportedRequestApiVersions(short version, ProduceRequest objectVersion) {
        this.version = version;
        this.objectVersion = objectVersion;
    }

    private static final Map<Short, ProduceRequest> VERSION_MAP =
            Arrays.stream(values())
                    .collect(Collectors.toMap(SupportedRequestApiVersions::getVersion,
                                                SupportedRequestApiVersions::getObjectVersion));

    public static ProduceRequest fromVersion(short v) {
        ProduceRequest instance = VERSION_MAP.get(v);
        if (instance == null) {
            throw new ProtocolException(ProtocolError.UNSUPPORTED_VERSION);
        }
        return instance;
    }
}
