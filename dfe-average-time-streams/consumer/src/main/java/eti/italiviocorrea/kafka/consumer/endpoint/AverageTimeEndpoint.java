package eti.italiviocorrea.kafka.consumer.endpoint;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationScoped
@Path("/nf3e-tempo-medio")
public class AverageTimeEndpoint {

    @GET
    @Path("/data/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNF3eServiceData(@PathParam("id") int id) {
        return Response.ok("Servico = "+id).build();
    }
}