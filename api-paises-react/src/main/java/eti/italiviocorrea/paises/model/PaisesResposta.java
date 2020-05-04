package eti.italiviocorrea.paises.model;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

@Schema( description="Schema da resposta padrão dos serviços.")
public class PaisesResposta {

    @Schema( description="Lista de países.",implementation = Paises.class)
    public List<Paises> paises;

    @Schema(description="Versão do schema de resposta.")
    public String version;

    public PaisesResposta() {
        this.paises = new ArrayList<Paises>();
        this.version = "3.0.0";
    }

}
