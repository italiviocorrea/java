package eti.italiviocorrea.api.rsocket.lcr.adapters.utils;

import org.bouncycastle.util.encoders.Base64;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MessageDigestEncoder {

    private static final char[] HEX = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private final String algorithm;
    private int iterations = 1;
    private boolean encodeHashAsBase64 = false;

    public MessageDigestEncoder(String algorithm) {
        this(algorithm, false);
    }

    public MessageDigestEncoder(String algorithm, boolean encodeHashAsBase64) {
        this.algorithm = algorithm;
    }

    public String encode(String rawKey) {

        byte[] digest;

        digest = digest(rawKey.getBytes(StandardCharsets.UTF_8));

        if (isEncodeHashAsBase64()) {
            return new String(Base64.encode(digest));
        } else {
            return new String(encodeHexadecimal(digest));
        }

    }

    public String encode(byte[] rawKey) {

        rawKey = digest(rawKey);

        if (isEncodeHashAsBase64()) {
            return new String(Base64.encode(rawKey));
        } else {
            return new String(encodeHexadecimal(rawKey));
        }

    }

    public byte[] digest(byte[] digest) {

        MessageDigest messageDigest = getMessageDigest();

        digest = messageDigest.digest(digest);

        // "stretch" the encoded value if configured to do so
        for (int i = 1; i < iterations; i++) {
            digest = messageDigest.digest(digest);
        }

        return digest;
    }

    public boolean isValid(String encKey, String rawKey) {
        String pass1 = "" + encKey;
        String pass2 = encode(rawKey);

        return pass1.equals(pass2);
    }

    public boolean isValid(String encKey, byte[] rawKey) {
        String pass1 = encKey;
        String pass2 = encode(rawKey);
        return pass1.equals(pass2);
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) throws Exception {
        if (iterations > 0) {
            this.iterations = iterations;
        } else {
            throw new Exception("Iterations value must be greater than zero");
        }
    }

    public boolean isEncodeHashAsBase64() {
        return encodeHashAsBase64;
    }

    public void setEncodeHashAsBase64(boolean encodeHashAsBase64) {
        this.encodeHashAsBase64 = encodeHashAsBase64;
    }

    protected final MessageDigest getMessageDigest() throws IllegalArgumentException {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalArgumentException("No such algorithm [" + algorithm + "]");
        }
    }

    public static char[] encodeHexadecimal(byte[] bytes) {
        final int nBytes = bytes.length;
        char[] result = new char[2 * nBytes];
        int j = 0;
        for (int i = 0; i < nBytes; i++) {
            // Char for top 4 bits
            result[j++] = HEX[(240 & bytes[i]) >>> 4];
            // Bottom 4
            result[j++] = HEX[(15 & bytes[i])];
        }
        return result;
    }
}
