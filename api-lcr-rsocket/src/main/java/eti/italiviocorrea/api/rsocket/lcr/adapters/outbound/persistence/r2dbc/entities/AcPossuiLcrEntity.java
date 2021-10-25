package eti.italiviocorrea.api.rsocket.lcr.adapters.outbound.persistence.r2dbc.entities;

import lombok.*;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(value = "ac_possui_lcr")
public class AcPossuiLcrEntity implements Serializable {

    Integer iditc;
    Integer idlcr;
}
