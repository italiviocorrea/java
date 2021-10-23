package br.gov.ms.sefaz.sat.cotin.dfe.api.lcr.adapters.outbound.persistence.r2dbc;

import br.gov.ms.sefaz.sat.cotin.dfe.api.lcr.adapters.outbound.persistence.r2dbc.entities.AcPossuiLcrEntity;
import br.gov.ms.sefaz.sat.cotin.dfe.api.lcr.adapters.outbound.persistence.r2dbc.entities.AcPossuiLcrPk;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface MssqlAcPossuiLcrRepository extends ReactiveCrudRepository<AcPossuiLcrEntity, AcPossuiLcrPk> {

    @Modifying
    @Query("insert into dfe_ac_possui_lcr(iditc,idlcr) values(:iditc,:idlcr)")
    Mono<Void> inserirAcPossuiLcr(Integer iditc, Integer idlcr);

}
