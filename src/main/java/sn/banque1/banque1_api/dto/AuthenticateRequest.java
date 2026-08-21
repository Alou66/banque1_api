package sn.banque1.banque1_api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AuthenticateRequest {

    @NotBlank(message = "Le téléphone est obligatoire")
    private String telephone;

    @NotBlank(message = "Le pin est obligatoire")
    private String pin;
}
