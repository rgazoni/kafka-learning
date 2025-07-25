package io.spring.training.boot.kafkatraining.apiKeys.apiVersions;

import io.spring.training.boot.kafkatraining.apiKeys.apiVersions.v4.ApiVersionRequestV4;
import io.spring.training.boot.kafkatraining.internal.protocolError.ProtocolError;
import io.spring.training.boot.kafkatraining.internal.protocolError.ProtocolException;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public enum SupportedApiVersionsVersions {

    V0((short) 4, new ApiVersionRequestV4());

    private final ApiVersions objectVersion;
    private final short version;

    SupportedApiVersionsVersions(short version, ApiVersions objectVersion) {
        this.version = version;
        this.objectVersion = objectVersion;
    }

    private static final Map<Short, ApiVersions> VERSION_MAP =
            Arrays.stream(values())
                    .collect(Collectors.toMap(SupportedApiVersionsVersions::getVersion,
                            SupportedApiVersionsVersions::getObjectVersion));

    public static ApiVersions fromVersion(short v) {
        ApiVersions instance = VERSION_MAP.get(v);
        if (instance == null) {
            throw new ProtocolException(ProtocolError.UNSUPPORTED_VERSION);
        }
        return instance;
    }

}
