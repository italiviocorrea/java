package eti.italiviocorrea.api.paymentmethodsserver.adapters.inbounds.services;

import eti.italiviocorrea.api.paymentmethodsserver.adapters.config.database.payments.repository.PaymentMethodsRepository;
import eti.italiviocorrea.api.paymentmethodsserver.domain.entities.PaymentMethods;
import eti.italiviocorrea.api.paymentmethodsserver.domain.ports.PaymentMethodsQueryPort;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Primary
public class PaymentMethodsQuery implements PaymentMethodsQueryPort {

    private final PaymentMethodsRepository repository;

    public PaymentMethodsQuery(PaymentMethodsRepository repository) {
        this.repository = repository;
    }

    @Override
    public Flux<PaymentMethods> findAll() {
        return repository.findAll();
    }

    @Override
    public Mono<PaymentMethods> findById(int id) {
        return repository.findById(id);
    }

}
