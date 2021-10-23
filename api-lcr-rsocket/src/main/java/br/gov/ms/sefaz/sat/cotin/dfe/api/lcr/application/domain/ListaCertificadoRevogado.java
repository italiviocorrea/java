package br.gov.ms.sefaz.sat.cotin.dfe.api.lcr.application.domain;

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
