package sn.banque1.banque1_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.banque1.banque1_api.model.Compte;

@Repository
public interface CompteRepository extends JpaRepository<Compte, Long> {

    Optional<Compte> findByTelephone(String telephone);

    Optional<Compte> findByEmail(String email);

    @Query("SELECT c FROM Compte c WHERE REPLACE(REPLACE(c.telephone, ' ', ''), '+', '') = :normalizedPhone")
    Optional<Compte> findByTelephoneNormalized(@Param("normalizedPhone") String normalizedPhone);
}
