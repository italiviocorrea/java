package eti.italiviocorrea.api.rsocket.lcr.application.ports.inboud;


import reactor.core.publisher.Mono;

public interface ListaCertificadoRevogadoUseCasePort {

    Mono<Void> incluirLcr(String nomeAC, String urlLcr) ;

}
