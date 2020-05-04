package eti.italiviocorrea.paises.model;

import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(type = SchemaType.OBJECT, name = "Paises", description = "Esquema da entidade países.")
public class Paises {

    @Schema(name = "Id", description = "Código/identificador do país.", required = true)
    public Long id;

    @Schema(name = "nome", description = "Nome do país.", required = true)
    public String nome;

    @Schema(name = "sigla", description = "Sigla do país.", required = true)
    public String sigla;

    public Paises() {
    }

    public Paises(Long id, String nome, String sigla) {
        this.id = id;
        this.nome = nome;
        this.sigla = sigla;
    }


}
