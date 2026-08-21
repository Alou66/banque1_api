package sn.banque1.banque1_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

/**
 * Requête de paiement initiée par un service tiers (ex. gestion_service_api)
 * pour le compte d'un client identifié par téléphone + PIN, sans session JWT.
 */
@Getter
public class PaiementExterneRequest {

    @NotBlank(message = "Le téléphone est obligatoire")
    private String telephone;

    @NotBlank(message = "Le pin est obligatoire")
    private String pin;

    @NotNull(message = "Le montant est obligatoire")
    @Positive(message = "Le montant doit etre positif")
    private Long montant;
}
