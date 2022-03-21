package eti.italiviocorrea.api.paymentmethodsserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Hooks;

@SpringBootApplication
public class ApiPaymentMethodsServerApplication {

    public static void main(String[] args) {
        Hooks.onErrorDropped(throwable -> {});
        SpringApplication.run(ApiPaymentMethodsServerApplication.class, args);
    }

}
