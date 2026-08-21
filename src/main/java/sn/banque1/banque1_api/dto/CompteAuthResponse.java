package sn.banque1.banque1_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Réponse minimale exposée à auth_api lors de la vérification du PIN :
 * pas de solde, adresse ou numéro de pièce, ce service n'en a pas besoin.
 */
@Getter
@AllArgsConstructor
public class CompteAuthResponse {

    private Long id;
    private String telephone;
    private String nom;
    private String prenom;
    private boolean actif;
}
