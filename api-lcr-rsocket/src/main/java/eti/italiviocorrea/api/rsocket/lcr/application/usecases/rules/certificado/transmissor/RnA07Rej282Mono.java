package eti.italiviocorrea.api.rsocket.lcr.application.usecases.rules.certificado.transmissor;

import com.hazelcast.map.IMap;
import eti.italiviocorrea.api.rsocket.lcr.application.domain.DadosCertificado;
import eti.italiviocorrea.api.rsocket.lcr.application.domain.RespostaValidacao;
import io.opentelemetry.extension.annotations.WithSpan;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Mono;

import java.security.cert.X509Certificate;
import java.util.function.Supplier;

public class RnA07Rej282Mono implements Supplier<Mono<RespostaValidacao>> {

    private final RespostaValidacao resp282 = RespostaValidacao.builder().cStat("282")
            .xMotivo("Rejeicao: Certificado Transmissor sem CNPJ").build();
    private final DadosCertificado dadosCertificado;
    private final IMap<X509Certificate, X509Certificate> cacheInvalido;

    public RnA07Rej282Mono(DadosCertificado dadosCertificado1, IMap<X509Certificate, X509Certificate> cacheInvalido) {
        this.dadosCertificado = dadosCertificado1;
        this.cacheInvalido = cacheInvalido;
    }

    @WithSpan
    @Override
    public Mono<RespostaValidacao> get() {


        return Mono.just(dadosCertificado)
                .flatMap(dadosCertificado1 -> {
                    if (ObjectUtils.isEmpty(dadosCertificado)) {
                        return Mono.just(RespostaValidacao.resp999().trace("pre requisitos da regra").className(getClass().getName()));
                    } else if (ObjectUtils.isEmpty(dadosCertificado.getCnpj())) {
                        cacheInvalido.put(dadosCertificado.getCertificate(), dadosCertificado.getCertificate());
                        return Mono.just(resp282);
                    }
                    return Mono.just(RespostaValidacao.builder().ok(true).cStat("100").xMotivo("OK").build());
                });

    }
}
