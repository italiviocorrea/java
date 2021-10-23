package br.gov.ms.sefaz.sat.cotin.dfe.api.lcr.domain.ports.outbound;

import reactor.core.publisher.Mono;

public interface AcPossuiLcrRepositoryPort {

    Mono<Void> inserirAcPossuiLcr(Integer iditc, Integer idlcr);

}
