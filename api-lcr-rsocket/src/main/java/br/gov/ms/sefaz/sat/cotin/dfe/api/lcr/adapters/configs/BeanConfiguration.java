package br.gov.ms.sefaz.sat.cotin.dfe.api.lcr.adapters.configs;

import br.gov.ms.sefaz.sat.cotin.dfe.api.lcr.ApiLcrApplication;
import br.gov.ms.sefaz.sat.cotin.dfe.api.lcr.domain.ports.outbound.AcPossuiLcrRepositoryPort;
import br.gov.ms.sefaz.sat.cotin.dfe.api.lcr.domain.ports.outbound.AutoridadeCertificadoraRepositoryPort;
import br.gov.ms.sefaz.sat.cotin.dfe.api.lcr.domain.ports.outbound.ListaCertificadoRevogadoRepositoryPort;
import br.gov.ms.sefaz.sat.cotin.dfe.api.lcr.domain.usecases.ListaCertificadoRevogadoUseCase;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = ApiLcrApplication.class)
public class BeanConfiguration {

    @Bean
    ListaCertificadoRevogadoUseCase listaCertificadoRevogadoUseCase(AutoridadeCertificadoraRepositoryPort autoridadeCertificadoraRepositoryPort,
                                                                    ListaCertificadoRevogadoRepositoryPort listaCertificadoRevogadoRepositoryPort,
                                                                    AcPossuiLcrRepositoryPort acPossuiLcrRepositoryPort) {
        return new ListaCertificadoRevogadoUseCase(autoridadeCertificadoraRepositoryPort,
                listaCertificadoRevogadoRepositoryPort,
                acPossuiLcrRepositoryPort);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
