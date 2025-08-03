package io.spring.training.boot.kafkatraining.apiKeys.apiVersions.v4;

import io.spring.training.boot.kafkatraining.apiKeys.Versionable;
import io.spring.training.boot.kafkatraining.apiKeys.apiVersions.ApiVersionsRequest;
import io.spring.training.boot.kafkatraining.internal.header.HeaderModel;
import io.spring.training.boot.kafkatraining.internal.protocolError.ProtocolError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

@Service
public class ApiVersionRequestV4 implements ApiVersionsRequest {
    private final List<Versionable<Object>> versionableList;

    @Autowired
    public ApiVersionRequestV4(List<Versionable<Object>> versionableList) {
        this.versionableList = versionableList;
    }

    @Override
    public void execute(HeaderModel hm, byte[] body) {

        // - 4 bytes -> message length
        // - correlation ID in the response header matches the correlation ID in the
        // request
        // - error code in the response body is 0 (No error)
        // - contains at least one entry for the API key 18 (API_VERSIONS)
        // - The MaxVersion for the ApiKey 18 is at least 4

        // ApiVersions Response (Version: 3) => error_code [api_keys] throttle_time_ms _tagged_fields
        //  error_code => INT16
        //  api_keys => api_key min_version max_version _tagged_fields
        //    api_key => INT16
        //    min_version => INT16
        //    max_version => INT16
        //  throttle_time_ms => INT32

        int corrId = hm.correlationId();
        int errorCode = ProtocolError.NONE.code();
//
//        versionableList.stream()
//                .collect(Collectors.toMap())


        ByteBuffer buf = ByteBuffer
                .allocate(2)
                .order(ByteOrder.BIG_ENDIAN)
                .putShort((short) 0);
    }

    @Override
    public short getVersion() {
        return 4;
    }
}
