package eti.italivio.correa.api.mdfes.hadoop.persistence.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author gelson
 */
@Component
@Slf4j
public class SecretUtil {

    @Value("${app.secrets.path}")
    private String secretsPath;

    public String getPassword(String secret) {
        String password = "";
        try {
            Resource resource = new FileSystemResource(String.format("%s%s", secretsPath, secret));
            if (resource.exists()) {
                password = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8).trim();
            } else {
                password = System.getenv(secret.toUpperCase().replace("-", "_"));
            }

        } catch (IOException ex) {
            log.error(ex.toString(), ex);
        }
        return password;
    }
}
