package sn.banque1.banque1_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import sn.banque1.banque1_api.dto.ApiResponse;
import sn.banque1.banque1_api.dto.LoginRequest;
import sn.banque1.banque1_api.dto.LoginResponse;
import sn.banque1.banque1_api.dto.SendOtpRequest;
import sn.banque1.banque1_api.dto.VerifyOtpRequest;
import sn.banque1.banque1_api.dto.OtpResponse;
import sn.banque1.banque1_api.helper.OtpHelper;
import sn.banque1.banque1_api.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final OtpHelper otpHelper;

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/send-otp")
    public ResponseEntity<ApiResponse<OtpResponse>> sendOtp(@Valid @RequestBody SendOtpRequest request) {
        OtpResponse response = otpHelper.sendOtp(request);
        return ResponseEntity.ok(new ApiResponse<>("OTP envoyé avec succès", response));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<OtpResponse>> verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
        OtpResponse response = otpHelper.verifyOtp(request);
        return ResponseEntity.ok(new ApiResponse<>("OTP validé avec succès", response));
    }
}