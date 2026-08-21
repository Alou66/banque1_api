package sn.banque1.banque1_api.helper;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import sn.banque1.banque1_api.client.AuthClient;
import sn.banque1.banque1_api.dto.AuthenticateRequest;
import sn.banque1.banque1_api.dto.CompteAuthResponse;
import sn.banque1.banque1_api.dto.CompteRequest;
import sn.banque1.banque1_api.dto.CompteResponse;
import sn.banque1.banque1_api.exception.AuthenticationException;
import sn.banque1.banque1_api.exception.BadRequestException;
import sn.banque1.banque1_api.mapper.CompteMapper;
import sn.banque1.banque1_api.model.Compte;
import sn.banque1.banque1_api.service.CompteService;

@Component
@RequiredArgsConstructor
public class CompteHelper {

    private final CompteService compteService;
    private final CompteMapper compteMapper;
    private final AuthClient authClient;
    private final PasswordEncoder passwordEncoder;

    public CompteResponse creerCompte(CompteRequest compteRequest) {
        String telephone = normalizeTelephone(compteRequest.getTelephone());

        if (!authClient.checkOtp(telephone)) {
            throw new BadRequestException("Veuillez valider votre numéro via OTP");
        }

        compteService.findByTelephone(telephone).ifPresent(compte -> {
            throw new BadRequestException("Ce numéro de téléphone existe déjà");
        });

        Compte compte = compteMapper.toCompte(compteRequest);
        compte.setSolde(0);
        Compte c = compteService.save(compte);

        authClient.consumeOtp(telephone);

        return compteMapper.toResponse(c);
    }

    public CompteResponse trouverCompte(String telephone) {
        Compte compte = compteService.findByTelephone(telephone)
                .orElseThrow(() -> new sn.banque1.banque1_api.exception.ResourceNotFoundException(
                        "Compte introuvable avec le téléphone : " + telephone));
        return compteMapper.toResponse(compte);
    }

    /**
     * Vérification interne du téléphone + PIN, appelée par auth_api lors du login.
     */
    public CompteAuthResponse authentifier(AuthenticateRequest request) {
        Compte compte = verifierPin(request.getTelephone(), request.getPin());

        return new CompteAuthResponse(
                compte.getId(),
                compte.getTelephone(),
                compte.getNom(),
                compte.getPrenom(),
                compte.isActif());
    }

    /**
     * Vérifie téléphone + PIN et retourne le compte correspondant. Réutilisée par
     * authentifier() (login via auth_api) et par le paiement de prestation externe
     * (gestion_service_api), qui n'ont ni l'un ni l'autre de session JWT à ce stade.
     */
    public Compte verifierPin(String telephone, String pin) {
        Compte compte = compteService.findByTelephone(normalizeTelephone(telephone))
                .orElseThrow(() -> new AuthenticationException("Numéro de téléphone ou PIN incorrect"));

        if (!passwordEncoder.matches(pin, compte.getPin())) {
            throw new AuthenticationException("Numéro de téléphone ou PIN incorrect");
        }

        return compte;
    }

    private String normalizeTelephone(String telephone) {
        if (telephone == null) {
            return null;
        }
        return telephone.trim().replaceAll("\\s+", "");
    }
}