package eti.italiviocorrea.api.rsocket.lcr.application.usecases;

import com.hazelcast.map.IMap;
import eti.italiviocorrea.api.rsocket.lcr.application.domain.AutoridadeCertificadora;
import eti.italiviocorrea.api.rsocket.lcr.application.ports.inboud.AutoridadeCertificadoraQueryPort;
import eti.italiviocorrea.api.rsocket.lcr.application.ports.inboud.AutoridadeCertificadoraUseCasePort;
import io.opentelemetry.extension.annotations.WithSpan;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

public class AutoridadeCertificadoraUseCase implements AutoridadeCertificadoraUseCasePort {

    private static final Logger LOGGER = Loggers.getLogger(AutoridadeCertificadoraUseCase.class);

    private final IMap<String, AutoridadeCertificadora> cache;

    private final AutoridadeCertificadoraQueryPort acRepository;

    public AutoridadeCertificadoraUseCase(IMap<String, AutoridadeCertificadora> cache, AutoridadeCertificadoraQueryPort acRepository) {
        this.cache = cache;
        this.acRepository = acRepository;
    }

    @WithSpan
    @Override
    public Mono<AutoridadeCertificadora> buscarPorNome(String nome) {
        return Mono.fromCompletionStage(() -> cache.getAsync(nome))
                .doOnNext(p -> LOGGER.info("AC com o nome : "+p.getNome() + ", encontrado no cache"))
                .switchIfEmpty(acRepository.findByNome(nome))
                .doOnNext(p -> {
                    cache.putAsync(p.getNome(),p);
                    LOGGER.info("AC com o nome : "+p.getNome()+", adicionado ao cache");
                });
    }

}
