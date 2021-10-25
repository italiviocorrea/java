package eti.italiviocorrea.api.rsocket.lcr.application.ports.outbound;

import reactor.core.publisher.Mono;

public interface AcPossuiLcrRepositoryPort {

    Mono<Void> inserirAcPossuiLcr(Integer iditc, Integer idlcr);

}
