package br.gov.ms.sefaz.sat.cotin.dfe.api.lcr.domain.models;

import lombok.*;

import java.io.Serializable;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AcPossuiLcr implements Serializable {

    Integer iditc;
    Integer idlcr;
}
