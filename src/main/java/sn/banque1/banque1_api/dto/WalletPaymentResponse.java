package sn.banque1.banque1_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Réponse d'un paiement de prestation initié par un service tiers
 * (ex. gestion_service_api). Format attendu par le client Feign WalletClient.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletPaymentResponse {

    /** SUCCESS | FAILED */
    private String status;

    /** Référence unique de transaction côté banque. */
    private String transactionReference;

    private UUID idempotencyKey;

    /** Message d'erreur en cas d'échec. */
    private String failureReason;

}