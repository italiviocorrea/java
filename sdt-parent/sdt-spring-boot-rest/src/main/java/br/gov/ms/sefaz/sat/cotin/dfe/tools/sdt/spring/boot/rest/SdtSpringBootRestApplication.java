package br.gov.ms.sefaz.sat.cotin.dfe.tools.sdt.spring.boot.rest;

import br.gov.ms.sefaz.sat.cotin.dfe.tools.sdt.repositorio.ProjetoCommandRepositoryPortImpl;
import br.gov.ms.sefaz.sat.cotin.dfe.tools.sdt.repositorio.ProjetoQueryRepositoyPortImpl;
import br.gov.ms.sefaz.sat.cotin.dfe.tools.sdt.usecase.ProjetoCommand;
import br.gov.ms.sefaz.sat.cotin.dfe.tools.sdt.usecase.ProjetoQuery;
import br.gov.ms.sefaz.sat.cotin.dfe.tools.sdt.usecase.ports.ProjetoCommandRepositoryPort;
import br.gov.ms.sefaz.sat.cotin.dfe.tools.sdt.usecase.ports.ProjetoQueryRepositoryPort;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SdtSpringBootRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SdtSpringBootRestApplication.class, args);
    }

    @Bean
    public ProjetoCommand projetoCommand(ProjetoCommandRepositoryPort projetoCommandRepositoryPort, ProjetoQueryRepositoryPort queryRepositoryPort) {
        return new ProjetoCommand(projetoCommandRepositoryPort, queryRepositoryPort);
    }

    @Bean
    public ProjetoQuery projetoQuery(ProjetoQueryRepositoryPort queryRepositoryPort) {
        return new ProjetoQuery(queryRepositoryPort);
    }

    @Bean
    public ProjetoCommandRepositoryPort projetoCommandRepositoryPort(){
        return new ProjetoCommandRepositoryPortImpl();
    }

    @Bean
    public ProjetoQueryRepositoryPort queryRepositoryPort(){
        return new ProjetoQueryRepositoyPortImpl();
    }

}
