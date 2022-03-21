package eti.italiviocorrea.api.paymentmethodsserver.domain.ports;

import eti.italiviocorrea.api.paymentmethodsserver.domain.entities.PaymentMethods;
import reactor.core.publisher.Mono;

public interface PaymentMethodsCmdPort {

    Mono<PaymentMethods> insert(PaymentMethods paymentMethods);

    Mono<PaymentMethods> update(int id, PaymentMethods paymentMethods);

    Mono<Void> delete(int id);

}
