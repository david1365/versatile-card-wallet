package ir.caspco.versatile.application.card.wallet.hampa.context.repositories;

import ir.caspco.versatile.application.card.wallet.hampa.context.domains.CardEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.domains.PersonEntity;
import ir.caspco.versatile.jms.client.common.enums.CardType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Repository
public interface CardRepository extends CrudRepository<CardEntity, BigDecimal> {

    Optional<CardEntity> findByCardNumber(String cardNumber);

    List<CardEntity> findByPerson_MobileNumber(String mobileNumber);

    Optional<CardEntity> findByPersonAndCardType(PersonEntity person, CardType cardType);
}
