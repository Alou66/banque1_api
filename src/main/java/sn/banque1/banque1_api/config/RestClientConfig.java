package sn.banque1.banque1_api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${auth.api.url}")
    private String authApiUrl;

    @Value("${internal.api.key}")
    private String internalApiKey;

    @Bean
    public RestClient authApiRestClient() {
        return RestClient.builder()
                .baseUrl(authApiUrl)
                .defaultHeader("X-Internal-Api-Key", internalApiKey)
                .build();
    }
}
