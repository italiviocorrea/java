package eti.italiviocorrea.api.rsocket.lcr.adapters.utils;

import java.util.List;

public class ICPBrasilUtil {

    private static final List<String> OIDS_PESSOA_FISICA = List.of("2.16.76.1.3.1", "2.16.76.1.3.5");
    private static final List<String> OIDS_PESSOA_JURIDICA = List.of("2.16.76.1.3.2", "2.16.76.1.3.3", "2.16.76.1.3.4");

    public static boolean isIcpBrasil(X509CertificateWrapper certificate) throws Exception {
        List oids = certificate.getOIDsSubjectAltName();
        return (oids.containsAll(OIDS_PESSOA_FISICA)) || (oids.containsAll(OIDS_PESSOA_JURIDICA));
    }
}
