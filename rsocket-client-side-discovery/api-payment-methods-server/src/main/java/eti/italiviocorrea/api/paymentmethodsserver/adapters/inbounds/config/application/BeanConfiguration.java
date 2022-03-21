package eti.italiviocorrea.api.paymentmethodsserver.adapters.inbounds.config.application;

import eti.italiviocorrea.api.paymentmethodsserver.ApiPaymentMethodsServerApplication;
import eti.italiviocorrea.api.paymentmethodsserver.domain.ports.PaymentMethodsCmdPort;
import eti.italiviocorrea.api.paymentmethodsserver.domain.ports.PaymentMethodsQueryPort;
import eti.italiviocorrea.api.paymentmethodsserver.domain.usecases.PaymentMethodsUseCaseCmd;
import eti.italiviocorrea.api.paymentmethodsserver.domain.usecases.PaymentMethodsUseCaseQry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = ApiPaymentMethodsServerApplication.class)
public class BeanConfiguration {

    @Bean
    PaymentMethodsUseCaseQry paymentMethodsUseCaseQry(PaymentMethodsQueryPort paymentMethodsQueryPort){
        return new PaymentMethodsUseCaseQry(paymentMethodsQueryPort);
    }

    @Bean
    PaymentMethodsUseCaseCmd paymentMethodsUseCaseCmd(PaymentMethodsCmdPort paymentMethodsCmdPort){
        return new PaymentMethodsUseCaseCmd(paymentMethodsCmdPort);
    }
}
