package eti.italiviocorrea.api.paymentmethodsserver.adapters.outbounds.services;

import eti.italiviocorrea.api.paymentmethodsserver.adapters.config.database.payments.repository.PaymentMethodsRepository;
import eti.italiviocorrea.api.paymentmethodsserver.domain.entities.PaymentMethods;
import eti.italiviocorrea.api.paymentmethodsserver.domain.ports.PaymentMethodsCmdPort;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Primary
public class PaymentMethodsCmd implements PaymentMethodsCmdPort {

    private final PaymentMethodsRepository repository;

    public PaymentMethodsCmd(PaymentMethodsRepository repository) {
        this.repository = repository;
    }


    @Override
    public Mono<PaymentMethods> insert(PaymentMethods paymentMethods) {
        return repository.save(paymentMethods);
    }

    @Override
    public Mono<PaymentMethods> update(int id, PaymentMethods paymentMethods) {
        return repository.findById(id)
                .map(p -> {
                    p.setDescription(paymentMethods.getDescription());
                    p.setType(paymentMethods.getType());
                    p.setEffectiveDate(paymentMethods.getEffectiveDate());
                    p.setExpirationDate(paymentMethods.getExpirationDate());
                    return p;
                }).flatMap(p -> repository.save(p));
    }

    @Override
    public Mono<Void> delete(int id) {
        return repository.findById(id)
                .flatMap(p -> repository.delete(p));
    }
}
