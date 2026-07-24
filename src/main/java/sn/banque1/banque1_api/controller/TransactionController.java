package sn.banque1.banque1_api.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import sn.banque1.banque1_api.dto.ApiResponse;
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
    public ResponseEntity<ApiResponse<TransactionResponse>> creerDepot(@Valid @RequestBody TransactionRequest request) {

        TransactionResponse response = transactionHelper.creerDepot(request);
        URI uri = URI.create("/api/transactions/" + response.getId());
        return ResponseEntity.created(uri).body(new ApiResponse<>("Dépot effectué avec succés", response));
    }

    @PostMapping("/retrait")
    public ResponseEntity<ApiResponse<TransactionResponse>> creerRetrait(
            @Valid @RequestBody TransactionRequest request) {

        TransactionResponse response = transactionHelper.creerRetrait(request);
        URI uri = URI.create("/api/transactions/" + response.getId());
        return ResponseEntity.created(uri).body(new ApiResponse<>("Retrait effectué avec succés", response));
    }

    @PostMapping("/paiement")
    public ResponseEntity<ApiResponse<TransactionResponse>> creerPaiement(
            @Valid @RequestBody TransactionRequest request) {

        TransactionResponse response = transactionHelper.creerPaiement(request);
        URI uri = URI.create("/api/transactions/" + response.getId());
        return ResponseEntity.created(uri).body(new ApiResponse<>("Paiement effectué avec succés", response));
    }

    @GetMapping("/{telephone}")
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> findByTelephone(@PathVariable String telephone) {
        List<TransactionResponse> response = transactionHelper.listerTransactions(telephone);
        return ResponseEntity.ok(new ApiResponse<>("Compte récupéré avec succés", response));
    }
}
