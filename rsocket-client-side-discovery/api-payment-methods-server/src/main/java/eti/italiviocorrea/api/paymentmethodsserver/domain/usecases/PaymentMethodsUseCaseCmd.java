package eti.italiviocorrea.api.paymentmethodsserver.domain.usecases;

import eti.italiviocorrea.api.paymentmethodsserver.domain.entities.PaymentMethods;
import eti.italiviocorrea.api.paymentmethodsserver.domain.ports.PaymentMethodsCmdPort;
import reactor.core.publisher.Mono;

public class PaymentMethodsUseCaseCmd implements PaymentMethodsCmdPort {


    private final PaymentMethodsCmdPort paymentMethodsCmdPort;

    public PaymentMethodsUseCaseCmd(PaymentMethodsCmdPort paymentMethodsCmdPort) {
        this.paymentMethodsCmdPort = paymentMethodsCmdPort;
    }

    @Override
    public Mono<PaymentMethods> insert(PaymentMethods paymentMethods) {
        return paymentMethodsCmdPort.insert(paymentMethods);
    }

    @Override
    public Mono<PaymentMethods> update(int id, PaymentMethods paymentMethods) {
        return paymentMethodsCmdPort.update(id, paymentMethods);
    }

    @Override
    public Mono<Void> delete(int id) {
        return paymentMethodsCmdPort.delete(id);
    }
}
