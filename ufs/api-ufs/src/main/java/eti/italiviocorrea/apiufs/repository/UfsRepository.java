package eti.italiviocorrea.apiufs.repository;

import eti.italiviocorrea.apiufs.model.Ufs;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface UfsRepository extends R2dbcRepository<Ufs, Integer> {

    @Query("SELECT * FROM ufs WHERE codigo = :codigo")
    Mono<Ufs> findByCode(Short codigo);

}
