package eti.italiviocorrea.api.rsocket.lcr.adapters.inbound.controllers.rsocket;

import eti.italiviocorrea.api.rsocket.lcr.adapters.inbound.controllers.IAPILcrController;
import eti.italiviocorrea.api.rsocket.lcr.adapters.inbound.dtos.RequisicaoDTO;
import eti.italiviocorrea.api.rsocket.lcr.adapters.inbound.dtos.RespostaDTO;
import eti.italiviocorrea.api.rsocket.lcr.adapters.utils.CertificateDigitalUtil;
import eti.italiviocorrea.api.rsocket.lcr.adapters.utils.X509CertificateWrapper;
import eti.italiviocorrea.api.rsocket.lcr.application.domain.AutoridadeCertificadora;
import eti.italiviocorrea.api.rsocket.lcr.application.domain.DadosCertificado;
import eti.italiviocorrea.api.rsocket.lcr.application.domain.RespostaValidacao;
import eti.italiviocorrea.api.rsocket.lcr.application.ports.inboud.AutoridadeCertificadoraUseCasePort;
import eti.italiviocorrea.api.rsocket.lcr.application.ports.inboud.ListaCertificadoRevogadoUseCasePort;
import eti.italiviocorrea.api.rsocket.lcr.application.usecases.rules.certificado.transmissor.CertificadoTransmissorSupervisor;
import io.opentelemetry.extension.annotations.WithSpan;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;
import reactor.util.Loggers;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@Controller
public class APILcrRsocketController implements IAPILcrController {

    private static final reactor.util.Logger LOGGER = Loggers.getLogger(CertificadoTransmissorSupervisor.class);

    @Autowired
    CertificadoTransmissorSupervisor certificadoTransmissorSupervisor;

    @Autowired
    ListaCertificadoRevogadoUseCasePort listaCertificadoRevogadoUseCasePort;

    @Autowired
    AutoridadeCertificadoraUseCasePort autoridadeCertificadoraUseCasePort;

    @Autowired
    ExecutorService cachedThreadPool;

    @WithSpan
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

    @WithSpan
    @MessageMapping("api-lcr.buscarAcPorNome.{nome}")
    public Mono<AutoridadeCertificadora> buscarAcPorCodigo(
            @DestinationVariable("nome") String nome) {
        return autoridadeCertificadoraUseCasePort.buscarPorNome(nome);
    }

    @WithSpan
    @MessageMapping("api-lcr.validar.certificado.transmissor")
    public Mono<RespostaDTO> validarCertificado(@Payload RequisicaoDTO requisicaoDTO) {

        //ExecutorService pool = Executors.newFixedThreadPool(10);

        Set<RespostaValidacao> respostas = new HashSet<>();

        DadosCertificado dadosCertificado = getDadosCertificado(requisicaoDTO.getCertificado());

        // Teste com uma lista de futuros
//        List<CompletableFuture<Set<RespostaValidacao>>> futures = new ArrayList<>();
//        for (int i = 0; i < 3; i++) {
//            futures.add(certificadoTransmissorSupervisor.validar(dadosCertificado));
//        }
//
//        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).thenAcceptAsync(result -> {
//            futures.parallelStream().map(CompletableFuture::join).collect(Collectors.toSet()).forEach(respostaValidacaos -> {
//                respostas.addAll(respostaValidacaos);
//            });
//        },cachedThreadPool).join();


        // Teste com chamadas normal
        CompletableFuture<Set<RespostaValidacao>> certificadoTransmissor
                = certificadoTransmissorSupervisor.validar(dadosCertificado);

        CompletableFuture.allOf(
                certificadoTransmissor
        ).thenAcceptAsync((unset) -> {
            respostas.addAll(certificadoTransmissor.join());
        },cachedThreadPool).join();

        if (respostas.size() > 1) {
            respostas.remove(RespostaValidacao.respOk());
        }

        RespostaValidacao respostaValidacao = null;

        LOGGER.info(respostas.toString());

        var resp = respostas.stream().findFirst();

        if (resp.isPresent()) {
            respostaValidacao = resp.get();
        }

        return Mono.just(RespostaDTO.builder()
                .cStatus(respostaValidacao.getCStat())
                .xMotivo(respostaValidacao.getXMotivo())
                .build());
    }

    @MessageExceptionHandler
    public Mono<RespostaDTO> handleException(Exception e) {
        return Mono.just(RespostaDTO.fromException(e));
    }

    private DadosCertificado getDadosCertificado(String certificado) {
        try {
            byte[] bencoded = Base64.decodeBase64(certificado.getBytes(StandardCharsets.UTF_8));
            X509Certificate cert = (new CertificateDigitalUtil()).getCertificate(new ByteArrayInputStream(bencoded));
            return (new X509CertificateWrapper(cert)).getDadosCertificado();
        } catch (Exception ex) {
            return null;
        }
    }

    private static <T> CompletableFuture<List<T>> listaRespostas(List<CompletableFuture<T>> futures) {
        CompletableFuture<Void> allDoneFuture =
                CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
        return allDoneFuture.thenApply(v ->
                futures.stream().
                        map(future -> future.join()).
                        collect(Collectors.<T>toList())
        );
    }
}
