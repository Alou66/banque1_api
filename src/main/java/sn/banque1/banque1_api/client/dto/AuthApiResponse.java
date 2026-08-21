package sn.banque1.banque1_api.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Reflet minimal de l'enveloppe ApiResponse renvoyée par auth_api : seul le
 * champ "data" nous intéresse ici.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthApiResponse<T> {
    private T data;
}
