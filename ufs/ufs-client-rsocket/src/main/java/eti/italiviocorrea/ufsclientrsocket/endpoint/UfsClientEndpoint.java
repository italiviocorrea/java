package eti.italiviocorrea.ufsclientrsocket.endpoint;

import eti.italiviocorrea.ufsclientrsocket.models.Ufs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/dfe/api/v1")
public class UfsClientEndpoint {

    private final RSocketRequester requester;

    @GetMapping(value = "/ufs", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Ufs> buscarTodos() {
        return this.requester.route("ufs.buscarTodos")
                .retrieveFlux(Ufs.class).retry(5).log();
    }

    @GetMapping(value = "/ufs/{codigo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Ufs> buscarPorCodigo(@PathVariable Short codigo){
        return this.requester.route("ufs.buscarPorCodigo."+codigo)
                .retrieveMono(Ufs.class).retry(5).log();
    }

    @PostMapping(value = "/ufs", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Ufs> inserir(@RequestBody Ufs ufs) {
        return this.requester.route("ufs.inserir")
                .data(ufs)
                .retrieveMono(Ufs.class)
                .retry(5)
                .log();
    }

    @PutMapping(value = "/ufs/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Ufs> atualizar(@PathVariable Integer id, @RequestBody Ufs ufs) {

        return this.requester.route("ufs.atualizar."+id)
                .data(ufs)
                .retrieveMono(Ufs.class)
                .retry(5)
                .log();
    }

    @DeleteMapping(value = "/ufs/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Void> remover(@PathVariable Integer id) {
        return this.requester.route("ufs.remover."+id).send();
    }
}
