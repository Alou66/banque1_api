package sn.banque1.banque1_api.dto;

import lombok.Getter;

@Getter
public class ApiResponse<T> {

    // Champ nécessaire pour rester compatible avec le contrat attendu par auth_api
    // (sn.auth.auth_api.dto.ApiResponse), qui lit ce champ dans ses réponses Feign.
    private final boolean success;
    private final String message;
    private final T data;

    public ApiResponse(String message, T data) {
        this.success = true;
        this.message = message;
        this.data = data;
    }
}
