package sn.banque1.banque1_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class ChangePinRequest {

    @NotBlank(message = "Le pin actuel est obligatoire")
    @Pattern(regexp = "^[0-9]{4}$", message = "Le pin doit contenir exactement 4 chiffres")
    private String currentPin;

    @NotBlank(message = "Le nouveau pin est obligatoire")
    @Pattern(regexp = "^[0-9]{4}$", message = "Le pin doit contenir exactement 4 chiffres")
    private String newPin;
}
