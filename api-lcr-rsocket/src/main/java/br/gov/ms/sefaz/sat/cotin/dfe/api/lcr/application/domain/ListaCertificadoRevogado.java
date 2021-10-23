package br.gov.ms.sefaz.sat.cotin.dfe.api.lcr.application.domain;

import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListaCertificadoRevogado {

    Integer id;
    int indiLcrDelta;
    String indiAtualzLcr;
    String infoUrlLcr;

}
