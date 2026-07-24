package sn.banque1.banque1_api.helper;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import sn.banque1.banque1_api.dto.TransactionRequest;
import sn.banque1.banque1_api.dto.TransactionResponse;
import sn.banque1.banque1_api.exception.BadRequestException;
import sn.banque1.banque1_api.exception.ResourceNotFoundException;
import sn.banque1.banque1_api.mapper.TransactionMapper;
import sn.banque1.banque1_api.model.Compte;
import sn.banque1.banque1_api.model.Transaction;
import sn.banque1.banque1_api.model.TypeTransaction;
import sn.banque1.banque1_api.service.CompteService;
import sn.banque1.banque1_api.service.TransactionService;

@RequiredArgsConstructor
@Component
public class TransactionHelper {
    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;
    private final CompteService compteService;

    public TransactionResponse creerDepot(TransactionRequest request) {

        Compte compte = trouverCompte(request.getTelephone());

        effectuerDepot(compte, request.getMontant());

        Transaction transaction = creerTransaction(
                compte,
                request.getMontant(),
                TypeTransaction.DEPOT);

        return transactionMapper.toResponse(transaction);
    }

    public TransactionResponse creerRetrait(TransactionRequest request) {

        Compte compte = trouverCompte(request.getTelephone());

        effectuerRetrait(compte, request.getMontant());

        Transaction transaction = creerTransaction(
                compte,
                request.getMontant(),
                TypeTransaction.RETRAIT);

        return transactionMapper.toResponse(transaction);
    }

    public TransactionResponse creerPaiement(TransactionRequest request) {

        Compte compte = trouverCompte(request.getTelephone());

        effectuerPaiement(compte, request.getMontant());

        Transaction transaction = creerTransaction(
                compte,
                request.getMontant(),
                TypeTransaction.PAIEMENT);

        return transactionMapper.toResponse(transaction);
    }

    public List<TransactionResponse> listerTransactions(String telephone) {

        trouverCompte(telephone);

        return transactionService.findAllTransactions(telephone).stream().map(transactionMapper::toResponse).toList();
    }

    private Compte trouverCompte(String telephone) {

        return compteService
                .findByTelephone(telephone)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Compte introuvable avec ce téléphone"));
    }

    // public CompteResponse trouverCompte(String telephone) {
    // Compte compte = compteService.findByTelephone(telephone).orElseThrow(() ->
    // new
    // ResourceNotFoundException(
    // "Compte introuvable avec le téléphone : " + telephone));
    // return compteMapper.toResponse(compte);
    // }

    private void effectuerRetrait(Compte compte, Long montant) {

        if (compte.getSolde() < montant) {
            throw new BadRequestException(
                    "Solde insuffisant");
        }

        compte.setSolde(
                compte.getSolde() - montant);

        compteService.save(compte);
    }

    private void effectuerDepot(Compte compte, Long montant) {

        compte.setSolde(compte.getSolde() + montant);

        compteService.save(compte);
    }

    private void effectuerPaiement(Compte compte, Long montant) {

        if (compte.getSolde() < montant) {
            throw new BadRequestException(
                    "Solde insuffisant");
        }

        compte.setSolde(
                compte.getSolde() - montant);

        compteService.save(compte);
    }

    private Transaction creerTransaction(Compte compte, Long montant, TypeTransaction type) {

        Transaction transaction = Transaction.builder()
                .montant(montant)
                .typeTransaction(type)
                .compte(compte)
                .build();

        return transactionService.save(transaction);
    }

}
