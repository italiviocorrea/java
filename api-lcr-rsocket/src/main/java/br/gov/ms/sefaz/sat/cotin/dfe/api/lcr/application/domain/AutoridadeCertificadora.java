package br.gov.ms.sefaz.sat.cotin.dfe.api.lcr.application.domain;

import lombok.*;

import java.io.Serializable;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AutoridadeCertificadora implements Serializable {

    Integer iditc;
    String alias;
    String nome;

}
