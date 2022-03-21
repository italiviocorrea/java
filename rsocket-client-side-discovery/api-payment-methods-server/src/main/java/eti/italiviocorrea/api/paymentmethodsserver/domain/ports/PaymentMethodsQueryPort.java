package eti.italiviocorrea.api.paymentmethodsserver.domain.ports;

import eti.italiviocorrea.api.paymentmethodsserver.domain.entities.PaymentMethods;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PaymentMethodsQueryPort {

    Flux<PaymentMethods> findAll();

    Mono<PaymentMethods> findById(int id);


}
