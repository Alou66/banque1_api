package sn.banque1.banque1_api.service;

import java.util.List;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import sn.banque1.banque1_api.model.Transaction;
import sn.banque1.banque1_api.repository.TransactionRepository;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public List<Transaction> findAllTransactions(String telephone) {
        return transactionRepository.findAllByCompteTelephone(telephone);
    }
}
