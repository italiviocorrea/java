package eti.italiviocorrea.utils;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Random;

public abstract class MyAbstractResource<T, K> extends MyAbstractLogging {

    @Inject
    @ConfigProperty(name = "api_resource.context", defaultValue = "v1/cfops/")
    public String api_resource_context;

    @Inject
    @ConfigProperty(name = "api_resource.content.type", defaultValue = "application/text")
    public String api_resource_content_type;

    @Inject
    @ConfigProperty(name = "api_resource.offset.default", defaultValue = "1")
    protected short offset_default;

    @Inject
    @ConfigProperty(name = "api_resource.limit.default", defaultValue = "20")
    protected short limit_default;

    protected Response.ResponseBuilder criarRespostaTodos(@Context UriInfo uriInfo,
                                                          T entitys,
                                                          short offset,
                                                          short limit) {

        if (entitys != null) {

            PageOpt pageOpt;
            pageOpt = new PageOpt(offset);

            Response.ResponseBuilder response = Response.ok(entitys);

            if (pageOpt.firstPage > 0) {
                response.links(
                        criarLinkPage(uriInfo, pageOpt.firstPage, limit, api_resource_context, "GET", "Primeira", api_resource_content_type)
                );
            }
            if (pageOpt.nextPage > 0) {
                response.links(
                        criarLinkPage(uriInfo, pageOpt.nextPage, limit, api_resource_context, "GET", "Proxima", api_resource_content_type)
                );
            }
            if (pageOpt.prevPage > 0) {
                response.links(
                        criarLinkPage(uriInfo, pageOpt.prevPage, limit, api_resource_context, "GET", "Anterio", api_resource_content_type)
                );
            }

            return response;

        } else {
            return Response.status(Response.Status.NOT_FOUND.getStatusCode(), "Não foi localizado o recurso solicitado");
        }
    }

    protected Response.ResponseBuilder criarRespostaPorId(@Context UriInfo uriInfo,
                                                          K entity,
                                                          Integer id) {

        return entity != null ? Response.ok(entity)
                .links(
                        criarLinkPage(uriInfo, offset_default, limit_default, api_resource_context, "GET", "Buscar todos", api_resource_content_type),
                        criarLink(uriInfo, api_resource_context + id, "PUT", "Atualizar", api_resource_content_type),
                        criarLink(uriInfo, api_resource_context + id, "DELETE", "Excluir", api_resource_content_type)
                ) : Response.status(Response.Status.NOT_FOUND.getStatusCode(), "Não foi localizado o recurso solicitado")
                .links(
                        criarLink(uriInfo, api_resource_context, "GET", "Buscar todos", api_resource_content_type)
                );
    }

    protected Link criarLinkPage(@Context UriInfo uriInfo,
                                 short offset,
                                 short limit,
                                 String path,
                                 String rel,
                                 String title,
                                 String type) {
        return offset > 0 ? Link.fromUriBuilder(
                uriInfo.getBaseUriBuilder()
                        .path(path)
                        .queryParam("offset", offset)
                        .queryParam("limit", limit))
                .rel(rel)
                .title(title)
                .type(type)
                .build() : null;
    }

    protected Link criarLink(@Context UriInfo uriInfo,
                             String path,
                             String rel,
                             String title,
                             String type) {
        return Link.fromUriBuilder(
                uriInfo.getBaseUriBuilder()
                        .path(path))
                .rel(rel)
                .title(title)
                .type(type)
                .build();
    }

    protected Response.ResponseBuilder addLink(@Context UriInfo uriInfo,
                                               Response.ResponseBuilder response, Link link) {
        return response.links(link);
    }

    // Para testar circuit-break
    protected static void possibleFailure() {
        if (new Random().nextFloat() < 0.5f) {
            throw new RuntimeException("Resource failure.");
        }
    }

    /**
     *
     */
    private class PageOpt {

        public short offset;
        public short prevPage;
        public short nextPage;
        public short firstPage;

        /**
         * @param offset
         */
        public PageOpt(short offset) {
            this.offset = offset;
            if (this.offset != 1) {
                this.firstPage = 1;
                this.nextPage = (short) (this.offset + 1);
                this.prevPage = (short) (this.offset - 1);
            } else {
                this.firstPage = 1;
                this.nextPage = (short) (this.offset + 1);
                this.prevPage = 0;
            }
        }

    }
}
