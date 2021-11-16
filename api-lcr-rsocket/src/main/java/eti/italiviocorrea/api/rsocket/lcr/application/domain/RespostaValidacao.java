package eti.italiviocorrea.api.rsocket.lcr.application.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RespostaValidacao implements Serializable {

    @Builder.Default
    private Boolean ok = false;
    private String cStat;
    private String xMotivo;
    private String complemento;
    private String trace;
    private String className;
    @JsonIgnore
    private Throwable throwable;

    @JsonIgnore
    public boolean isRejeicao() {
        return !ok;
    }

    public boolean isOk() {
        return ok;
    }

    @JsonIgnore
    public static RespostaValidacao respOk() {
        return RespostaValidacao.builder().ok(true).build();
    }

    @JsonIgnore
    public static RespostaValidacao resp999() {
        return RespostaValidacao.builder().cStat(ResultadoProcessamento.RP_999.getCStat())
                .xMotivo(ResultadoProcessamento.RP_999.getXMotivo()).build();
    }

    @JsonIgnore
    public String getXMotivoComComplemento() {
        if (!ObjectUtils.isEmpty(complemento)) {
            return String.format("%s [%s]", xMotivo, complemento);
        } else {
            return xMotivo;
        }
    }

    public RespostaValidacao complemento(String complemento) {
        this.complemento = complemento;
        return this;
    }

    public RespostaValidacao trace(String trace) {
        this.trace = trace;
        return this;
    }

    public RespostaValidacao className(String className) {
        this.className = className;
        return this;
    }

    public RespostaValidacao throwable(Throwable throwable) {
        this.throwable = throwable;
        return this;
    }

    public void set(RespostaValidacao inserirInvalido) {
    }
}
