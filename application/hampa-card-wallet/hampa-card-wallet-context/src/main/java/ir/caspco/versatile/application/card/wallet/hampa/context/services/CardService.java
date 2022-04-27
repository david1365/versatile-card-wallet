package ir.caspco.versatile.application.card.wallet.hampa.context.services;

import ir.caspco.versatile.application.card.wallet.hampa.context.domains.CardEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.domains.PersonEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.EncryptedCardInfoResultVO;
import ir.caspco.versatile.jms.client.common.enums.CardType;
import ir.caspco.versatile.jms.client.common.msg.hampa.CardChangeStatusRequest;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

public interface CardService {

    CardEntity findById(BigDecimal id);

    CardEntity findByCardNumber(String cardNumber);

    List<CardEntity> findByMobileNumber(String mobileNumber);

    Boolean cardChangeStatus(CardChangeStatusRequest cardChangeStatusRequest);

    CardEntity findByPersonAndCardType(PersonEntity person, CardType cardType);

    CardEntity findCreditCardByPerson(PersonEntity person);

    CardEntity findDebitCardByPerson(PersonEntity person);

    Boolean activeHampaCard(CardChangeStatusRequest cardChangeStatusRequest);

    EncryptedCardInfoResultVO cardInfo(String cardNumber);
}
