package eti.italiviocorrea.model;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

@Schema( description="Schema da resposta do método buscarTodos.")
public class CfopsResposta {

    @Schema( description="Lista de cfops.",implementation = Cfops.class)
    public List<Cfops> cfops;

    @Schema(description="Versão do schema de resposta.")
    public String version;

    public CfopsResposta() {
        this.cfops = new ArrayList<>();
        this.version = "3.0.0";
    }
}
