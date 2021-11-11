package eti.italiviocorrea.api.rsocket.lcr.adapters.configs;

import com.hazelcast.config.MapConfig;
import com.hazelcast.core.HazelcastInstance;
import eti.italiviocorrea.api.rsocket.lcr.ApiLcrApplication;
import eti.italiviocorrea.api.rsocket.lcr.application.ports.inboud.AutoridadeCertificadoraQueryPort;
import eti.italiviocorrea.api.rsocket.lcr.application.ports.outbound.AcPossuiLcrCommandPort;
import eti.italiviocorrea.api.rsocket.lcr.application.ports.outbound.ListaCertificadoRevogadoCommandPort;
import eti.italiviocorrea.api.rsocket.lcr.application.usecases.AutoridadeCertificadoraUseCase;
import eti.italiviocorrea.api.rsocket.lcr.application.usecases.ListaCertificadoRevogadoUseCase;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = ApiLcrApplication.class)
public class BeanConfiguration {

    @Bean
    ListaCertificadoRevogadoUseCase listaCertificadoRevogadoUseCase(AutoridadeCertificadoraQueryPort autoridadeCertificadoraRepositoryPort,
                                                                    ListaCertificadoRevogadoCommandPort listaCertificadoRevogadoRepositoryPort,
                                                                    AcPossuiLcrCommandPort acPossuiLcrRepositoryPort) {
        return new ListaCertificadoRevogadoUseCase(autoridadeCertificadoraRepositoryPort,
                listaCertificadoRevogadoRepositoryPort,
                acPossuiLcrRepositoryPort);
    }

    @Bean
    AutoridadeCertificadoraUseCase autoridadeCertificadoraUseCase(HazelcastInstance instance,
                                                                  AutoridadeCertificadoraQueryPort autoridadeCertificadoraRepositoryPort) {
        return new AutoridadeCertificadoraUseCase(instance.getMap("acs"), autoridadeCertificadoraRepositoryPort);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
