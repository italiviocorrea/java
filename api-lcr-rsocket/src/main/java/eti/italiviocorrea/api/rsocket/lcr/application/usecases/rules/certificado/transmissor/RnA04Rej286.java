package eti.italiviocorrea.api.rsocket.lcr.application.usecases.rules.certificado.transmissor;

import com.hazelcast.map.IMap;
import eti.italiviocorrea.api.rsocket.lcr.application.domain.DadosCertificado;
import eti.italiviocorrea.api.rsocket.lcr.application.domain.ListaCertificadoRevogado;
import eti.italiviocorrea.api.rsocket.lcr.application.domain.RespostaValidacao;
import eti.italiviocorrea.api.rsocket.lcr.application.ports.outbound.ListaCertificadoRevogadoCommandPort;
import io.opentelemetry.extension.annotations.WithSpan;
import org.springframework.util.ObjectUtils;

import java.security.cert.X509Certificate;
import java.util.List;
import java.util.function.Supplier;

public class RnA04Rej286 implements Supplier<RespostaValidacao> {

    private final RespostaValidacao resp286 = RespostaValidacao.builder().cStat("286")
            .xMotivo("Rejeicao: Certificado Transmissor erro no acesso a LCR").build();

    private final DadosCertificado dadosCertificado;
    private final IMap<X509Certificate, X509Certificate> cacheInvalido;
    private final ListaCertificadoRevogadoCommandPort repository;

    public RnA04Rej286(DadosCertificado dadosCertificado,
                       IMap<X509Certificate, X509Certificate> cacheInvalido,
                       ListaCertificadoRevogadoCommandPort repository) {
        this.dadosCertificado = dadosCertificado;
        this.cacheInvalido = cacheInvalido;
        this.repository = repository;
    }

    @WithSpan
    @Override
    public RespostaValidacao get() {

        if (ObjectUtils.isEmpty(dadosCertificado)
                || ObjectUtils.isEmpty(repository)
                || ObjectUtils.isEmpty(dadosCertificado.getCertificate())) {
            return RespostaValidacao.resp999().trace("pre requisitos da regra").className(getClass().getName());
        }

        List<String> lista = dadosCertificado.getPontosDistribuicaoLCR();

        if (ObjectUtils.isEmpty(lista)) {
            return inserirInvalido(dadosCertificado);
        }

        RespostaValidacao resp = RespostaValidacao.builder().ok(true).cStat("100").xMotivo("OK").build();

        lista.parallelStream().forEach(url -> {

            ListaCertificadoRevogado listaCertificadoRevogado = repository.findStatusListaByUrl(url).block();

            System.out.println(url);

            if (listaCertificadoRevogado != null) {
                if (listaCertificadoRevogado.getIndiLcrDelta() > 0) {
                    resp.set(inserirInvalido(dadosCertificado));
                }
            }

        });

        return resp;
    }

    private RespostaValidacao inserirInvalido(DadosCertificado dadosCertificado) {
        cacheInvalido.put(dadosCertificado.getCertificate(), dadosCertificado.getCertificate());
        return resp286;
    }

}
