package sn.banque1.banque1_api.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import sn.banque1.banque1_api.model.TypeTransaction;

@Getter
@Setter
@AllArgsConstructor
public class TransactionResponse {
    private Long id;
    private Long montant;
    private TypeTransaction typeTransaction;
    private LocalDate dateTransaction;

}
