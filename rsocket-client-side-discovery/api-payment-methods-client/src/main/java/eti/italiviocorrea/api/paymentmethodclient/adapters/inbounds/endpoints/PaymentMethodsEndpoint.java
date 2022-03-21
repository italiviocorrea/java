package eti.italiviocorrea.api.paymentmethodclient.adapters.inbounds.endpoints;

import eti.italiviocorrea.api.paymentmethodclient.domain.entities.PaymentMethods;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class PaymentMethodsEndpoint {

    @Autowired
    @Qualifier(value = "rsrPaymentMethods")
    private RSocketRequester rsrPaymentMethods;

    @GetMapping(value = "/paymentmethods", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<PaymentMethods> findAll() {
        return this.rsrPaymentMethods.route("payment-methods.findAll")
                .retrieveFlux(PaymentMethods.class)
                .retry(5)
                .log();
    }

    @GetMapping(value = "/paymentmethods/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<PaymentMethods> findById(@PathVariable int id) {
        return this.rsrPaymentMethods.route("payment-methods.findById."+id)
                .retrieveMono(PaymentMethods.class)
                .retry(5)
                .log();
    }

}
