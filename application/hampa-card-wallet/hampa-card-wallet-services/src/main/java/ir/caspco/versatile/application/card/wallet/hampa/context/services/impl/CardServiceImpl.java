package ir.caspco.versatile.application.card.wallet.hampa.context.services.impl;

import ir.caspco.versatile.application.card.wallet.hampa.context.domains.CardEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.domains.PersonEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.exceptions.CardNotFoundException;
import ir.caspco.versatile.application.card.wallet.hampa.context.repositories.CardRepository;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.CardService;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.CardPhysicResultVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.CardPrintDataResultVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.EncryptedCardInfoResultVO;
import ir.caspco.versatile.common.util.Shift;
import ir.caspco.versatile.common.util.encryption.Cryptography;
import ir.caspco.versatile.jms.client.common.client.hampa.ActiveHampaCardClient;
import ir.caspco.versatile.jms.client.common.client.hampa.CardChangeStatusClient;
import ir.caspco.versatile.jms.client.common.client.hampa.HampaCardTrackClient;
import ir.caspco.versatile.jms.client.common.enums.CardType;
import ir.caspco.versatile.jms.client.common.exceptions.CoreContentResultException;
import ir.caspco.versatile.jms.client.common.msg.hampa.CardChangeStatusRequest;
import ir.caspco.versatile.jms.client.common.msg.hampa.CardChangeStatusResponse;
import ir.caspco.versatile.jms.client.common.vo.CardPhysicVO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */


//TODO from davood akbari: Do not forget to test.

@Service
@Transactional
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final CardChangeStatusClient cardChangeStatusClient;
    private final ActiveHampaCardClient activeHampaCardClient;
    private final HampaCardTrackClient cardTrackClient;


    private final Cryptography caspianRsa;
    private final Cryptography hampaRsa;


    public CardServiceImpl(CardRepository cardRepository,
                           CardChangeStatusClient cardChangeStatusClient,
                           ActiveHampaCardClient activeHampaCardClient,
                           HampaCardTrackClient cardTrackClient,

                           @Qualifier("caspian-rsa")
                                   Cryptography caspianRsa,

                           @Qualifier("hampa-rsa")
                                   Cryptography hampaRsa) {

        this.cardRepository = cardRepository;
        this.cardChangeStatusClient = cardChangeStatusClient;
        this.activeHampaCardClient = activeHampaCardClient;
        this.cardTrackClient = cardTrackClient;

        this.caspianRsa = caspianRsa;
        this.hampaRsa = hampaRsa;
    }

    @Override
    public CardEntity findById(BigDecimal id) {

        return cardRepository.findById(id)
                .orElseThrow(CardNotFoundException::new);
    }

    @Override
    public CardEntity findByCardNumber(String cardNumber) {

        return cardRepository.findByCardNumber(cardNumber)
                .orElseThrow(CardNotFoundException::new);
    }

    @Override
    public List<CardEntity> findByMobileNumber(String mobileNumber) {

        return cardRepository.findByPerson_MobileNumber(mobileNumber);
    }

    @Override
    public Boolean cardChangeStatus(CardChangeStatusRequest cardChangeStatusRequest) {

        CardChangeStatusResponse cardChangeStatusResponse = cardChangeStatusClient.send(cardChangeStatusRequest)
                .retrieve()
                .result()
                .orElseThrow(CoreContentResultException::new);


        return "ok".equalsIgnoreCase(cardChangeStatusResponse.getMessage());
    }

    @Override
    public CardEntity findByPersonAndCardType(PersonEntity person, CardType cardType) {

        return cardRepository.findByPersonAndCardType(person, cardType)
                .orElseThrow(CardNotFoundException::new);
    }

    @Override
    public CardEntity findCreditCardByPerson(PersonEntity person) {

        return findByPersonAndCardType(person, CardType.CREDIT);
    }

    @Override
    public CardEntity findDebitCardByPerson(PersonEntity person) {

        return findByPersonAndCardType(person, CardType.DEBIT);
    }

    @Override
    public Boolean activeHampaCard(CardChangeStatusRequest cardChangeStatusRequest) {

        CardChangeStatusResponse cardChangeStatusResponse = activeHampaCardClient.send(cardChangeStatusRequest)
                .retrieve()
                .result()
                .orElseThrow(CoreContentResultException::new);

        return "ok".equalsIgnoreCase(cardChangeStatusResponse.getMessage());
    }

    @Override
    public EncryptedCardInfoResultVO cardInfo(String cardNumber) {

        String jsonCardInfo = customJsonCardInfo(cardNumber);

        String jsonEncrypted = hampaRsa.encrypt(jsonCardInfo);
        String jsonSignature = caspianRsa.sign(jsonCardInfo);

        return EncryptedCardInfoResultVO.builder()
                .jsonEncrypted(jsonEncrypted)
                .jsonSignature(jsonSignature)
                .build();
    }

    public String customJsonCardInfo(String cardNumber) {

        CardPhysicVO cardPhysic = cardTrackClient.cardTrack(cardNumber);

        CardPhysicResultVO cardPhysicResult = CardPhysicResultVO.builder()
                .cardEmbossData(cardPhysic.getCardEmbossDataDto())
                .cardPrintData(Shift.just(cardPhysic.getCardPrintDataDto()).toShift(CardPrintDataResultVO.class).toObject())
                .build();

        return Shift.just(cardPhysicResult).toJson();
    }
}
