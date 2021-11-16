package eti.italiviocorrea.api.rsocket.lcr.adapters.utils;

import org.bouncycastle.asn1.*;
import org.bouncycastle.asn1.x500.X500Name;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.io.*;
import java.math.BigInteger;
import java.security.NoSuchProviderException;
import java.security.cert.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Component
public class CertificateDigitalUtil {

    private static final String TYPE = "X.509";
    private static final String PROVIDER = "SUN";

    public X509Certificate getCertificate(X509Certificate[] certs) {
        if (certs == null || certs.length == 0) {
            return null;
        }
        return certs[0];
    }

    public X509Certificate getCertificate(InputStream inputStream) throws CertificateException, NoSuchProviderException {
        CertificateFactory factory = CertificateFactory.getInstance(TYPE, PROVIDER);
        return (X509Certificate) factory.generateCertificate(inputStream);
    }

    public ASN1Primitive toDER(byte[] bytes) throws IOException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
                ASN1InputStream asnis = new ASN1InputStream(bais)) {
            return asnis.readObject();
        }
    }

    public String getSubjectAltName(X509Certificate certificate) throws IOException {
        byte[] extensao = certificate.getExtensionValue("2.5.29.17");
        if (extensao == null) {
            return "";
        }
        byte[] octetos = ((DEROctetString) toDER(extensao)).getOctets();

        ASN1Sequence nomes = (ASN1Sequence) toDER(octetos);
        StringBuilder response = new StringBuilder();
        int i = 0;
        for (int len = nomes.size(); i < len; i++) {
            response.append(getName((DERTaggedObject) nomes.getObjectAt(i)));
            response.append('\n');
        }
        return response.toString();
    }

    private String getName(DERTaggedObject nomeDER) {
        StringBuilder response = new StringBuilder();

        switch (nomeDER.getTagNo()) {
            case 0:
                ASN1Sequence objetoASN1 = (ASN1Sequence) nomeDER.getObject();
                String sOid = ((ASN1ObjectIdentifier) objetoASN1.getObjectAt(0)).getId();
                String sVal = convertObjectToHex(objetoASN1.getObjectAt(1));
                response.append(MessageFormat.format("Outros nomes ({0}):\\n\\t{1}", new Object[]{sOid, sVal}));
                break;
            case 1:
                DEROctetString rfc822 = (DEROctetString) nomeDER.getObject();
                String sRfc822 = new String(rfc822.getOctets());
                response.append(MessageFormat.format("Nome RFC 822: {0}", new Object[]{sRfc822}));
                break;
            case 2:
                DEROctetString dns = (DEROctetString) nomeDER.getObject();
                String sDns = new String(dns.getOctets());
                response.append(MessageFormat.format("Nome DNS: {0}", new Object[]{sDns}));
                break;
            case 4:
                ASN1Sequence diretorioASN1 = (ASN1Sequence) nomeDER.getObject();
                X500Name nome = X500Name.getInstance(diretorioASN1);
                response.append(MessageFormat.format("Nome de diretorio: {0}", new Object[]{nome.toString()}));
                break;
            case 6:
                DEROctetString uri = (DEROctetString) nomeDER.getObject();
                String sUri = new String(uri.getOctets());
                response.append(MessageFormat.format("URI: {0}", new Object[]{sUri}));
                break;
            case 7:
                DEROctetString enderecoIp = (DEROctetString) nomeDER.getObject();
                byte[] ip = enderecoIp.getOctets();
                StringBuilder ipString = new StringBuilder();
                int j;
                int i = 0;
                for (int bl = ip.length; i < bl; i++) {
                    j = ip[i];
                    ipString.append(j & 0xFF);
                    if (i + 1 < ip.length) {
                        ipString.append('.');
                    }
                }
                response.append(MessageFormat.format("Endereco IP: {0}", new Object[]{ipString.toString()}));
                break;
            case 8:
                DEROctetString idRegistro = (DEROctetString) nomeDER.getObject();
                byte[] idRegistroByte = idRegistro.getOctets();
                StringBuilder idRegistroString = new StringBuilder();

                for (int jj = 0; jj < idRegistroByte.length; jj++) {
                    int b = idRegistroByte[jj];

                    idRegistroString.append(b & 0xFF);
                    if (jj + 1 < idRegistroByte.length) {
                        idRegistroString.append('.');
                    }
                }

                response.append(MessageFormat.format("ID Registrado: {0}", idRegistroString.toString()));
                break;
            case 3:
            case 5:
            default:
                response.append(MessageFormat.format("Tipo nao suportado - numero {0}", nomeDER.getTagNo()));
        }

        return response.toString();
    }

    /**
     * Converte object em hexadecimal.
     *
     * @param object
     * @return
     */
    private String convertObjectToHex(Object object) {
        if ((object instanceof ASN1String)) {
            return ((ASN1String) object).getString();
        }
        if ((object instanceof ASN1Integer)) {
            return convertByteToStringHex((ASN1Integer) object);
        }
        if ((object instanceof byte[])) {
            return convertByteToStringHex((byte[]) object);
        }
        if ((object instanceof ASN1TaggedObject)) {
            ASN1TaggedObject tagObj = (ASN1TaggedObject) object;
            return "[" + tagObj.getTagNo() + "] "
                    + convertObjectToHex(tagObj.getObject());
        }
        DERUTF8String response = DERUTF8String.getInstance(object);
        return response.getString();
    }

    private String convertByteToStringHex(byte[] bytes) {

        StringBuilder response = new StringBuilder(new BigInteger(1, bytes).toString(16).toUpperCase());

        if (response.length() > 4) {
            for (int i = 4; i < response.length(); i += 5) {
                response.insert(i, ' ');
            }
        }

        return response.toString();
    }

    private String convertByteToStringHex(ASN1Integer aSN1Integer) {

        String value = aSN1Integer.getValue().toString(16).toUpperCase();
        StringBuilder response = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            response.append(value.charAt(i));
            if (((i + 1) % 4 == 0) && (i + 1 != value.length())) {
                response.append(' ');
            }
        }

        return response.toString();
    }

    public X509CRL getCRL(File crl) throws CertificateException, NoSuchProviderException, CRLException, FileNotFoundException, IOException {
        try (FileInputStream fis = new FileInputStream(crl)) {
            CertificateFactory factory = CertificateFactory.getInstance(TYPE, PROVIDER);
            return (X509CRL) factory.generateCRL(fis);
        }
    }

    public List<String> getCRs(File file) throws CertificateException, NoSuchProviderException, CRLException, IOException {
        X509CRL crl = getCRL(file);
        List<String> crs = new ArrayList<>();
        if (!ObjectUtils.isEmpty(crl.getRevokedCertificates())) {
            crl.getRevokedCertificates().forEach((cRLEntry) -> {
                crs.add(cRLEntry.getSerialNumber().toString());
            });
        }
        return crs;
    }

    public String getKeyCompare(X509Certificate certificate) {
        return certificate.getIssuerDN().getName().concat(certificate.getSerialNumber().toString());
    }

}
