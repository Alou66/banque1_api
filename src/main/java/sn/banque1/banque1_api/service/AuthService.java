package sn.banque1.banque1_api.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import sn.banque1.banque1_api.dto.LoginRequest;
import sn.banque1.banque1_api.dto.LoginResponse;
import sn.banque1.banque1_api.exception.AuthenticationException;
import sn.banque1.banque1_api.model.Compte;
import sn.banque1.banque1_api.repository.CompteRepository;
import sn.banque1.banque1_api.security.JwtService;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final CompteRepository compteRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest request) {
        String telephone = normalizeTelephone(request.getTelephone());

        Compte compte = compteRepository.findByTelephone(telephone)
                .orElseGet(() -> compteRepository.findByTelephoneNormalized(telephone)
                        .orElseThrow(() -> new AuthenticationException("Identifiants invalides")));

        if (!passwordEncoder.matches(request.getPin(), compte.getPin())) {
            throw new AuthenticationException("Identifiants invalides");
        }

        String token = jwtService.generateToken(compte.getTelephone());
        return new LoginResponse(token);
    }

    private String normalizeTelephone(String telephone) {
        if (telephone == null) {
            return null;
        }
        return telephone.trim().replaceAll("\\s+", "").replaceAll("^\\+", "");
    }
}