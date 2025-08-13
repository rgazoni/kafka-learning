package io.spring.training.boot.kafkatraining.apiKeys;

import io.spring.training.boot.kafkatraining.internal.header.HeaderModel;
import io.spring.training.boot.kafkatraining.internal.protocol.error.ProtocolException;
import io.spring.training.boot.kafkatraining.support.Samples;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

public class ApiKeyHandlerTests {


   @Test
   public void shouldThrownInvalidRequestException() {
       var v1 = Mockito.mock(RequestType.class);
       when(v1.getApiKey()).thenReturn((short)1);
       var v2 = Mockito.mock(RequestType.class);
       when(v2.getApiKey()).thenReturn((short)2);
       var v3 = Mockito.mock(RequestType.class);
       when(v3.getApiKey()).thenReturn((short)3);
       var v4 = Mockito.mock(RequestType.class);
       when(v4.getApiKey()).thenReturn((short)4);

       var apiKeyHandler = new ApiKeyHandler(List.of(v1, v2, v3, v4));

       var hm = HeaderModel.builder()
               .messageSize(8)
               .requestApiKey((short)16)
               .requestApiVersion((short)5)
               .correlationId(2)
               .build();

       assertThatThrownBy(() -> apiKeyHandler.redirect(hm, Samples.smallBody()))
               .isInstanceOf(ProtocolException.class)
               .hasMessage("INVALID_REQUEST");
   }

}
