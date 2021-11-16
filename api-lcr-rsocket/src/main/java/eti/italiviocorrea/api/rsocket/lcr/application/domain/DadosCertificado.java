package eti.italiviocorrea.api.rsocket.lcr.application.domain;

import lombok.*;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;

import javax.security.auth.x500.X500Principal;
import java.io.Serializable;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.List;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class DadosCertificado implements Serializable {

    private String cnpj;
    private String cpf;
    private String cn;
    private String issuerCn;
    private String serialNumber;
    private int versao;
    private int basicConstraints;
    private boolean[] keyUsage;
    private Date notAfter;
    private Date notBefore;
    private String c;
    private String emailAddress;
    private List<String> pontosDistribuicaoLCR;
    private X509Certificate certificate;
    private Boolean hasNonRepudiation;
    private Boolean isDigitalSignature;
    private Boolean isClientAuthentication;

    public String getNomeAC() {
        X500Principal xname = new X500Principal(getCertificate().getIssuerDN().getName());
        X500Name x500name = new X500Name(xname.getName());
        RDN[] rdns = x500name.getRDNs(BCStyle.CN);
        RDN rdn = rdns[0];
        return IETFUtils.valueToString(rdn.getFirst().getValue());

    }

    public String getNomeCN() {
        try {
            X500Name xname = new X500Name(getCertificate().getSubjectX500Principal().getName());
            return IETFUtils.valueToString(xname.getRDNs(BCStyle.CN)[0].getFirst().getValue());
        } catch (Exception ex) {
            return null;
        }
    }

    public String getNomeC() {
        try {
            X500Name xname = new X500Name(getCertificate().getSubjectX500Principal().getName());
            return IETFUtils.valueToString(xname.getRDNs(BCStyle.C)[0].getFirst().getValue());
        } catch (Exception ex) {
            return null;
        }
    }

    public String getNomeEmailAddress() {
        try {
            X500Name xname = new X500Name(getCertificate().getSubjectX500Principal().toString());
            return IETFUtils.valueToString(xname.getRDNs(BCStyle.EmailAddress)[0].getFirst().getValue());
        } catch (Exception ex) {
            return null;
        }
    }

}
