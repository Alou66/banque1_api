package sn.banque1.banque1_api.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.banque1.banque1_api.model.Transaction;

/**
 * Repository JPA pour l'entité Transaction.
 *
 * <p>La méthode {@link #findByIdempotencyKey(UUID)} permet de détecter les doubles
 * appels vers l'endpoint de paiement externe : si une transaction avec la même
 * clé d'idempotence existe déjà, le paiement est retourné sans être ré-executé.</p>
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllByCompteTelephone(String telephone);

    Optional<Transaction> findByIdempotencyKey(UUID idempotencyKey);
}
