package sn.banque1.banque1_api.helper;

import java.util.List;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import sn.banque1.banque1_api.dto.CompteRequest;
import sn.banque1.banque1_api.dto.CompteResponse;
import sn.banque1.banque1_api.exception.BadRequestException;
import sn.banque1.banque1_api.mapper.CompteMapper;
import sn.banque1.banque1_api.model.Compte;
import sn.banque1.banque1_api.service.CompteService;

@Component
@RequiredArgsConstructor
public class CompteHelper {

    private final CompteService compteService;
    private final CompteMapper compteMapper;
    private final OtpHelper otpHelper;

    public CompteResponse creerCompte(CompteRequest compteRequest) {
        String telephone = normalizeTelephone(compteRequest.getTelephone());

        if (!otpHelper.hasValidOtp(telephone)) {
            throw new BadRequestException("Veuillez valider votre numéro via OTP");
        }

        compteService.findByTelephone(telephone).ifPresent(compte -> {
            throw new BadRequestException("Ce numéro de téléphone existe déjà");
        });

        Compte compte = compteMapper.toCompte(compteRequest);
        compte.setSolde(0);
        Compte c = compteService.save(compte);
        CompteResponse cr = compteMapper.toResponse(c);

        return cr;
    }

    public List<CompteResponse> listerComptes() {
        return compteService.findAllComptes().stream().map(compteMapper::toResponse).toList();
    }

    public CompteResponse trouverCompte(String telephone) {
        Compte compte = compteService.findByTelephone(telephone)
                .orElseThrow(() -> new sn.banque1.banque1_api.exception.ResourceNotFoundException(
                        "Compte introuvable avec le téléphone : " + telephone));
        return compteMapper.toResponse(compte);
    }

    private String normalizeTelephone(String telephone) {
        if (telephone == null) {
            return null;
        }
        return telephone.trim().replaceAll("\\s+", "");
    }
}