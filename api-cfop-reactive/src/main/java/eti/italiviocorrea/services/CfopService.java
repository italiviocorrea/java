package eti.italiviocorrea.services;

import eti.italiviocorrea.model.Cfops;
import eti.italiviocorrea.model.CfopsResposta;
import eti.italiviocorrea.utils.MyAbstractResource;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import org.eclipse.microprofile.opentracing.Traced;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.LockModeType;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;

@Traced
@ApplicationScoped
public class CfopService extends MyAbstractResource implements PanacheRepositoryBase<Cfops, Long> {

    public CfopsResposta buscarTodosPaginado(short offset, short limit) {
        CfopsResposta cfopsResposta = new CfopsResposta();
        for (Cfops cfops : findAll().page(offset - 1, limit).list()) {
            cfopsResposta.cfops.add(cfops);
        }
        return cfopsResposta;
    }

    @Transactional
    public Uni<Boolean> incluir(@NotNull Cfops cfops) {
        return Uni.createFrom().item(() -> {
            persistAndFlush(cfops);
            return cfops;
        })
                .subscribeOn(Infrastructure.getDefaultExecutor())
                .onItem().apply(row -> row != null);
    }

    @Transactional
    public Uni<Boolean> atualizar(Cfops cfops, Long id) {
        return Uni.createFrom().item(() -> {
            Cfops cfops1 = findById(id, LockModeType.PESSIMISTIC_WRITE);
            cfops1.nome = cfops.nome.equals(cfops1.nome) ? cfops1.nome : cfops.nome;
            cfops1.indTransp = cfops.indTransp == cfops1.indTransp ? cfops1.indTransp : cfops.indTransp;
            cfops1.indAnula = cfops.indAnula == cfops1.indAnula ? cfops1.indAnula : cfops.indAnula;
            cfops1.indComb = cfops.indComb == cfops1.indComb ? cfops1.indComb : cfops.indComb;
            cfops1.indComunica = cfops.indComunica == cfops1.indComunica ? cfops1.indComunica : cfops.indComunica;
            cfops1.indDevol = cfops.indDevol == cfops1.indDevol ? cfops1.indDevol : cfops.indDevol;
            cfops1.indNfe = cfops.indNfe == cfops1.indNfe? cfops1.indNfe : cfops.indNfe;
            cfops1.indRemes = cfops.indRemes == cfops1.indRemes ? cfops1.indRemes : cfops.indRemes;
            cfops1.indRetor = cfops.indRetor == cfops1.indRetor ? cfops1.indRetor : cfops.indRetor;
            cfops1.inicioVigencia = cfops.inicioVigencia == cfops1.inicioVigencia ? cfops1.inicioVigencia : cfops.inicioVigencia;
            cfops1.fimVigencia = cfops.fimVigencia == cfops1.fimVigencia ? cfops1.fimVigencia : cfops.fimVigencia;
            return cfops1;
        })
                .subscribeOn(Infrastructure.getDefaultExecutor())
                .onItem().apply(row -> row != null);
    }

    @Transactional
    public Uni<Boolean> remover(Long id) {
        return Uni.createFrom().item(() -> deleteById(id))
                .subscribeOn(Infrastructure.getDefaultExecutor());
    }

    /**
     *
     * @param limit
     * @return
     */
    public int pageCount(short limit) {
        return Math.toIntExact(this.count() / limit);
    }
}
