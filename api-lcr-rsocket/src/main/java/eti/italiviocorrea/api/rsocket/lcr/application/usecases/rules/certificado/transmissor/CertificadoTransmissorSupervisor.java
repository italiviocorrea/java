package eti.italiviocorrea.api.rsocket.lcr.application.usecases.rules.certificado.transmissor;

import com.hazelcast.map.IMap;
import eti.italiviocorrea.api.rsocket.lcr.application.domain.DadosCertificado;
import eti.italiviocorrea.api.rsocket.lcr.application.domain.RespostaValidacao;
import eti.italiviocorrea.api.rsocket.lcr.application.domain.ResultadoProcessamento;
import eti.italiviocorrea.api.rsocket.lcr.application.ports.inboud.AutoridadeCertificadoraQueryPort;
import eti.italiviocorrea.api.rsocket.lcr.application.ports.outbound.ListaCertificadoRevogadoCommandPort;
import io.opentelemetry.extension.annotations.WithSpan;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.util.Loggers;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

public class CertificadoTransmissorSupervisor {


    private static final reactor.util.Logger LOGGER = Loggers.getLogger(CertificadoTransmissorSupervisor.class);

    private final IMap<X509Certificate, X509Certificate> cacheValido;

    private final IMap<X509Certificate, X509Certificate> cacheInvalido;

    private ListaCertificadoRevogadoCommandPort listaCertificadoRevogadoRepository;

    private AutoridadeCertificadoraQueryPort autoridadeCertificadoraRepository;

    private final ExecutorService cachedThreadPool;

    public CertificadoTransmissorSupervisor(IMap<X509Certificate, X509Certificate> cacheValido,
                                            IMap<X509Certificate, X509Certificate> cacheInvalido,
                                            ListaCertificadoRevogadoCommandPort listaCertificadoRevogadoRepository,
                                            AutoridadeCertificadoraQueryPort autoridadeCertificadoraRepository,
                                            ExecutorService cachedThreadPool) {
        this.cacheValido = cacheValido;
        this.cacheInvalido = cacheInvalido;
        this.listaCertificadoRevogadoRepository = listaCertificadoRevogadoRepository;
        this.autoridadeCertificadoraRepository = autoridadeCertificadoraRepository;
        this.cachedThreadPool = cachedThreadPool;
    }

    @WithSpan
    public CompletableFuture<Set<RespostaValidacao>> validar(DadosCertificado dadosCertificado) {

        //ExecutorService pool2 = Executors.newFixedThreadPool(20);

        LOGGER.info(dadosCertificado.getCnpj());

        return CompletableFuture.supplyAsync(() -> {

            Set<RespostaValidacao> respostas = new HashSet<>();

            if (isCacheValido(dadosCertificado)) {
                respostas.add(RespostaValidacao.respOk());
                return respostas;
            }

            if (isCacheInvalido(dadosCertificado)) {
                respostas.add(RespostaValidacao.builder().cStat(ResultadoProcessamento.RP_280.getCStat())
                        .xMotivo(ResultadoProcessamento.RP_280.getXMotivo()).build());
                return respostas;
            }

            RespostaValidacao respRnA01Rej280 = new RnA01Rej280(dadosCertificado, cacheInvalido).get();
            if (respRnA01Rej280.isRejeicao()) {
                respostas.add(respRnA01Rej280);
                return respostas;
            }

            List<CompletableFuture<RespostaValidacao>> futuresRnTransmissor = new ArrayList<>();

            futuresRnTransmissor.add(CompletableFuture.supplyAsync(new RnA02Rej281(dadosCertificado, cacheInvalido)));
            futuresRnTransmissor.add(CompletableFuture.supplyAsync(new RnA07Rej282(dadosCertificado, cacheInvalido)));
            futuresRnTransmissor.add(CompletableFuture.supplyAsync(new RnA03Rej283(dadosCertificado, cacheInvalido, autoridadeCertificadoraRepository)));
            futuresRnTransmissor.add(CompletableFuture.supplyAsync(new RnA05Rej284(dadosCertificado, cacheInvalido, listaCertificadoRevogadoRepository)));
            futuresRnTransmissor.add(CompletableFuture.supplyAsync(new RnA06Rej285(dadosCertificado, cacheInvalido)));
            futuresRnTransmissor.add(CompletableFuture.supplyAsync(new RnA04Rej286(dadosCertificado, cacheInvalido, listaCertificadoRevogadoRepository)));

            CompletableFuture
                    .allOf(futuresRnTransmissor.toArray(new CompletableFuture[0]))
                    .thenAcceptAsync(result -> {
                        respostas.addAll(futuresRnTransmissor
                                .stream()
                                .map(CompletableFuture::join)
                                .collect(Collectors.toSet()));
                    }, cachedThreadPool).join();


            LOGGER.info("Respostas : " + respostas.toString());

            if (respostas.size() == 1 && respostas.contains(RespostaValidacao.respOk())) {
                cacheValido.put(dadosCertificado.getCertificate(), dadosCertificado.getCertificate());
            }
            return respostas;
        });
    }

    @WithSpan
    public Mono<Set<RespostaValidacao>> validarMono(DadosCertificado dadosCertificado) {

        Scheduler boundedElastic = Schedulers.boundedElastic();
        Scheduler parallel = Schedulers.parallel();

        Set<RespostaValidacao> respostas = new HashSet<>();

        if (isCacheValido(dadosCertificado)) {
            respostas.add(RespostaValidacao.respOk());
            return Mono.just(respostas);
        }

        if (isCacheInvalido(dadosCertificado)) {
            respostas.add(RespostaValidacao.builder().cStat(ResultadoProcessamento.RP_280.getCStat())
                    .xMotivo(ResultadoProcessamento.RP_280.getXMotivo()).build());
            return Mono.just(respostas);
        }

        RespostaValidacao respRnA01Rej280 = new RnA01Rej280(dadosCertificado, cacheInvalido).get();
        if (respRnA01Rej280.isRejeicao()) {
            respostas.add(respRnA01Rej280);
            return Mono.just(respostas);
        }

        Mono<RespostaValidacao> rnA07Rej282 = Mono.defer(() -> new RnA07Rej282Mono(dadosCertificado, cacheInvalido).get()).subscribeOn(parallel);
        Mono<RespostaValidacao> rnA06Rej285 = Mono.defer(() -> new RnA06Rej285Mono(dadosCertificado, cacheInvalido).get()).subscribeOn(parallel);

        Mono<RespostaValidacao> rnA02Rej281 = Mono.defer(() -> new RnA02Rej281Mono(dadosCertificado, cacheInvalido).get()).subscribeOn(parallel);
        Mono<RespostaValidacao> rnA03Rej283 = Mono.defer(() -> new RnA03Rej283Mono(dadosCertificado, cacheInvalido, autoridadeCertificadoraRepository).get()).subscribeOn(parallel);
        Mono<RespostaValidacao> rnA05Rej284 = Mono.defer(() -> new RnA05Rej284Mono(dadosCertificado, cacheInvalido, listaCertificadoRevogadoRepository).get()).subscribeOn(parallel);
        Mono<RespostaValidacao> rnA04Rej286 = Mono.defer(() -> new RnA04Rej286Mono(dadosCertificado, cacheInvalido, listaCertificadoRevogadoRepository).get()).subscribeOn(parallel);

        return Flux.concat(rnA02Rej281, rnA07Rej282, rnA06Rej285, rnA03Rej283, rnA05Rej284, rnA04Rej286)
                .publishOn(Schedulers.boundedElastic())
                .collectList()
                .flatMap(respostaValidacaos -> {
                    if (respostaValidacaos.size() == 1 && respostaValidacaos.contains(RespostaValidacao.respOk())) {
                        cacheValido.put(dadosCertificado.getCertificate(), dadosCertificado.getCertificate());
                    }
                    return Mono.just(respostaValidacaos);
                }).map(respostaValidacaos -> respostaValidacaos.parallelStream().collect(Collectors.toSet()));

    }

    private boolean isCacheValido(DadosCertificado dadosCertificado) {
        return cacheValido.get(dadosCertificado.getCertificate()) != null;
    }

    private boolean isCacheInvalido(DadosCertificado dadosCertificado) {
        return cacheInvalido.get(dadosCertificado.getCertificate()) != null;
    }

}
