package eti.italiviocorrea.api.rsocket.lcr.application.domain;

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
