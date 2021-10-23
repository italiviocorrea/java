package br.gov.ms.sefaz.sat.cotin.dfe.api.lcr.adapters.outbound.persistence.r2dbc.entities;

import lombok.Data;

import java.io.Serializable;

@Data
public class AcPossuiLcrPk implements Serializable {

    Integer iditc;
    Integer idlcr;

}
