package eti.italiviocorrea.api.rsocket.lcr.adapters.inbound.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class RequisicaoDTO implements Serializable {

    String nomeAC;
    String urlLcr;
    Integer indiLcrDelta;
    String indiAtualzLcr;
}
