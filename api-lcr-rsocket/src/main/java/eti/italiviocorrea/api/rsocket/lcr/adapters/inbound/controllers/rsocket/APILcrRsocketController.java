package eti.italiviocorrea.api.rsocket.lcr.adapters.inbound.controllers.rsocket;

import eti.italiviocorrea.api.rsocket.lcr.adapters.inbound.controllers.IAPILcrController;
import eti.italiviocorrea.api.rsocket.lcr.adapters.inbound.dtos.RequisicaoDTO;
import eti.italiviocorrea.api.rsocket.lcr.adapters.inbound.dtos.RespostaDTO;
import eti.italiviocorrea.api.rsocket.lcr.application.domain.AutoridadeCertificadora;
import eti.italiviocorrea.api.rsocket.lcr.application.ports.inboud.AutoridadeCertificadoraUseCasePort;
import eti.italiviocorrea.api.rsocket.lcr.application.ports.inboud.ListaCertificadoRevogadoUseCasePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
public class APILcrRsocketController implements IAPILcrController {

    @Autowired
    ListaCertificadoRevogadoUseCasePort listaCertificadoRevogadoUseCasePort;

    @Autowired
    AutoridadeCertificadoraUseCasePort autoridadeCertificadoraUseCasePort;

    @Override
    @MessageMapping("api-lcr.incluir")
    public Mono<RespostaDTO> incluirlcr(@Payload RequisicaoDTO requisicaoDTO) {
        return listaCertificadoRevogadoUseCasePort
                .incluirLcr(
                        requisicaoDTO.getNomeAC(),
                        requisicaoDTO.getUrlLcr())
                .thenReturn(RespostaDTO.builder()
                        .cStatus("100")
                        .xMotivo("LCR incluida com sucesso!").build());
    }

    @MessageMapping("api-lcr.buscarAcPorNome.{nome}")
    public Mono<AutoridadeCertificadora> buscarAcPorCodigo(
            @DestinationVariable("nome") String nome) {
        return autoridadeCertificadoraUseCasePort.buscarPorNome(nome);
    }

    @MessageExceptionHandler
    public Mono<RespostaDTO> handleException(Exception e) {
        return Mono.just(RespostaDTO.fromException(e));
    }

}
