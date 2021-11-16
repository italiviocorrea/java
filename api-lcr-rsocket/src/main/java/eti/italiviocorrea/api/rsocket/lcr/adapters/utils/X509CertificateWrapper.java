package eti.italiviocorrea.api.rsocket.lcr.adapters.utils;

import eti.italiviocorrea.api.rsocket.lcr.application.domain.DadosCertificado;
import org.bouncycastle.asn1.*;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class X509CertificateWrapper {

    public static final ASN1ObjectIdentifier CNPJ = new ASN1ObjectIdentifier("2.16.76.1.3.3");
    public static final ASN1ObjectIdentifier CPF = new ASN1ObjectIdentifier("2.16.76.1.3.1");

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    final private CertificateDigitalUtil util = new CertificateDigitalUtil();

    private X509Certificate[] certificates;
    private final X509Certificate certificate;

    public X509CertificateWrapper(X509Certificate certificate) {
        this.certificate = certificate;
    }

    public X509Certificate getCertificate() {
        return certificate;
    }

    public X509Certificate[] getCertificates() {
        return certificates;
    }

    public String getSerialNumber() {
        return certificate.getSerialNumber().toString();
    }

    public String getVersion() {
        return "Version " + String.valueOf(certificate.getVersion());
    }

    public String getSigAlgName() {
        return certificate.getSigAlgName();
    }

    public String getSubjectDN() {
        return certificate.getSubjectDN().getName();
    }

    public Date getNotBefore() {
        return certificate.getNotBefore();
    }

    public Date getNotAfter() {
        return certificate.getNotAfter();
    }

    public byte[] getEncoded() throws CertificateEncodingException {
        return certificate.getEncoded();
    }

    /**
     * Retorna os OIDs dos subject alternative name.
     *
     * @return
     * @throws IOException
     */
    public List<String> getOIDsSubjectAltName() throws IOException {

        ArrayList response = new ArrayList();
        byte[] extensao = certificate.getExtensionValue("2.5.29.17");
        if (extensao != null) {
            byte[] octetos = ((DEROctetString) util.toDER(extensao)).getOctets();

            ASN1Sequence nomes = (ASN1Sequence) util.toDER(octetos);

            int i = 0;
            for (int len = nomes.size(); i < len; i++) {
                DLTaggedObject objeto = (DLTaggedObject) nomes.getObjectAt(i);
                if (objeto.getTagNo() == 0) {
                    ASN1Sequence objetoCodificadoASN1 = (ASN1Sequence) objeto.getObject();
                    String sOid = ((ASN1ObjectIdentifier) objetoCodificadoASN1.getObjectAt(0)).getId();
                    response.add(sOid);
                }
            }
        }
        return response;
    }

    public String getSHA1FingerPrint() throws CertificateEncodingException {
        byte[] fingerprint;
        MessageDigestEncoder digistEncoder = new MessageDigestEncoder("SHA-1");
        fingerprint = digistEncoder.digest(getEncoded());
        return formatFingerPrint(fingerprint);
    }

    public String getSHA256FingerPrint() throws CertificateEncodingException {
        byte[] fingerprint;
        MessageDigestEncoder digistEncoder = new MessageDigestEncoder("SHA-256");
        fingerprint = digistEncoder.digest(getEncoded());
        return formatFingerPrint(fingerprint);
    }

    public String getSHA256EncodeHashAsBase64() throws CertificateEncodingException {
        MessageDigestEncoder digestEncoder = new MessageDigestEncoder("SHA-256", true);
        return digestEncoder.encode(getEncoded());
    }

    /**
     *
     * @param fingerPrint
     * @return
     */
    private static String formatFingerPrint(byte[] fingerPrint) {

        StringBuilder stringBuffer = new StringBuilder(new BigInteger(1, fingerPrint).toString(16).toUpperCase());

        if (stringBuffer.length() % 2 == 1) {
            stringBuffer.insert(0, '0');
        }
        if (stringBuffer.length() > 2) {
            for (int iCnt = 2; iCnt < stringBuffer.length(); iCnt += 3) {
                stringBuffer.insert(iCnt, ':');
            }
        }
        return stringBuffer.toString();
    }

    public DadosCertificado getDadosCertificado() throws CertificateParsingException, IOException {
        DadosCertificado dadosCertificado = new DadosCertificado();
        Collection<?> alternativeNames = JcaX509ExtensionUtils.getSubjectAlternativeNames(certificate);
        alternativeNames.stream().filter((alternativeName)
                -> (alternativeName instanceof ArrayList)).map((alternativeName)
                -> (ArrayList<?>) alternativeName).map((listOfValues)
                -> listOfValues.get(1)).filter((value)
                -> (value instanceof DLSequence)).map((value)
                -> (DLSequence) value).forEachOrdered((dLSequence) -> {
            ASN1ObjectIdentifier aSN1ObjectIdentifier = (ASN1ObjectIdentifier) dLSequence.getObjectAt(0);
            DLTaggedObject dLTaggedObject = (DLTaggedObject) dLSequence.getObjectAt(1);
            ASN1Primitive derObject = dLTaggedObject.getObject();
            String valueOfTag = "";
            if (derObject instanceof DEROctetString) {
                DEROctetString octet = (DEROctetString) derObject;
                valueOfTag = new String(octet.getOctets());
            } else if (derObject instanceof DERPrintableString) {
                DERPrintableString octet = (DERPrintableString) derObject;
                valueOfTag = new String(octet.getOctets());
            } else if (derObject instanceof DERUTF8String) {
                DERUTF8String str = (DERUTF8String) derObject;
                valueOfTag = str.getString();
            }
            if ((valueOfTag != null) && (!"".equals(valueOfTag))) {
                if (aSN1ObjectIdentifier.equals(CNPJ)) {
                    dadosCertificado.setCnpj(valueOfTag);
                } else if (aSN1ObjectIdentifier.equals(CPF)) {
                    try {
                        String cpf = valueOfTag.substring(8, 19);
                        dadosCertificado.setCpf(cpf);
                    } catch (Exception ex) {
                        log.error(ex.toString());
                        dadosCertificado.setCpf(valueOfTag);
                    }
                }
            }
        });
        dadosCertificado.setCn(certificate.getSubjectX500Principal().getName());
        dadosCertificado.setIssuerCn(certificate.getIssuerX500Principal().getName());
        dadosCertificado.setSerialNumber(certificate.getSerialNumber().toString());
        dadosCertificado.setPontosDistribuicaoLCR(this.getCrlDistributionPoints(certificate));
        dadosCertificado.setBasicConstraints(certificate.getBasicConstraints());
        dadosCertificado.setVersao(certificate.getVersion());
        dadosCertificado.setNotAfter(certificate.getNotAfter());
        dadosCertificado.setNotBefore(certificate.getNotBefore());
        dadosCertificado.setCertificate(certificate);
        dadosCertificado.setHasNonRepudiation(hasNonRepudiationUsage(certificate));
        dadosCertificado.setIsDigitalSignature(hasDigitalSignature(certificate));
        dadosCertificado.setIsClientAuthentication(isClientAuthentication(certificate));
        return dadosCertificado;
    }

    public boolean hasCNPJExtension(X509Certificate cert) throws IOException, CertificateParsingException {
        boolean hasCNPJCertificate = false;
        Collection<?> alternativeNames = JcaX509ExtensionUtils.getSubjectAlternativeNames(certificate);
        for (Object alternativeName : alternativeNames) {
            if (alternativeName instanceof ArrayList) {
                ArrayList<?> listOfValues = (ArrayList<?>) alternativeName;
                Object value = listOfValues.get(1);
                if (value instanceof DLSequence) {
                    DLSequence dLSequence = (DLSequence) value;
                    ASN1ObjectIdentifier aSN1ObjectIdentifier = (ASN1ObjectIdentifier) dLSequence.getObjectAt(0);
                    DLTaggedObject dLTaggedObject = (DLTaggedObject) dLSequence.getObjectAt(1);
                    ASN1Primitive derObject = dLTaggedObject.getObject();
                    String valueOfTag = "";
                    if (derObject instanceof DEROctetString) {
                        DEROctetString octet = (DEROctetString) derObject;
                        valueOfTag = new String(octet.getOctets());
                    } else if (derObject instanceof DERPrintableString) {
                        DERPrintableString octet = (DERPrintableString) derObject;
                        valueOfTag = new String(octet.getOctets());
                    } else if (derObject instanceof DERUTF8String) {
                        DERUTF8String str = (DERUTF8String) derObject;
                        valueOfTag = str.getString();
                    }
                    if ((valueOfTag != null) && (!"".equals(valueOfTag))) {
                        if (aSN1ObjectIdentifier.equals(CNPJ)) {
                            hasCNPJCertificate = true;
                        }
                    }
                }
            }
        }
        return hasCNPJCertificate;
    }

    /**
     *
     * @param cert
     * @return
     * @throws IOException
     */
    public List<String> getCrlDistributionPoints(X509Certificate cert) throws IOException {
        byte[] crldpExt = cert.getExtensionValue(Extension.cRLDistributionPoints.getId());
        if (crldpExt == null) {
            return new ArrayList<>();
        }

        try (ByteArrayInputStream bais = new ByteArrayInputStream(crldpExt);
                ASN1InputStream asnis = new ASN1InputStream(bais);
                ByteArrayInputStream bais1 = new ByteArrayInputStream(((DEROctetString) asnis.readObject()).getOctets());
                ASN1InputStream asnis1 = new ASN1InputStream(bais1)) {

            ASN1Primitive aSN1Primitive = asnis1.readObject();
            CRLDistPoint distPoint = CRLDistPoint.getInstance(aSN1Primitive);
            List<String> crlUrls = new ArrayList<>();
            for (DistributionPoint dp : distPoint.getDistributionPoints()) {
                DistributionPointName dpn = dp.getDistributionPoint();
                // Look for URIs in fullName
                if (dpn != null && dpn.getType() == DistributionPointName.FULL_NAME) {
                    GeneralName[] genNames = GeneralNames.getInstance(
                            dpn.getName()).getNames();
                    for (GeneralName genName : genNames) {
                        if (genName.getTagNo() == GeneralName.uniformResourceIdentifier) {
                            String url = DERIA5String.getInstance(genName.getName()).getString();
                            crlUrls.add(url);
                        }
                    }
                }
            }
            return crlUrls;
        }
    }
    
    public boolean hasNonRepudiationUsage(X509Certificate cert){
        boolean hasNonRepudiationUsage = false;
        
        if(cert != null && cert.getKeyUsage() != null){
            hasNonRepudiationUsage = cert.getKeyUsage()[1];
            
        }
        
        return hasNonRepudiationUsage;
    }
    
    public boolean hasDigitalSignature(X509Certificate cert){
        boolean hasDigitalSignature = false;
        
        if(cert != null && cert.getKeyUsage() != null){
            hasDigitalSignature = cert.getKeyUsage()[0];
            
        }
        
        return hasDigitalSignature;
    }
    
    
    public boolean isClientAuthentication(X509Certificate cert) throws CertificateParsingException {
        List<String> usos = cert.getExtendedKeyUsage();
        
        if(usos!= null){
            if (usos.stream().anyMatch(uso -> (uso.equals("1.3.6.1.5.5.7.3.2")))) {
                return true;
            }
        }
        return false;
        
    }
}
