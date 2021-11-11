package eti.italiviocorrea.api.rsocket.lcr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.blockhound.BlockHound;
import reactor.core.publisher.Hooks;

@SpringBootApplication
public class ApiLcrApplication {

    public static void main(String[] args) {
//        BlockHound.install();
        Hooks.onErrorDropped(throwable -> {});
        SpringApplication.run(ApiLcrApplication.class, args);
    }

}
