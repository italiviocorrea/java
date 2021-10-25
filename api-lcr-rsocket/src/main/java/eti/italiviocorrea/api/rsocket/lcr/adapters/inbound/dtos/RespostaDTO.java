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
public class RespostaDTO  implements Serializable {

    String cStatus;
    String xMotivo;

    public static RespostaDTO fromException(Exception e) {
        RespostaDTO respostaDTO =
                RespostaDTO.builder()
                        .cStatus("999")
                        .xMotivo(e.getMessage())
                        .build();
        return respostaDTO;
    }
}
