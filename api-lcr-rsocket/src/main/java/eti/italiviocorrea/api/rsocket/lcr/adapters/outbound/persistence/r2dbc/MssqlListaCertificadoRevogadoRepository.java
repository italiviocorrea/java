package eti.italiviocorrea.api.rsocket.lcr.adapters.outbound.persistence.r2dbc;

import eti.italiviocorrea.api.rsocket.lcr.adapters.outbound.persistence.r2dbc.entities.ListaCertificadoRevogadoEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface MssqlListaCertificadoRevogadoRepository
        extends ReactiveCrudRepository<ListaCertificadoRevogadoEntity, Integer> {

//    @CircuitBreaker(name = "nf3edb")
    @Query("select idlcr as id, indi_lcr_delta as indiLcrDelta"
            + " from dfe_lista_certificado_revogado"
            + " where info_url_lcr =  :infoUrlLcr")
    Mono<ListaCertificadoRevogadoEntity> findStatusListaByUrl(@Param("infoUrlLcr") String url);

//    @CircuitBreaker(name = "nf3edb")
    @Query(" select idlcr as id"
            + "  from dfe_lista_certificado_revogado"
            + "  where  info_url_lcr =  :infoUrlLcr"
            + "    and Exists (select 1 "
            + "                 from dfe_certificado_revogado"
            + "                 where numero_serie =  :numeroSerie"
            + "                   and idLcr = dfe_lista_certificado_revogado.idlcr)")
    Mono<ListaCertificadoRevogadoEntity> findByUrlESerialNumber(
            @Param("infoUrlLcr") String url,
            @Param("numeroSerie") String serialNumber);


}
