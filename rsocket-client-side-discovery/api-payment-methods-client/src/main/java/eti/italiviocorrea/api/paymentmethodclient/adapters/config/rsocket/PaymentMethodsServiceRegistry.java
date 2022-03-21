package eti.italiviocorrea.api.paymentmethodclient.adapters.config.rsocket;

import eti.italiviocorrea.rsocket.csd.core.services.ServiceRegistry;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@ConfigurationProperties(prefix = "app.rsocket.client.payment")
public class PaymentMethodsServiceRegistry extends ServiceRegistry {

}
