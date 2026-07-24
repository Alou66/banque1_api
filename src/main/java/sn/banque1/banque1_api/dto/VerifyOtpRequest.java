package sn.banque1.banque1_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class VerifyOtpRequest {

    @NotBlank(message = "Le téléphone est obligatoire")
    @Pattern(regexp = "^[0-9]{9}$", message = "Le téléphone doit contenir 9 chiffres")
    private String telephone;

    @NotBlank(message = "L'OTP est obligatoire")
    @Pattern(regexp = "^[0-9]{6}$", message = "L'OTP doit contenir exactement 6 chiffres")
    private String otp;
}