package eti.italiviocorrea.api.rsocket.lcr.adapters.inbound.persistence.repositorys;

import eti.italiviocorrea.api.rsocket.lcr.adapters.inbound.persistence.r2dbc.MssqlAutoridadeCertificadoraRepository;
import eti.italiviocorrea.api.rsocket.lcr.application.domain.AutoridadeCertificadora;
import eti.italiviocorrea.api.rsocket.lcr.application.ports.inboud.AutoridadeCertificadoraQueryPort;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Primary
public class AutoridadeCertificadoraQuery implements AutoridadeCertificadoraQueryPort {


    private final MssqlAutoridadeCertificadoraRepository repository;

    public AutoridadeCertificadoraQuery(MssqlAutoridadeCertificadoraRepository repository) {
        this.repository = repository;
    }

    @Autowired
    ModelMapper modelMapper;

    @Override
    public Mono<AutoridadeCertificadora> findByNome(String nome) {
        return repository.findByNome(nome).map(acEntity -> this.modelMapper.map(acEntity, AutoridadeCertificadora.class));
    }

}
