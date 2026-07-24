package sn.banque1.banque1_api.mapper;

import org.springframework.stereotype.Component;
import sn.banque1.banque1_api.dto.TransactionRequest;
import sn.banque1.banque1_api.dto.TransactionResponse;
import sn.banque1.banque1_api.model.Transaction;

@Component
public class TransactionMapper {

    public Transaction toTransaction(TransactionRequest request) {
        Transaction transaction = new Transaction();
        transaction.setMontant(request.getMontant());

        return transaction;
    }

    public TransactionResponse toResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getMontant(),
                transaction.getTypeTransaction(),
                transaction.getDateTransaction());
    }
}
