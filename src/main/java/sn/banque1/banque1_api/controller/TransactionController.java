package sn.banque1.banque1_api.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import sn.banque1.banque1_api.dto.ApiResponse;
import sn.banque1.banque1_api.dto.PaiementExterneRequest;
import sn.banque1.banque1_api.dto.TransactionRequest;
import sn.banque1.banque1_api.dto.TransactionResponse;
import sn.banque1.banque1_api.dto.WalletPaymentResponse;
import sn.banque1.banque1_api.dto.WalletPaymentStatusResponse;
import sn.banque1.banque1_api.helper.TransactionHelper;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transactions")
@SecurityRequirement(name = "bearerAuth")
public class TransactionController {
    private final TransactionHelper transactionHelper;

    @PostMapping("/depot")
    public ResponseEntity<ApiResponse<TransactionResponse>> creerDepot(
            @Valid @RequestBody TransactionRequest request,
            Authentication authentication) {

        TransactionResponse response = transactionHelper.creerDepot(authentication.getName(), request);
        URI uri = URI.create("/api/transactions/" + response.getId());
        return ResponseEntity.created(uri).body(new ApiResponse<>("Dépôt effectué avec succès", response));
    }

    @PostMapping("/retrait")
    public ResponseEntity<ApiResponse<TransactionResponse>> creerRetrait(
            @Valid @RequestBody TransactionRequest request,
            Authentication authentication) {

        TransactionResponse response = transactionHelper.creerRetrait(authentication.getName(), request);
        URI uri = URI.create("/api/transactions/" + response.getId());
        return ResponseEntity.created(uri).body(new ApiResponse<>("Retrait effectué avec succès", response));
    }

    @PostMapping("/paiement")
    public ResponseEntity<ApiResponse<TransactionResponse>> creerPaiement(
            @Valid @RequestBody TransactionRequest request,
            Authentication authentication) {

        TransactionResponse response = transactionHelper.creerPaiement(authentication.getName(), request);
        URI uri = URI.create("/api/transactions/" + response.getId());
        return ResponseEntity.created(uri).body(new ApiResponse<>("Paiement effectué avec succès", response));
    }

    /**
     * Endpoint interne, appelé par gestion_service_api pour payer une prestation.
     * Le client n'a pas de JWT ici : téléphone+PIN prouvent son identité.
     * Protégé par InternalApiKeyFilter (pas par JWT).
     * Retourne toujours 200 OK avec un WalletPaymentResponse : le statut
     * (SUCCESS/FAILED) et le failureReason indiquent le résultat, sans
     * lever d'exception HTTP (évite l'erreur 500 côté client Feign).
     */
    @PostMapping("/paiement-externe")
    public ResponseEntity<WalletPaymentResponse> creerPaiementExterne(
            @Valid @RequestBody PaiementExterneRequest request) {

        WalletPaymentResponse response = transactionHelper.payerAvecPin(request);
        // 200 OK même en cas d'échec : le failureReason porte le message.
        // On ne met pas de Location header si pas de référence (échec).
        if (response.getTransactionReference() != null) {
            URI uri = URI.create("/api/transactions/" + response.getTransactionReference());
            return ResponseEntity.created(uri).body(response);
        }
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint interne pour vérifier le statut d'un paiement par idempotencyKey.
     * Utilisé par gestion_service_api pour la synchronisation en cas de timeout.
     * Protégé par InternalApiKeyFilter.
     */
    @GetMapping("/paiement-externe/status")
    public ResponseEntity<WalletPaymentStatusResponse> getPaymentStatus(
            @RequestParam("idempotencyKey") UUID idempotencyKey) {

        WalletPaymentStatusResponse response = transactionHelper.getPaymentStatusByIdempotencyKey(idempotencyKey);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> findMyTransactions(Authentication authentication) {

        List<TransactionResponse> response = transactionHelper.listerTransactions(authentication.getName());
        return ResponseEntity.ok(new ApiResponse<>("Transactions récupérées avec succés", response));
    }
}
