package lanz.global.financeservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("gl.service.config")
public class ServiceConfig {

    private final Security security = new Security();
    private final S3 s3 = new S3();

    private String serviceName;

    @Getter
    @Setter
    public static class S3 {
        private String url;
        private String key;
        private String secret;
    }

    @Getter
    @Setter
    public static class Security {
        private String apiSecret;
        private String originAllowed;
        private String basicUser;
        private String basicPassword;
    }
}
