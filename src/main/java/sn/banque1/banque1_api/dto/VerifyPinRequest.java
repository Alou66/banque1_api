package sn.banque1.banque1_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class VerifyPinRequest {

    @NotBlank(message = "Le pin est obligatoire")
    @Pattern(regexp = "^[0-9]{4}$", message = "Le pin doit contenir exactement 4 chiffres")
    private String pin;
}
