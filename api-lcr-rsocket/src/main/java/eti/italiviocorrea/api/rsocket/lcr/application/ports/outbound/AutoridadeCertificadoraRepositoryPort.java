package eti.italiviocorrea.api.rsocket.lcr.application.ports.outbound;

import eti.italiviocorrea.api.rsocket.lcr.application.domain.AutoridadeCertificadora;
import reactor.core.publisher.Mono;

public interface AutoridadeCertificadoraRepositoryPort {

    Mono<AutoridadeCertificadora> findByNome(String nome);

}
