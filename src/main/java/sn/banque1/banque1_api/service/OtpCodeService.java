package sn.banque1.banque1_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.banque1.banque1_api.exception.BadRequestException;
import sn.banque1.banque1_api.model.OtpCode;
import sn.banque1.banque1_api.repository.OtpCodeRepository;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
public class OtpCodeService {

    private final OtpCodeRepository otpCodeRepository;
    private final Random random = new Random();

    public OtpCode generateAndSaveOtp(String telephone) {
        otpCodeRepository.deleteByTelephone(telephone);

        String code = String.format("%06d", random.nextInt(1000000));
        LocalDateTime expirationDate = LocalDateTime.now().plusMinutes(5);

        OtpCode otpCode = OtpCode.builder()
                .telephone(telephone)
                .code(code)
                .expirationDate(expirationDate)
                .valide(true)
                .utilise(false)
                .build();

        System.out.println("OTP généré pour " + telephone + " : " + code);

        return otpCodeRepository.save(otpCode);
    }

    public OtpCode verifyOtp(String telephone, String code) {
        OtpCode otpCode = otpCodeRepository.findByTelephoneAndCodeAndValideAndUtiliseFalse(telephone, code, true)
                .orElseThrow(() -> new BadRequestException("OTP invalide ou déjà utilisé"));

        if (otpCode.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("OTP expiré");
        }

        otpCode.setValide(true);
        otpCode.setUtilise(true);

        return otpCodeRepository.save(otpCode);
    }

    public boolean hasValidOtp(String telephone) {
        return otpCodeRepository.findByTelephoneAndValideTrueAndUtiliseTrue(telephone).isPresent();
    }
}