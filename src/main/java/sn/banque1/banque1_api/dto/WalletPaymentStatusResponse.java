package sn.banque1.banque1_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Réponse du Wallet lors d'une vérification de statut par idempotencyKey.
 * Utilisé pour la gestion des timeouts et la compensation.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletPaymentStatusResponse {

    private UUID idempotencyKey;

    /** SUCCESS | FAILED | PENDING | NOT_FOUND */
    private String status;

    private String transactionReference;
    private String failureReason;
}