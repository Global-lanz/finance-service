package lanz.global.financeservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("gl.service.config")
public class ServiceConfig {

    private final Security security = new Security();
    private String serviceName;

    @Getter
    @Setter
    public static class Security {
        private String serviceSecret;
        private String apiSecret;
        private String originAllowed;
        private String basicUser;
        private String basicPassword;
    }
}
