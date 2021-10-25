package eti.italiviocorrea.api.rsocket.lcr.adapters.inbound.controllers;


import eti.italiviocorrea.api.rsocket.lcr.adapters.inbound.dtos.RequisicaoDTO;
import eti.italiviocorrea.api.rsocket.lcr.adapters.inbound.dtos.RespostaDTO;
import reactor.core.publisher.Mono;

public interface IAPILcrController {

    Mono<RespostaDTO> incluirlcr(RequisicaoDTO requisicaoDTO);
}
