package sn.banque1.banque1_api.helper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sn.banque1.banque1_api.dto.PaiementExterneRequest;
import sn.banque1.banque1_api.dto.TransactionRequest;
import sn.banque1.banque1_api.dto.TransactionResponse;
import sn.banque1.banque1_api.dto.WalletPaymentResponse;
import sn.banque1.banque1_api.dto.WalletPaymentStatusResponse;
import sn.banque1.banque1_api.exception.AuthenticationException;
import sn.banque1.banque1_api.exception.BadRequestException;
import sn.banque1.banque1_api.exception.ResourceNotFoundException;
import sn.banque1.banque1_api.mapper.TransactionMapper;
import sn.banque1.banque1_api.model.Compte;
import sn.banque1.banque1_api.model.Transaction;
import sn.banque1.banque1_api.model.TypeTransaction;
import sn.banque1.banque1_api.service.CompteService;
import sn.banque1.banque1_api.service.TransactionService;

/**
 * Helper pour les opérations sur les transactions bancaires.
 *
 * <p>La méthode {@link #payerAvecPin(PaiementExterneRequest)} est appelée par un
 * service tiers (gestion_service_api) pour payer une prestation. Elle :
 * <ul>
 *   <li>Vérifie le téléphone + PIN (pas de session JWT)</li>
 *   <li>Vérifie l'idempotence (évite les doubles débits)</li>
 *   <li>Débite le compte et crée la transaction dans une seule transaction JPA</li>
 *   <li>Retourne un {@link WalletPaymentResponse}</li>
 * </ul>
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class TransactionHelper {
    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;
    private final CompteService compteService;
    private final CompteHelper compteHelper;

    public TransactionResponse creerDepot(String telephone, TransactionRequest request) {

        Compte compte = trouverCompte(telephone);

        effectuerDepot(compte, request.getMontant());

        Transaction transaction = creerTransaction(
                compte,
                request.getMontant(),
                TypeTransaction.DEPOT);

        return transactionMapper.toResponse(transaction);
    }

    public TransactionResponse creerRetrait(String telephone, TransactionRequest request) {

        Compte compte = trouverCompte(telephone);

        effectuerRetrait(compte, request.getMontant());

        Transaction transaction = creerTransaction(
                compte,
                request.getMontant(),
                TypeTransaction.RETRAIT);

        return transactionMapper.toResponse(transaction);
    }

    public TransactionResponse creerPaiement(String telephone, TransactionRequest request) {

        Compte compte = trouverCompte(telephone);

        effectuerPaiement(compte, request.getMontant());

        Transaction transaction = creerTransaction(
                compte,
                request.getMontant(),
                TypeTransaction.PAIEMENT);

        return transactionMapper.toResponse(transaction);
    }

    /**
     * Paiement d'une prestation par un service tiers (gestion_service_api) : le
     * client n'a pas de session JWT ici, l'identité est prouvée par téléphone+PIN.
     *
     * <p>Vérification du PIN et débit dans la même transaction JPA, pour ne jamais
     * débiter un compte dont le PIN n'a pas été confirmé au même instant.</p>
     *
     * <p>Idempotence : si une transaction avec la même {@code idempotencyKey}
     * existe déjà, elle est retournée sans être ré-executée (évite les doubles
     * débits en cas de timeout ou de réessai).</p>
     */
    @Transactional
    public WalletPaymentResponse payerAvecPin(PaiementExterneRequest request) {

        // --- 1. Idempotence : on vérifie d'abord si cette clé a déjà été utilisée ---
        if (request.getIdempotencyKey() != null) {
            Optional<Transaction> existing = transactionService.findByIdempotencyKey(request.getIdempotencyKey());
            if (existing.isPresent()) {
                Transaction transaction = existing.get();
                log.info("Paiement déjà traité (idempotence) : idempotencyKey={}, transactionId={}",
                        request.getIdempotencyKey(), transaction.getId());
                return WalletPaymentResponse.builder()
                        .status("SUCCESS")
                        .transactionReference(String.valueOf(transaction.getId()))
                        .idempotencyKey(transaction.getIdempotencyKey())
                        .failureReason(null)
                        .build();
            }
        }

        // --- 2. Vérification du PIN (throw si invalide, avant tout débit) ---
        try {
            Compte compte = compteHelper.verifierPin(request.getTelephone(), request.getPin());

            // --- 3. Débit du compte ---
            Long montant = request.getAmount().longValue();
            effectuerPaiement(compte, montant);

            // --- 4. Création de la transaction (avec idempotencyKey) ---
            Transaction transaction = creerTransaction(compte, montant, TypeTransaction.PAIEMENT);
            transaction.setIdempotencyKey(request.getIdempotencyKey());
            transaction = transactionService.save(transaction);

            log.info("Paiement externe créé : transactionId={}, idempotencyKey={}",
                    transaction.getId(), request.getIdempotencyKey());

            return WalletPaymentResponse.builder()
                    .status("SUCCESS")
                    .transactionReference(String.valueOf(transaction.getId()))
                    .idempotencyKey(transaction.getIdempotencyKey())
                    .failureReason(null)
                    .build();

        } catch (AuthenticationException e) {
            log.warn("Paiement échoué (PIN incorrect) : telephone={}, raison={}",
                    request.getTelephone(), e.getMessage());
            return WalletPaymentResponse.builder()
                    .status("FAILED")
                    .transactionReference(null)
                    .idempotencyKey(request.getIdempotencyKey())
                    .failureReason("Numéro de téléphone ou PIN incorrect.")
                    .build();
        } catch (ResourceNotFoundException e) {
            log.warn("Paiement échoué (compte introuvable) : telephone={}, raison={}",
                    request.getTelephone(), e.getMessage());
            return WalletPaymentResponse.builder()
                    .status("FAILED")
                    .transactionReference(null)
                    .idempotencyKey(request.getIdempotencyKey())
                    .failureReason("Aucun compte bancaire associé à ce numéro de téléphone.")
                    .build();
        } catch (BadRequestException e) {
            log.warn("Paiement échoué (solde insuffisant) : telephone={}, raison={}",
                    request.getTelephone(), e.getMessage());
            return WalletPaymentResponse.builder()
                    .status("FAILED")
                    .transactionReference(null)
                    .idempotencyKey(request.getIdempotencyKey())
                    .failureReason("Solde insuffisant pour effectuer ce paiement.")
                    .build();
        }
    }

    /**
     * Récupère le statut d'un paiement externe par sa clé d'idempotence.
     * Utilisé par gestion_service_api pour synchroniser le statut en cas de timeout.
     */
    public WalletPaymentStatusResponse getPaymentStatusByIdempotencyKey(UUID idempotencyKey) {
        Optional<Transaction> transactionOpt = transactionService.findByIdempotencyKey(idempotencyKey);

        if (transactionOpt.isEmpty()) {
            return WalletPaymentStatusResponse.builder()
                    .idempotencyKey(idempotencyKey)
                    .status("NOT_FOUND")
                    .build();
        }

        Transaction transaction = transactionOpt.get();
        return WalletPaymentStatusResponse.builder()
                .idempotencyKey(transaction.getIdempotencyKey())
                .status("SUCCESS")
                .transactionReference(String.valueOf(transaction.getId()))
                .failureReason(null)
                .build();
    }

    public List<TransactionResponse> listerTransactions(String telephone) {

        trouverCompte(telephone);

        return transactionService.findAllTransactions(telephone).stream().map(transactionMapper::toResponse).toList();
    }

    private Compte trouverCompte(String telephone) {

        return compteService
                .findByTelephone(telephone)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Compte introuvable avec le numéro de téléphone : " + telephone));
    }

    private void effectuerRetrait(Compte compte, Long montant) {

        if (compte.getSolde() < montant) {
            throw new BadRequestException(
                    "Solde insuffisant pour effectuer ce retrait.");
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
                    "Solde insuffisant pour effectuer ce paiement.");
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