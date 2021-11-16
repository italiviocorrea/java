package eti.italiviocorrea.api.rsocket.lcr.application.usecases.rules.certificado.transmissor;

import com.hazelcast.map.IMap;
import eti.italiviocorrea.api.rsocket.lcr.application.domain.DadosCertificado;
import eti.italiviocorrea.api.rsocket.lcr.application.domain.RespostaValidacao;
import io.opentelemetry.extension.annotations.WithSpan;
import org.springframework.util.ObjectUtils;

import java.security.cert.X509Certificate;
import java.util.function.Supplier;

public class RnA07Rej282 implements Supplier<RespostaValidacao> {

    private final RespostaValidacao resp282 = RespostaValidacao.builder().cStat("282")
            .xMotivo("Rejeicao: Certificado Transmissor sem CNPJ").build();
    private final DadosCertificado dadosCertificado;
    private final IMap<X509Certificate, X509Certificate> cacheInvalido;

    public RnA07Rej282(DadosCertificado dadosCertificado1, IMap<X509Certificate, X509Certificate> cacheInvalido) {
        this.dadosCertificado = dadosCertificado1;
        this.cacheInvalido = cacheInvalido;
    }

    @WithSpan
    @Override
    public RespostaValidacao get() {
        if (ObjectUtils.isEmpty(dadosCertificado)) {
            return RespostaValidacao.resp999().trace("pre requisitos da regra").className(getClass().getName());
        }

        if (ObjectUtils.isEmpty(dadosCertificado.getCnpj())) {
            cacheInvalido.put(dadosCertificado.getCertificate(),dadosCertificado.getCertificate());
            return resp282;
        }

        return RespostaValidacao.builder().ok(true).cStat("100").xMotivo("OK").build();
    }
}
