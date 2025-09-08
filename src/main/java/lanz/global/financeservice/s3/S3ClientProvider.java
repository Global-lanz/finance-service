package lanz.global.financeservice.s3;

import lanz.global.financeservice.config.ServiceConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@Configuration
@RequiredArgsConstructor
public class S3ClientProvider {

    private final ServiceConfig serviceConfig;

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .endpointOverride(URI.create(serviceConfig.getS3().getUrl()))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(serviceConfig.getS3().getKey(), serviceConfig.getS3().getSecret())
                ))
                .region(Region.US_EAST_1)
                .forcePathStyle(true)
                .build();
    }
}
