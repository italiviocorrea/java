package br.gov.ms.sefaz.sat.cotin.dfe.api.lcr.domain.models;

import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AutoridadeCertificadora {

    Integer iditc;
    String alias;
    String nome;

}
