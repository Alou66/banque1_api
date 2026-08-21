package sn.banque1.banque1_api.controller;

import java.net.URI;
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
import sn.banque1.banque1_api.dto.AuthenticateRequest;
import sn.banque1.banque1_api.dto.CompteAuthResponse;
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

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<CompteResponse>> findMyCompte(Authentication authentication) {

        CompteResponse response = compteHelper.trouverCompte(authentication.getName());
        return ResponseEntity.ok(new ApiResponse<>("Compte récupéré avec succés", response));
    }

    /**
     * Endpoint interne, appelé uniquement par auth_api lors du login pour vérifier
     * le PIN. Protégé par InternalApiKeyFilter (pas par JWT).
     */
    @PostMapping("/authenticate")
    public ResponseEntity<ApiResponse<CompteAuthResponse>> authenticate(
            @Valid @RequestBody AuthenticateRequest request) {

        CompteAuthResponse response = compteHelper.authentifier(request);
        return ResponseEntity.ok(new ApiResponse<>("Authentification réussie", response));
    }

}
