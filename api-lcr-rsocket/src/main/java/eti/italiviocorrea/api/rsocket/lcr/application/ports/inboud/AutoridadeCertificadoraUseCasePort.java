package eti.italiviocorrea.api.rsocket.lcr.application.ports.inboud;

import eti.italiviocorrea.api.rsocket.lcr.application.domain.AutoridadeCertificadora;
import reactor.core.publisher.Mono;

public interface AutoridadeCertificadoraUseCasePort {

    Mono<AutoridadeCertificadora> buscarPorNome(String nome);

}
