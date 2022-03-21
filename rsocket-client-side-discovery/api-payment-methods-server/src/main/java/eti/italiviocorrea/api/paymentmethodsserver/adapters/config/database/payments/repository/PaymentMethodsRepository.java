package eti.italiviocorrea.api.paymentmethodsserver.adapters.config.database.payments.repository;

import eti.italiviocorrea.api.paymentmethodsserver.domain.entities.PaymentMethods;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentMethodsRepository extends ReactiveCrudRepository<PaymentMethods, Integer> {

}
