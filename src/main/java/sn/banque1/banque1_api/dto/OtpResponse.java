package sn.banque1.banque1_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OtpResponse {

    private String telephone;
    private boolean valide;
}