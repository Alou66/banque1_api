package sn.banque1.banque1_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CompteRequest {

    @NotBlank(message = "Le prénom est obligatoire !")
    @Size(min = 2, message = "Le prénom doit contenir minimun 2 caracteres")
    private String prenom;

    @NotBlank(message = "Le nom est obligatoire !")
    @Size(min = 2, message = "Le nom doit contenir minimun 2 caracteres")
    private String nom;

    private String adresse;

    @NotBlank(message = "Le téléphone est obligatoire")
    private String telephone;

    @NotBlank(message = "Le pin est obligatoire")
    private String pin;

    @Pattern(regexp = "^[0-9]{10}$", message = "Le numéro de pièce doit contenir exactement 10 chiffres")
    private String numPiece;

}
