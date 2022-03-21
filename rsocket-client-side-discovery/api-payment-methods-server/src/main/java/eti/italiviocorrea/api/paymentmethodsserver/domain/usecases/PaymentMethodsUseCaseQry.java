package eti.italiviocorrea.api.paymentmethodsserver.domain.usecases;

import eti.italiviocorrea.api.paymentmethodsserver.domain.entities.PaymentMethods;
import eti.italiviocorrea.api.paymentmethodsserver.domain.ports.PaymentMethodsQueryPort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class PaymentMethodsUseCaseQry implements PaymentMethodsQueryPort {


    private final PaymentMethodsQueryPort paymentMethodsQueryPort;

    public PaymentMethodsUseCaseQry(PaymentMethodsQueryPort paymentMethodsQueryPort) {
        this.paymentMethodsQueryPort = paymentMethodsQueryPort;
    }

    @Override
    public Flux<PaymentMethods> findAll() {
        return paymentMethodsQueryPort.findAll();
    }

    @Override
    public Mono<PaymentMethods> findById(int id) {
        return paymentMethodsQueryPort.findById(id);
    }

}
