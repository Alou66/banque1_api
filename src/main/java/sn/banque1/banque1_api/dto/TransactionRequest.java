package sn.banque1.banque1_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionRequest {

    @NotNull(message = "Le montant est obligatoire")
    @Positive(message = "Le montant doit etre positif")
    private Long montant;

    @NotBlank(message = "Le téléphone est obligatoire")
    @Pattern(regexp = "^(77|78)[0-9]{7}$", message = "Le téléphone doit commencer par 77 ou 78 et contenir 9 chiffres")
    private String telephone;

}
