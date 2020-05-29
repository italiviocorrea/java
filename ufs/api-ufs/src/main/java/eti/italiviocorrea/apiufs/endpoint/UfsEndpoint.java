package eti.italiviocorrea.apiufs.endpoint;

import eti.italiviocorrea.apiufs.model.Ufs;
import eti.italiviocorrea.apiufs.repository.UfsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UfsEndpoint {

    private final UfsRepository ufs;

    @MessageMapping("ufs.buscarTodos")
    public Flux<Ufs> buscarTodos() {
        return ufs.findAll();
    }

    @MessageMapping("ufs.buscarPorCodigo.{codigo}")
    public Mono<Ufs> buscarPorCodigo(
            @DestinationVariable("codigo") Short codigo) {
        return ufs.findByCode(codigo);
    }

    @MessageMapping("ufs.inserir")
    public Mono<Ufs> inserir(@Payload Ufs ufs) {
        return this.ufs.save(ufs);
    }

    @MessageMapping("ufs.atualizar.{id}")
    public Mono<Ufs> atualizar(
            @DestinationVariable("id") Integer id,
            @Payload Ufs ufs) {
        return this.ufs.findById(id)
                .map(p -> {
                    p.setNome(ufs.getNome());
                    p.setSigla(ufs.getSigla());
                    p.setInicioVigencia(ufs.getInicioVigencia());
                    p.setFimVigencia(ufs.getFimVigencia());
                    return p;
                })
                .flatMap(p -> this.ufs.save(p));
    }

    @MessageMapping("ufs.remover.{id}")
    public Mono<Void> remover(
            @DestinationVariable("id") Integer id) {
        return ufs.findById(id)
                .flatMap(p -> ufs.delete(p));
    }

}
