package sn.banque1.banque1_api.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import sn.banque1.banque1_api.dto.ApiResponse;
import sn.banque1.banque1_api.dto.PaiementExterneRequest;
import sn.banque1.banque1_api.dto.TransactionRequest;
import sn.banque1.banque1_api.dto.TransactionResponse;
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
        return ResponseEntity.created(uri).body(new ApiResponse<>("Dépot effectué avec succés", response));
    }

    @PostMapping("/retrait")
    public ResponseEntity<ApiResponse<TransactionResponse>> creerRetrait(
            @Valid @RequestBody TransactionRequest request,
            Authentication authentication) {

        TransactionResponse response = transactionHelper.creerRetrait(authentication.getName(), request);
        URI uri = URI.create("/api/transactions/" + response.getId());
        return ResponseEntity.created(uri).body(new ApiResponse<>("Retrait effectué avec succés", response));
    }

    @PostMapping("/paiement")
    public ResponseEntity<ApiResponse<TransactionResponse>> creerPaiement(
            @Valid @RequestBody TransactionRequest request,
            Authentication authentication) {

        TransactionResponse response = transactionHelper.creerPaiement(authentication.getName(), request);
        URI uri = URI.create("/api/transactions/" + response.getId());
        return ResponseEntity.created(uri).body(new ApiResponse<>("Paiement effectué avec succés", response));
    }

    /**
     * Endpoint interne, appelé par gestion_service_api pour payer une prestation.
     * Le client n'a pas de JWT ici : téléphone+PIN prouvent son identité.
     * Protégé par InternalApiKeyFilter (pas par JWT).
     */
    @PostMapping("/paiement-externe")
    public ResponseEntity<ApiResponse<TransactionResponse>> creerPaiementExterne(
            @Valid @RequestBody PaiementExterneRequest request) {

        TransactionResponse response = transactionHelper.payerAvecPin(request);
        URI uri = URI.create("/api/transactions/" + response.getId());
        return ResponseEntity.created(uri).body(new ApiResponse<>("Paiement effectué avec succés", response));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> findMyTransactions(Authentication authentication) {

        List<TransactionResponse> response = transactionHelper.listerTransactions(authentication.getName());
        return ResponseEntity.ok(new ApiResponse<>("Transactions récupérées avec succés", response));
    }
}
