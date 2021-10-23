package br.gov.ms.sefaz.sat.cotin.dfe.api.lcr.adapters.outbound.persistence.repositorys;

import br.gov.ms.sefaz.sat.cotin.dfe.api.lcr.adapters.outbound.persistence.r2dbc.MssqlAutoridadeCertificadoraRepository;
import br.gov.ms.sefaz.sat.cotin.dfe.api.lcr.domain.models.AutoridadeCertificadora;
import br.gov.ms.sefaz.sat.cotin.dfe.api.lcr.domain.ports.outbound.AutoridadeCertificadoraRepositoryPort;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Primary
public class AutoridadeCertificadoraRepository implements AutoridadeCertificadoraRepositoryPort {


    private final MssqlAutoridadeCertificadoraRepository repository;

    public AutoridadeCertificadoraRepository(MssqlAutoridadeCertificadoraRepository repository) {
        this.repository = repository;
    }

    @Autowired
    ModelMapper modelMapper;

    @Override
    public Mono<AutoridadeCertificadora> findByNome(String nome) {
        return repository.findByNome(nome).map(acEntity -> this.modelMapper.map(acEntity, AutoridadeCertificadora.class));
    }

}
