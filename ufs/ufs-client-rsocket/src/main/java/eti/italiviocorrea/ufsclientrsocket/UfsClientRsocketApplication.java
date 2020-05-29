package eti.italiviocorrea.ufsclientrsocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.util.MimeTypeUtils;

@SpringBootApplication
public class UfsClientRsocketApplication {

    public static void main(String[] args) {

        SpringApplication.run(UfsClientRsocketApplication.class, args);
    }

    @Bean
    public RSocketRequester rSocketRequester(RSocketRequester.Builder b) {
        return b.dataMimeType(MimeTypeUtils.APPLICATION_JSON)
                .connectTcp( "localhost", 7000)
                .block();
    }
}
