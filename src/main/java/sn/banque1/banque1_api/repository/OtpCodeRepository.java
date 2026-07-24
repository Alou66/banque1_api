package sn.banque1.banque1_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sn.banque1.banque1_api.model.OtpCode;

import java.util.Optional;

@Repository
public interface OtpCodeRepository extends JpaRepository<OtpCode, Long> {

    Optional<OtpCode> findByTelephoneAndCodeAndValideAndUtiliseFalse(String telephone, String code, boolean valide);

    Optional<OtpCode> findByTelephoneAndValideTrueAndUtiliseTrue(String telephone);

    @Modifying
    @Query("DELETE FROM OtpCode o WHERE o.telephone = :telephone")
    void deleteByTelephone(String telephone);
}