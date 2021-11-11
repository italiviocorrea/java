package eti.italiviocorrea.api.rsocket.lcr.adapters.inbound.controllers;


import eti.italiviocorrea.api.rsocket.lcr.adapters.inbound.dtos.RequisicaoDTO;
import eti.italiviocorrea.api.rsocket.lcr.adapters.inbound.dtos.RespostaDTO;
import eti.italiviocorrea.api.rsocket.lcr.application.domain.AutoridadeCertificadora;
import reactor.core.publisher.Mono;

public interface IAPILcrController {

    Mono<RespostaDTO> incluirlcr(RequisicaoDTO requisicaoDTO);
    Mono<AutoridadeCertificadora> buscarAcPorCodigo(String nome);

}
