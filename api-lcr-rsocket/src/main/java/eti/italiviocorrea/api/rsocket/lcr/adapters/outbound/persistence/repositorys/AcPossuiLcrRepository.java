package eti.italiviocorrea.api.rsocket.lcr.adapters.outbound.persistence.repositorys;

import eti.italiviocorrea.api.rsocket.lcr.adapters.outbound.persistence.r2dbc.MssqlAcPossuiLcrRepository;
import eti.italiviocorrea.api.rsocket.lcr.application.ports.outbound.AcPossuiLcrRepositoryPort;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Primary
public class AcPossuiLcrRepository implements AcPossuiLcrRepositoryPort {

    private final MssqlAcPossuiLcrRepository repository;

    public AcPossuiLcrRepository(MssqlAcPossuiLcrRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<Void> inserirAcPossuiLcr(Integer iditc, Integer idlcr) {
        return repository.inserirAcPossuiLcr(iditc,idlcr);
    }

}
