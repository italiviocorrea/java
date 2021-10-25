package eti.italiviocorrea.api.rsocket.lcr.application.domain;

import lombok.*;

import java.io.Serializable;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListaCertificadoRevogado implements Serializable {

    Integer id;
    int indiLcrDelta;
    String indiAtualzLcr;
    String infoUrlLcr;

}
