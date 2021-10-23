package br.gov.ms.sefaz.sat.cotin.dfe.api.lcr.adapters.outbound.persistence.r2dbc;

import br.gov.ms.sefaz.sat.cotin.dfe.api.lcr.adapters.outbound.persistence.r2dbc.entities.AutoridadeCertificadoraEntity;
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
