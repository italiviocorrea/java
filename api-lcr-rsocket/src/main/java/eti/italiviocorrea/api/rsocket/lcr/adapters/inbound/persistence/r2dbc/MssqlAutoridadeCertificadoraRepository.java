package eti.italiviocorrea.api.rsocket.lcr.adapters.inbound.persistence.r2dbc;

import eti.italiviocorrea.api.rsocket.lcr.adapters.inbound.persistence.r2dbc.entities.AutoridadeCertificadoraEntity;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface MssqlAutoridadeCertificadoraRepository
        extends ReactiveCrudRepository<AutoridadeCertificadoraEntity, Integer> {

//    @CircuitBreaker(name = "nf3edb")
    Mono<AutoridadeCertificadoraEntity> findByNome(@Param("nome") String nome);

}
