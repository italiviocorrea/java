package eti.italiviocorrea.api.rsocket.lcr.application.ports.outbound;

import reactor.core.publisher.Mono;

public interface AcPossuiLcrCommandPort {

    Mono<Void> inserirAcPossuiLcr(Integer iditc, Integer idlcr);

}
