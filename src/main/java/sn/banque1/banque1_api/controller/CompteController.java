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
import sn.banque1.banque1_api.dto.CompteRequest;
import sn.banque1.banque1_api.dto.CompteResponse;
import sn.banque1.banque1_api.helper.CompteHelper;

@RestController
@RequestMapping("/api/comptes")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class CompteController {

    private final CompteHelper compteHelper;

    @PostMapping
    public ResponseEntity<ApiResponse<CompteResponse>> creer(
            @Valid @RequestBody CompteRequest request) {

        CompteResponse response = compteHelper.creerCompte(request);
        URI uri = URI.create("/api/comptes/" + response.getId());
        return ResponseEntity.created(uri).body(new ApiResponse<>("Compte créé avec succés", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CompteResponse>>> findAll() {

        List<CompteResponse> responses = compteHelper.listerComptes();
        return ResponseEntity.ok(new ApiResponse<>("Liste des comptes récupérée avec succès", responses));
    }

    @GetMapping("/{telephone}")
    public ResponseEntity<ApiResponse<CompteResponse>> findByTelephone(@PathVariable String telephone) {
        CompteResponse response = compteHelper.trouverCompte(telephone);
        return ResponseEntity.ok(new ApiResponse<>("Compte récupéré avec succés", response));
    }

}
