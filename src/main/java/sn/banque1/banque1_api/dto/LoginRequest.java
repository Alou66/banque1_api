package sn.banque1.banque1_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class LoginRequest {

    @NotBlank(message = "Le téléphone est obligatoire")
    private String telephone;

    @NotBlank(message = "Le PIN est obligatoire !")
    @Pattern(regexp = "^[0-9]{4}$", message = "Le PIN doit contenir exactement 4 chiffres")
    private String pin;
}
