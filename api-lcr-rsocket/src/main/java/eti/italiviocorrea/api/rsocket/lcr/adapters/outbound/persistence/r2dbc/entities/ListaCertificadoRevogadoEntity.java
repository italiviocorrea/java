package eti.italiviocorrea.api.rsocket.lcr.adapters.outbound.persistence.r2dbc.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Table(value = "lista_certificado_revogado")
public class ListaCertificadoRevogadoEntity implements Serializable {

    @Id
    @Column("idlcr")
    Integer id;

    @Column("indi_lcr_delta")
    int indiLcrDelta;

    @Column("indi_atualz_lcr")
    String indiAtualzLcr;

    @Column("info_url_lcr")
    String infoUrlLcr;

}
