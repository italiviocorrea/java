package eti.italiviocorrea.paises.endpoint;

import eti.italiviocorrea.paises.model.Paises;
import eti.italiviocorrea.paises.service.PaisesService;
import eti.italiviocorrea.paises.util.MyAbstractResource;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;


@Path("/v1/paises")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PaisesResource extends MyAbstractResource {

    @Inject
    PaisesService paisesService;

    @GET
    // métricas
    @Counted(description = "Contagem da lista de países", absolute = true, name = "paises_buscar_todos")
    @Timed(name = "timerCheck", description = "Quanto tempo leva para carregar a lista de países", unit = MetricUnits.MILLISECONDS)
    public Uni<Response> buscarTodos(@Context UriInfo uriInfo,
                                     @DefaultValue("1")
                                     @QueryParam("offset") short offset,
                                     @DefaultValue("20")
                                     @QueryParam("limit") short limit) {
        return paisesService.buscarTodos(offset, limit)
                .onItem().apply(paises -> criarRespostaTodos(uriInfo, paises, offset, limit))
                .onItem().apply(ResponseBuilder::build);

    }

    @GET
    @Path("{id}")
    // métricas
    @Counted(description = "Total de busca por ID", absolute = true, name = "qtd-paises-busca-por-id")
    @Timed(name = "tempo-buscar-pais-por-id", description = "Quanto tempo leva para buscar um país por ID", unit = MetricUnits.MILLISECONDS)
    public Uni<Response> buscarPorId(@Context UriInfo uriInfo,
                                     @PathParam Long id) {
        writeMessageLogging("Buscando o país cadastrado com o ID = " + id);
        return paisesService.buscarPorId(id)
                .onItem().apply(paises -> criarRespostaPorId(uriInfo,paises,id))
                .onItem().apply(ResponseBuilder::build);
    }

    @POST
    // métricas
    @Counted(description = "Total de países incluídos", absolute = true, name = "qtd-paises-inclusao")
    @Timed(name = "tempo-incluir-pais", description = "Tempo leva para incluir um país", unit = MetricUnits.MILLISECONDS)
    public Uni<Response> incluir(@Context UriInfo uriInfo, Paises paises) {
        writeMessageLogging("Incluído o país " + paises.nome);
        return paisesService.incluir(paises)
                .onItem().apply(incluido -> incluido ? Status.NO_CONTENT : Status.NOT_ACCEPTABLE)
                .onItem().apply(status -> Response.status(status))
                .onItem().apply(response -> addLink(uriInfo,
                        response,
                        criarLink(uriInfo,
                                "v1/paises" + paises.id,
                                "GET",
                                "Buscar por ID",
                                "application/json")))
                .onItem().apply(ResponseBuilder::build);
    }

    @PUT
    @Path("{id}")
    // métricas
    @Counted(description = "Total de modificações de países", absolute = true, name = "qtd-paises-atualizado")
    @Timed(name = "tempo-atualizar-pais", description = "Quanto tempo leva para atualizar um país", unit = MetricUnits.MILLISECONDS)
    public Uni<Response> atualizar(@Context UriInfo uriInfo,
                                   @PathParam Long id, Paises paises) {
        writeMessageLogging("Atualizando o país cadastrado com o ID = " + id);
        return paisesService.atualizar(paises, id)
                .onItem().apply(atualizado -> atualizado ? Status.NO_CONTENT : Status.NOT_FOUND)
                .onItem().apply(status -> Response.status(status))
                .onItem().apply(response -> addLink(uriInfo,
                        response,
                        criarLink(uriInfo,
                                "v1/paises" + id,
                                "GET",
                                "Buscar por ID",
                                "application/json")))
                .onItem().apply(ResponseBuilder::build);
    }

    @DELETE
    @Path("{id}")
    // métricas
    @Counted(description = "Total de exclusões de países", absolute = true, name = "qtd-paises-exclusão")
    @Timed(name = "tempo-excluir-pais", description = "Quanto tempo leva para excluir um país", unit = MetricUnits.MILLISECONDS)
    public Uni<Response> remover(@Context UriInfo uriInfo,
                                 @PathParam Long id) {
        writeMessageLogging("Removendo o país cadastrado com o ID = " + id);
        return paisesService.remover(id)
                .onItem().apply(removido -> removido ? Status.NO_CONTENT : Status.NOT_FOUND)
                .onItem().apply(status -> Response.status(status))
                .onItem().apply(response -> addLink(uriInfo, response,
                        criarLink(uriInfo,
                                "v1/paises",
                                "GET",
                                "Buscar todos",
                                "application/json")))
                .onItem().apply(ResponseBuilder::build);
    }


}