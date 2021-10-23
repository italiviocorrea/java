package br.gov.ms.sefaz.sat.cotin.dfe.api.lcr.application.ports.outbound;

import reactor.core.publisher.Mono;

public interface AcPossuiLcrRepositoryPort {

    Mono<Void> inserirAcPossuiLcr(Integer iditc, Integer idlcr);

}
