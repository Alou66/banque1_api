package sn.banque1.banque1_api.mapper;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import sn.banque1.banque1_api.dto.CompteRequest;
import sn.banque1.banque1_api.dto.CompteResponse;
import sn.banque1.banque1_api.model.Compte;

@Component
public class CompteMapper {

    private final PasswordEncoder passwordEncoder;

    public CompteMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public Compte toCompte(CompteRequest request) {
        Compte compte = new Compte();
        compte.setPrenom(request.getPrenom());
        compte.setNom(request.getNom());
        compte.setAdresse(request.getAdresse());
        compte.setNumPiece(request.getNumPiece());
        compte.setTelephone(normalizeTelephone(request.getTelephone()));
        compte.setPin(passwordEncoder.encode(request.getPin()));
        return compte;
    }

    public CompteResponse toResponse(Compte compte) {
        return new CompteResponse(
                compte.getId(),
                compte.getSolde(),
                compte.getDateCreation(),
                compte.getNumPiece(),
                compte.getPrenom(),
                compte.getNom(),
                compte.getAdresse(),
                compte.getTelephone());
    }

    private String normalizeTelephone(String telephone) {
        if (telephone == null) {
            return null;
        }
        return telephone.trim().replaceAll("\\s+", "");
    }
}
