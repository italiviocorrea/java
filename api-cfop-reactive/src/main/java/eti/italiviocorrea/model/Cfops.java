package eti.italiviocorrea.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

@Schema(type = SchemaType.OBJECT, name = "Cfops", description = "Esquema da entidade Cfops.")
@Entity
public class Cfops extends PanacheEntityBase {

    @Id
    @Schema(name = "Id", description = "Código/identificador do cfop.", required = true)
    public Long id;
    @Schema(name = "nome", description = "Nome do cfop.", required = true)
    public String nome;
    @Schema(name = "indNfe", description = "Indicador de NF-e.", required = true)
    public short indNfe;
    @Schema(name = "indComunica", description = "Indicador de Comunicação.", required = true)
    public short indComunica;
    @Schema(name = "indTransp", description = "Indicador de Transporte.", required = true)
    public short indTransp;
    @Schema(name = "indDevol", description = "Indicador de Devolução.", required = true)
    public short indDevol;
    @Schema(name = "indRetor", description = "Indicador de Retorno.", required = true)
    public short indRetor;
    @Schema(name = "indAnula", description = "Indicador de Anulação.", required = true)
    public short indAnula;
    @Schema(name = "indRemes", description = "Indicador de Remessa.", required = true)
    public short indRemes;
    @Schema(name = "indComb", description = "Indicador de Combustivel.", required = true)
    public short indComb;
    @Schema(name = "inicioVigencia", description = "Inicio de vigência do cfop.", required = true)
    public LocalDate inicioVigencia;
    @Schema(name = "fimVigencia", description = "Fim da vigência do cfop. (não obrigatório)", required = false)
    public LocalDate fimVigencia;

    public Cfops() {
    }

    public Cfops(Long id, String nome, short indNfe, short indComunica, short indTransp, short indDevol, short indRetor, short indAnula, short indRemes, short indComb, LocalDate inicioVigencia, LocalDate fimVigencia) {
        this.id = id;
        this.nome = nome;
        this.indNfe = indNfe;
        this.indComunica = indComunica;
        this.indTransp = indTransp;
        this.indDevol = indDevol;
        this.indRetor = indRetor;
        this.indAnula = indAnula;
        this.indRemes = indRemes;
        this.indComb = indComb;
        this.inicioVigencia = inicioVigencia;
        this.fimVigencia = fimVigencia;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public short getIndNfe() {
        return indNfe;
    }

    public void setIndNfe(short indNfe) {
        this.indNfe = indNfe;
    }

    public short getIndComunica() {
        return indComunica;
    }

    public void setIndComunica(short indComunica) {
        this.indComunica = indComunica;
    }

    public short getIndTransp() {
        return indTransp;
    }

    public void setIndTransp(short indTransp) {
        this.indTransp = indTransp;
    }

    public short getIndDevol() {
        return indDevol;
    }

    public void setIndDevol(short indDevol) {
        this.indDevol = indDevol;
    }

    public short getIndRetor() {
        return indRetor;
    }

    public void setIndRetor(short indRetor) {
        this.indRetor = indRetor;
    }

    public short getIndAnula() {
        return indAnula;
    }

    public void setIndAnula(short indAnula) {
        this.indAnula = indAnula;
    }

    public short getIndRemes() {
        return indRemes;
    }

    public void setIndRemes(short indRemes) {
        this.indRemes = indRemes;
    }

    public short getIndComb() {
        return indComb;
    }

    public void setIndComb(short indComb) {
        this.indComb = indComb;
    }

    public LocalDate getInicioVigencia() {
        return inicioVigencia;
    }

    public void setInicioVigencia(LocalDate inicioVigencia) {
        this.inicioVigencia = inicioVigencia;
    }

    public LocalDate getFimVigencia() {
        return fimVigencia;
    }

    public void setFimVigencia(LocalDate fimVigencia) {
        this.fimVigencia = fimVigencia;
    }
}
