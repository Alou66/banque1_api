package sn.banque1.banque1_api.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sn.banque1.banque1_api.dto.SendOtpRequest;
import sn.banque1.banque1_api.dto.VerifyOtpRequest;
import sn.banque1.banque1_api.dto.OtpResponse;
import sn.banque1.banque1_api.service.OtpCodeService;

@Component
@RequiredArgsConstructor
public class OtpHelper {

    private final OtpCodeService otpCodeService;

    public OtpResponse sendOtp(SendOtpRequest request) {
        String telephone = normalizeTelephone(request.getTelephone());
        otpCodeService.generateAndSaveOtp(telephone);
        return new OtpResponse(telephone, true);
    }

    public OtpResponse verifyOtp(VerifyOtpRequest request) {
        String telephone = normalizeTelephone(request.getTelephone());
        otpCodeService.verifyOtp(telephone, request.getOtp());
        return new OtpResponse(telephone, true);
    }

    public boolean hasValidOtp(String telephone) {
        return otpCodeService.hasValidOtp(normalizeTelephone(telephone));
    }

    private String normalizeTelephone(String telephone) {
        if (telephone == null) {
            return null;
        }
        return telephone.trim().replaceAll("\\s+", "");
    }
}