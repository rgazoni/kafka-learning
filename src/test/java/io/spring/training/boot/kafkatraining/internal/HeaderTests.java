package io.spring.training.boot.kafkatraining.internal;

import io.spring.training.boot.kafkatraining.internal.header.Header;
import io.spring.training.boot.kafkatraining.internal.header.HeaderModel;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

@EnableAutoConfiguration
@SpringBootTest
public class HeaderTests {


    @Test
    public void parsingHeader() throws IOException {
        int expectedCorrelationId = 7;

        byte[] b = {0, 0, 0, 4, 0, 0, 0, 7};
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(b));

        Header h = new Header(dis);
        HeaderModel hm = h.parse();

        assertEquals(expectedCorrelationId, hm.correlationId());
    }
}
