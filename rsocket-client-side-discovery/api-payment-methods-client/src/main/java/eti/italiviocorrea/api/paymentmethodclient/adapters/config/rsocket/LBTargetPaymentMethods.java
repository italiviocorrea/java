package eti.italiviocorrea.api.paymentmethodclient.adapters.config.rsocket;

import eti.italiviocorrea.rsocket.csd.core.config.AbstractLoadBalanceTarget;
import eti.italiviocorrea.rsocket.csd.core.models.EndpointInstance;
import io.rsocket.loadbalance.LoadbalanceTarget;
import io.rsocket.transport.netty.client.TcpClientTransport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class LBTargetPaymentMethods extends AbstractLoadBalanceTarget {

    @Autowired
    private PaymentMethodsServiceRegistry paymentMethodsServiceRegistry;

    @Bean
    public Flux<List<LoadbalanceTarget>> lbtPaymentMethods() {
        return Mono.fromSupplier(() -> getEndpoints(paymentMethodsServiceRegistry))
                .repeatWhen(longFlux -> longFlux.delayElements(Duration.ofSeconds(1)))
                .log()
                .map(this::toLoadBalanceTarget);
    }

    private List<LoadbalanceTarget> toLoadBalanceTarget(List<EndpointInstance> rSocketServers) {
        return rSocketServers.stream()
               .map(server -> LoadbalanceTarget.from(server.getHost() + server.getPort(),
                TcpClientTransport.create(server.getHost(), server.getPort())))
                .collect(Collectors.toList());
    }
}
