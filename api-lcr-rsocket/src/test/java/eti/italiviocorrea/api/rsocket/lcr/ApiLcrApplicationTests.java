package eti.italiviocorrea.api.rsocket.lcr;

import eti.italiviocorrea.api.rsocket.lcr.application.domain.AutoridadeCertificadora;
import io.rsocket.frame.decoder.PayloadDecoder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.MimeTypeUtils;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.retry.Retry;

@SpringBootTest
@ActiveProfiles("local")
class ApiLcrApplicationTests {

    private static RSocketRequester requester;

    @BeforeAll
    public static void setupOnce(@Autowired RSocketRequester.Builder builder) {

        Hooks.onErrorDropped(throwable -> {});

        requester = builder
                .rsocketConnector(rSocketConnector -> rSocketConnector
                        .reconnect(Retry.indefinitely())
                        .payloadDecoder(PayloadDecoder.ZERO_COPY))
                .dataMimeType(MimeTypeUtils.APPLICATION_JSON)
                .tcp("localhost", 7000);
    }

    /**
     * Método comum para enviar requisição para API rsocket
     *
     * @param nome Nome da autoridade certificadora
     * @return Retorna a Autoridade Certificadora ou nulo
     */
    Mono<AutoridadeCertificadora> buscarPorNome(String nome) {
        return requester.route("api-lcr.buscarAcPorNome." + nome)
                .retrieveMono(AutoridadeCertificadora.class);
    }

    @Test
    @DisplayName("Tentar Buscar uma AC sem informar o nome dela")
    void testEnvioRequisicaoVazia() {

        Mono<AutoridadeCertificadora> result = buscarPorNome("");

        // Verificar se a mensagem de resposta contém os dados esperados
        StepVerifier
                .create(result)
                .expectError();
    }

    @Test
    @DisplayName("Buscar por nome uma AC, que não existe.")
    void testBuscaACNaoExiste() {

        Mono<AutoridadeCertificadora> result = buscarPorNome("AC Não Existe");

        // Verificar se a mensagem de resposta contém os dados esperados
        StepVerifier
                .create(result)
                .expectError();
    }

    @Test
    @DisplayName("Buscar  por nome, uma AC que existe.")
    void testBuscaACExistente() {

        // AutoridadeCertificadora que existe na base de dados
        AutoridadeCertificadora ac = AutoridadeCertificadora
                .builder()
                .iditc(1)
                .alias("ac_soluti_v5")
                .nome("AC SOLUTI Multipla v5")
                .build();

        // Buscar a autoridade certificadora no banco de dados
        Mono<AutoridadeCertificadora> result = buscarPorNome(ac.getNome());

        // Verificar se a mensagem de resposta contém os dados esperados
        StepVerifier
                .create(result)
                .expectNextMatches(autoridadeCertificadora -> autoridadeCertificadora.equals(ac))
                .verifyComplete();
    }

}