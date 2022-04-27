package ir.caspco.versatile.application.card.wallet.hampa.context.repositories;

import ir.caspco.versatile.application.card.wallet.hampa.context.domains.PersonEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Repository
public interface PersonRepository extends CrudRepository<PersonEntity, BigDecimal> {

    Optional<PersonEntity> findByNationalCode(String nationalCode);

    Optional<PersonEntity> findByMobileNumber(String mobileNumber);
}