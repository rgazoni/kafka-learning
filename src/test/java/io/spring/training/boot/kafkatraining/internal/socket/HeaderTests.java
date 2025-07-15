package io.spring.training.boot.kafkatraining.internal.socket;

import io.spring.training.boot.kafkatraining.internal.Header;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

@EnableAutoConfiguration
@SpringBootTest
public class HeaderTests {


    @Test
    public void parsingHeader() {
        String request = "\\x00\\x00\\x00\\x04\\x00\\x00\\x00\\x01";
        byte[] b = request.getBytes();
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(b));

        System.out.println(b);

       Header h = new Header(dis);


    }
}
