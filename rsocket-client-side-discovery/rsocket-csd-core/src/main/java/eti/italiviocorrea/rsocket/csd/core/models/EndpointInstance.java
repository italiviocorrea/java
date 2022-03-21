package eti.italiviocorrea.rsocket.csd.core.models;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class EndpointInstance {

    private String host;
    private int port;

}
