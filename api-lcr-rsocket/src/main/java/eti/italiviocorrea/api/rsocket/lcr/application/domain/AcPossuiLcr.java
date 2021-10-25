package eti.italiviocorrea.api.rsocket.lcr.application.domain;

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
