package br.gov.ms.sefaz.sat.cotin.dfe.api.lcr.domain.ports.inboud;


import reactor.core.publisher.Mono;

public interface ListaCertificadoRevogadoUseCasePort {

    Mono<Void> incluirLcr(String nomeAC, String urlLcr) ;

}
