package sn.banque1.banque1_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdateCompteRequest {

    @NotBlank(message = "Le prénom est obligatoire !")
    @Size(min = 2, message = "Le prénom doit contenir minimun 2 caracteres")
    private String prenom;

    @NotBlank(message = "Le nom est obligatoire !")
    @Size(min = 2, message = "Le nom doit contenir minimun 2 caracteres")
    private String nom;

    @NotBlank(message = "Le téléphone est obligatoire")
    @Pattern(regexp = "^(77|78|70)[0-9]{7}$", message = "Le téléphone doit commencer par 77, 78 ou 70 et contenir 9 chiffres au total")
    private String telephone;
}
