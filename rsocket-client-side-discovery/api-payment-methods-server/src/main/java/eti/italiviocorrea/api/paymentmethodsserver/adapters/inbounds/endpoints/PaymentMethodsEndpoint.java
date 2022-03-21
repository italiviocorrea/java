package eti.italiviocorrea.api.paymentmethodsserver.adapters.inbounds.endpoints;

import eti.italiviocorrea.api.paymentmethodsserver.domain.entities.PaymentMethods;
import eti.italiviocorrea.api.paymentmethodsserver.domain.ports.PaymentMethodsCmdPort;
import eti.italiviocorrea.api.paymentmethodsserver.domain.ports.PaymentMethodsQueryPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class PaymentMethodsEndpoint {

    @Autowired
    PaymentMethodsQueryPort paymentMethodsQueryPort;

    @Autowired
    PaymentMethodsCmdPort paymentMethodsCmdPort;

    @MessageMapping("payment-methods.ping")
    public Mono<String> ping() {
        return Mono.just("OK");
    }

    @MessageMapping("payment-methods.findAll")
    public Flux<PaymentMethods> findAll() {
        return paymentMethodsQueryPort.findAll().log();
    }

    @MessageMapping("payment-methods.findById.{id}")
    public Mono<PaymentMethods> findById(@DestinationVariable("id") int id) {
        return paymentMethodsQueryPort.findById(id).log();
    }

    @MessageMapping("payment-methods.insert")
    public Mono<PaymentMethods> insert(@Payload PaymentMethods paymentMethods) {
        return paymentMethodsCmdPort.insert(paymentMethods);
    }

    @MessageMapping("payment-methods.update")
    public Mono<PaymentMethods> update(@DestinationVariable("id") int id,
                                          @Payload PaymentMethods paymentMethods) {
        return paymentMethodsCmdPort.update(id, paymentMethods);
    }

    @MessageMapping("payment-methods.delete")
    public Mono<Void> delete(@DestinationVariable("id") int id) {
        return paymentMethodsCmdPort.delete(id);
    }

}
