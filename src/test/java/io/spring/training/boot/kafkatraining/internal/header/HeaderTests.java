package io.spring.training.boot.kafkatraining.internal.header;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

@EnableAutoConfiguration
@SpringBootTest
public class HeaderTests {

    // echo -ne '\x00\x00\x00\x04\x00\x00\x00\x07\x00\x17\x00\x02\x00\x08\x00\x0A' | nc 127.0.0.1 9092 | hexdump -C
    @Test
    public void parsingHeader() {
        int expectedCorrelationId = 7;

        byte[] b = {0, 0, 0, 4, 0, 0, 0, 7};
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(b));

        HeaderModel hm = Header.getInstance().parse(dis);

        assertEquals(expectedCorrelationId, hm.correlationId());
    }
}
