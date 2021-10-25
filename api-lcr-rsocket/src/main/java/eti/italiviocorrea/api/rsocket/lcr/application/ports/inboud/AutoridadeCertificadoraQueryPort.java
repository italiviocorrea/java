package eti.italiviocorrea.api.rsocket.lcr.application.ports.inboud;

import eti.italiviocorrea.api.rsocket.lcr.application.domain.AutoridadeCertificadora;
import reactor.core.publisher.Mono;

public interface AutoridadeCertificadoraQueryPort {

    Mono<AutoridadeCertificadora> findByNome(String nome);

}
