package sn.banque1.banque1_api.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CompteResponse {

    private Long id;
    private Long solde;
    private LocalDate dateCreation;
    private String numPiece;
    private String prenom;
    private String nom;
    private String adresse;
    private String telephone;
    private String email;
    private boolean actif;
}
