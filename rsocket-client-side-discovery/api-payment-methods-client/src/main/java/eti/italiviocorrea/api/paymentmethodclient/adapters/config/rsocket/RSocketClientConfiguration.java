package eti.italiviocorrea.api.paymentmethodclient.adapters.config.rsocket;

import io.rsocket.loadbalance.LoadbalanceTarget;
import io.rsocket.loadbalance.RoundRobinLoadbalanceStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.util.MimeTypeUtils;
import reactor.core.publisher.Flux;

import java.util.List;

@Configuration
@Slf4j
public class RSocketClientConfiguration {

    @Bean
    @Qualifier(value = "rsrPaymentMethods")
    public RSocketRequester rsrPaymentMethods(RSocketRequester.Builder builder, LBTargetPaymentMethods lbTargetPaymentMethods) {
        Flux<List<LoadbalanceTarget>> targetFlux = lbTargetPaymentMethods.lbtPaymentMethods();
        return builder.dataMimeType(MimeTypeUtils.APPLICATION_JSON)
                .transports(targetFlux, new RoundRobinLoadbalanceStrategy());
    }
}
