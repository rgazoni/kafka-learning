package io.spring.training.boot.kafkatraining.apiKeys.produce;

import io.spring.training.boot.kafkatraining.apiKeys.produce.v0.ProduceRequestV0;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.TreeSet;

public class ProducerTests {

    @Test
    public void v0_shouldExposeVersionZero() {
        assertThat(ProduceRequestV0.VERSION).isZero();
        assertThat(new ProduceRequestV0().getVersion()).isZero();
    }

   @Test
   public void shouldListProducersSupportedVersions() {
       var v1 = Mockito.mock(ProduceRequest.class);
       when(v1.getVersion()).thenReturn((short)1);
       var v2 = Mockito.mock(ProduceRequest.class);
       when(v2.getVersion()).thenReturn((short)2);

       Producer producer = new Producer(List.of(v1, v2));

       assertThat(producer.getRangeOfSupportedVersions())
               .hasSize(2);

       assertThat(producer.getRangeOfSupportedVersions())
               .isEqualTo(new TreeSet<>(List.of((short)1,(short)2)));
   }

}
