package sn.banque1.banque1_api.client;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import sn.banque1.banque1_api.client.dto.AuthApiResponse;

/**
 * Client HTTP vers auth_api pour la vérification/consommation de l'OTP lors
 * de la création d'un compte. Appels protégés côté auth_api par la clé API
 * interne (déjà injectée par défaut dans le RestClient, voir RestClientConfig).
 */
@Component
@RequiredArgsConstructor
public class AuthClient {

    private final RestClient authApiRestClient;

    public boolean checkOtp(String telephone) {
        AuthApiResponse<Boolean> response = authApiRestClient.get()
                .uri("/api/auth/check/{telephone}", telephone)
                .retrieve()
                .body(new ParameterizedTypeReference<AuthApiResponse<Boolean>>() {
                });

        return response != null && Boolean.TRUE.equals(response.getData());
    }

    public void consumeOtp(String telephone) {
        authApiRestClient.post()
                .uri("/api/auth/consume/{telephone}", telephone)
                .retrieve()
                .toBodilessEntity();
    }
}
