package eti.italiviocorrea.endpoint;

import eti.italiviocorrea.model.Cfops;
import eti.italiviocorrea.model.CfopsResposta;
import eti.italiviocorrea.services.CfopService;
import eti.italiviocorrea.utils.MyAbstractResource;
import io.smallrye.mutiny.TimeoutException;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/v1/cfops")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CfopResource extends MyAbstractResource<CfopsResposta, Cfops> {

    @Inject
    CfopService service;


    @GET
    // Tolerância a falhas
    @Timeout(100)
    @Retry(retryOn = {RuntimeException.class, TimeoutException.class},
            maxRetries = 4)
    @CircuitBreaker(failOn = {RuntimeException.class, TimeoutException.class},
            successThreshold = 5,
            requestVolumeThreshold = 4,
            failureRatio = 0.75,
            delay = 1000)
    // métricas
    @Counted(description = "Contagem da lista de países", absolute = true, name = "paises_buscar_todos")
    @Timed(name = "timerCheck", description = "Quanto tempo leva para carregar a lista de países", unit = MetricUnits.MILLISECONDS)
    public Uni<Response> buscarTodos(@Context UriInfo uriInfo,
                                     @DefaultValue("1") @QueryParam("offset") short offset,
                                     @DefaultValue("20") @QueryParam("limit") short limit) {
        writeMessageLogging("Buscando todos página " + offset + " limitado " + limit);
        return Uni.createFrom().item(service.buscarTodosPaginado(offset, limit))
                .subscribeOn(Infrastructure.getDefaultExecutor())
                .onItem().apply(cfops ->  criarRespostaTodos(uriInfo, cfops, offset, limit))
                .onItem().apply(Response.ResponseBuilder::build);

    }

    @GET
    @Path("{id}")
    // Tolerância a falhas
    @Timeout(100)
    @Retry(retryOn = {RuntimeException.class, TimeoutException.class},
            maxRetries = 4)
    @CircuitBreaker(failOn = {RuntimeException.class, TimeoutException.class},
            successThreshold = 5,
            requestVolumeThreshold = 4,
            failureRatio = 0.75,
            delay = 1000)
    // métricas
    @Counted(description = "Total de busca por ID", absolute = true, name = "qtd-cfops-busca-por-id")
    @Timed(name = "tempo-buscar-cfop-por-id", description = "Quanto tempo leva para buscar um CFOP por ID", unit = MetricUnits.MILLISECONDS)
    public Uni<Response> buscarPorId(@Context UriInfo uriInfo,
                                     @PathParam("id") Integer id) {
        return Uni.createFrom().item(service.findById(Long.valueOf(id)))
                .subscribeOn(Infrastructure.getDefaultExecutor())
                .onItem().apply(cfops ->   criarRespostaPorId(uriInfo,cfops,id))
                .onItem().apply(Response.ResponseBuilder::build);
    }

    @POST
    // Tolerância a falhas
    @Timeout(100)
    @Retry(retryOn = {RuntimeException.class, TimeoutException.class},
            maxRetries = 4)
    @CircuitBreaker(failOn = {RuntimeException.class, TimeoutException.class},
            successThreshold = 5,
            requestVolumeThreshold = 4,
            failureRatio = 0.75,
            delay = 1000)
    // métricas
    @Counted(description = "Total de cfops incluídos", absolute = true, name = "qtd-cfops-inclusao")
    @Timed(name = "tempo-incluir-cfops", description = "Tempo leva para incluir um cfop", unit = MetricUnits.MILLISECONDS)
    public Uni<Response> incluir(@Context UriInfo uriInfo, Cfops cfops) {
        writeMessageLogging("Incluído o Cfop " + cfops.nome);
        return service.incluir(cfops)
                .onItem().apply(incluido -> incluido ? Response.Status.NO_CONTENT : Response.Status.NOT_ACCEPTABLE)
                .onItem().apply(status -> Response.status(status))
                .onItem().apply(response -> addLink(uriInfo,
                        response,
                        criarLink(uriInfo,
                                api_resource_context + cfops.id,
                                "GET",
                                "Buscar por ID",
                                api_cfops_resource_content_type)))
                .onItem().apply(Response.ResponseBuilder::build);
    }

    @PUT
    @Path("{id}")
    // Tolerância a falhas
    @Timeout(100)
    @Retry(retryOn = {RuntimeException.class, TimeoutException.class},
            maxRetries = 4)
    @CircuitBreaker(failOn = {RuntimeException.class, TimeoutException.class},
            successThreshold = 5,
            requestVolumeThreshold = 4,
            failureRatio = 0.75,
            delay = 1000)
    // métricas
    @Counted(description = "Total de modificações de países", absolute = true, name = "qtd-paises-atualizado")
    @Timed(name = "tempo-atualizar-pais", description = "Quanto tempo leva para atualizar um país", unit = MetricUnits.MILLISECONDS)
    public Uni<Response> atualizar(@Context UriInfo uriInfo,
                                   @PathParam("id") Long id, Cfops cfops) {
        writeMessageLogging("Atualizando o país cadastrado com o ID = " + id);
        return service.atualizar(cfops, id)
                .onItem().apply(atualizado -> atualizado ? Response.Status.NO_CONTENT : Response.Status.NOT_FOUND)
                .onItem().apply(status -> Response.status(status))
                .onItem().apply(response -> addLink(uriInfo,
                        response,
                        criarLink(uriInfo,
                                api_resource_context + id,
                                "GET",
                                "Buscar por ID",
                                api_cfops_resource_content_type)))
                .onItem().apply(Response.ResponseBuilder::build);
    }

    @DELETE
    @Path("{id}")
    // Tolerância a falhas
    @Timeout(100)
    @Retry(retryOn = {RuntimeException.class, TimeoutException.class},
            maxRetries = 4)
    @CircuitBreaker(failOn = {RuntimeException.class, TimeoutException.class},
            successThreshold = 5,
            requestVolumeThreshold = 4,
            failureRatio = 0.75,
            delay = 1000)
    // métricas
    @Counted(description = "Total de exclusões de cfops", absolute = true, name = "qtd-cfops-exclusao")
    @Timed(name = "tempo-excluir-cfops", description = "Quanto tempo leva para excluir um cfops", unit = MetricUnits.MILLISECONDS)
    public Uni<Response> remover(@Context UriInfo uriInfo,
                                 @PathParam("id") Long id) {
        writeMessageLogging("Removendo o cfops cadastrado com o ID = " + id);
        return service.remover(id)
                .onItem().apply(removido -> removido ? Response.Status.NO_CONTENT : Response.Status.NOT_FOUND)
                .onItem().apply(status -> Response.status(status))
                .onItem().apply(response -> addLink(uriInfo, response,
                        criarLinkPage(uriInfo,
                                offset_default,
                                limit_default,
                                api_resource_context,
                                "GET",
                                "Buscar todos",
                                api_cfops_resource_content_type)))
                .onItem().apply(Response.ResponseBuilder::build);
    }

}