package eti.italiviocorrea.api.rsocket.lcr.application.usecases.rules.certificado.transmissor;

import com.hazelcast.map.IMap;
import eti.italiviocorrea.api.rsocket.lcr.application.domain.DadosCertificado;
import eti.italiviocorrea.api.rsocket.lcr.application.domain.RespostaValidacao;
import eti.italiviocorrea.api.rsocket.lcr.application.ports.inboud.AutoridadeCertificadoraQueryPort;
import io.opentelemetry.extension.annotations.WithSpan;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Mono;
import reactor.util.Loggers;

import java.security.cert.X509Certificate;
import java.util.function.Supplier;

public class RnA03Rej283Mono implements Supplier<Mono<RespostaValidacao>> {

    private static final reactor.util.Logger LOGGER = Loggers.getLogger(RnA03Rej283Mono.class);

    private final RespostaValidacao resp283 = RespostaValidacao.builder().cStat("283")
            .xMotivo("Rejeicao: Certificado Transmissor - erro Cadeia de Certificacao").build();
    private final DadosCertificado dadosCertificado;
    private final IMap<X509Certificate, X509Certificate> cacheInvalido;
    private final AutoridadeCertificadoraQueryPort repository;

    public RnA03Rej283Mono(DadosCertificado dadosCertificado,
                           IMap<X509Certificate, X509Certificate> cacheInvalido,
                           AutoridadeCertificadoraQueryPort repository) {
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

//        Mono<AutoridadeCertificadora> ac = repository.findByNome(dadosCertificado.getNomeAC());
//
//        return Mono.just(RespostaValidacao.builder().ok(true).cStat("100").xMotivo("OK").build())
//                .flatMap(respostaValidacao -> {
//                    if (ObjectUtils.isEmpty(ac)) {
//                        cacheInvalido.put(dadosCertificado.getCertificate(), dadosCertificado.getCertificate());
//                        return Mono.just(resp283);
//                    }
//                    return Mono.just(respostaValidacao);
//                });

        return Mono.just(dadosCertificado.getNomeAC())
                .flatMap(acs -> repository.findByNome(acs))
                .filter(autoridadeCertificadora -> ObjectUtils.isEmpty(autoridadeCertificadora))
                .flatMap(autoridadeCertificadora -> inserirCacheInvalido(dadosCertificado))
                .defaultIfEmpty(RespostaValidacao.builder().ok(true).cStat("100").xMotivo("OK").build());
    }

    private Mono<RespostaValidacao> inserirCacheInvalido(DadosCertificado dadosCertificado) {
        cacheInvalido.put(dadosCertificado.getCertificate(), dadosCertificado.getCertificate());
        return Mono.just(resp283);
    }
}
