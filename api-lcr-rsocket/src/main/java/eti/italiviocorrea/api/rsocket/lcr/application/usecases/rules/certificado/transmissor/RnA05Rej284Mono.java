package eti.italiviocorrea.api.rsocket.lcr.application.usecases.rules.certificado.transmissor;

import com.hazelcast.map.IMap;
import eti.italiviocorrea.api.rsocket.lcr.application.domain.DadosCertificado;
import eti.italiviocorrea.api.rsocket.lcr.application.domain.RespostaValidacao;
import eti.italiviocorrea.api.rsocket.lcr.application.ports.outbound.ListaCertificadoRevogadoCommandPort;
import io.opentelemetry.extension.annotations.WithSpan;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.security.cert.X509Certificate;
import java.util.List;
import java.util.function.Supplier;

public class RnA05Rej284Mono implements Supplier<Mono<RespostaValidacao>> {

    private final RespostaValidacao resp284 = RespostaValidacao.builder().cStat("284")
            .xMotivo("Rejeicao: Certificado Transmissor revogado").build();
    private final DadosCertificado dadosCertificado;
    private final IMap<X509Certificate, X509Certificate> cacheInvalido;
    private final ListaCertificadoRevogadoCommandPort repository;

    public RnA05Rej284Mono(DadosCertificado dadosCertificado,
                           IMap<X509Certificate, X509Certificate> cacheInvalido,
                           ListaCertificadoRevogadoCommandPort repository) {
        this.dadosCertificado = dadosCertificado;
        this.cacheInvalido = cacheInvalido;
        this.repository = repository;
    }

    @WithSpan
    @Override
    public Mono<RespostaValidacao> get() {

        if (ObjectUtils.isEmpty(dadosCertificado)
                || ObjectUtils.isEmpty(repository)) {
            return Mono.just(RespostaValidacao.resp999().trace("pre requisitos da regra").className(getClass().getName()));
        }

        List<String> lista = dadosCertificado.getPontosDistribuicaoLCR();

        if (ObjectUtils.isEmpty(lista)) {
            return Mono.just(inserirInvalido(dadosCertificado));
        }

        return Mono.just(lista)
                .flatMapMany(Flux::fromIterable)
                .flatMap(url -> Mono.defer(() -> repository.findByUrlESerialNumber(url, dadosCertificado.getSerialNumber()))
                        .subscribeOn(Schedulers.newParallel("lcrUrlNs",lista.size())))
                .collectList()
                .filter(listaCertificadoRevogados -> !listaCertificadoRevogados.isEmpty())
                .map(listaCertificadoRevogados -> inserirInvalido(dadosCertificado))
                .defaultIfEmpty(RespostaValidacao.builder().ok(true).cStat("100").xMotivo("OK").build());
    }

    private RespostaValidacao inserirInvalido(DadosCertificado dadosCertificado) {
        cacheInvalido.put(dadosCertificado.getCertificate(), dadosCertificado.getCertificate());
        return resp284;
    }

}
