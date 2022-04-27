package ir.caspco.versatile.application.card.wallet.hampa.context.services.impl;

import ir.caspco.versatile.application.card.wallet.hampa.context.domains.CardEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.domains.PersonEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.domains.WalletEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.exceptions.WalletInactiveException;
import ir.caspco.versatile.application.card.wallet.hampa.context.exceptions.WalletNotFoundException;
import ir.caspco.versatile.application.card.wallet.hampa.context.repositories.WalletRepository;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.WalletService;
import ir.caspco.versatile.jms.client.common.enums.CardType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Service
@Transactional
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;

    public WalletServiceImpl(WalletRepository walletRepository) {

        this.walletRepository = walletRepository;
    }

    @Override
    public WalletEntity findByWalletIdAndStatusTrue(UUID walletId) {

        return walletRepository.findByWalletIdAndStatusTrue(walletId)
                .orElseThrow(WalletInactiveException::new);
    }

    @Override
    public WalletEntity findByWalletId(UUID walletId) {

        return walletRepository.findById(walletId)
                .orElseThrow(WalletNotFoundException::new);
    }

    @Override
    public WalletEntity findBySegmentCode(Set<WalletEntity> wallets, String segmentCode) {

        return wallets.stream()
                .filter(wallet -> filterBySegmentCode(segmentCode, wallet))
                .findAny()
                .orElseThrow(WalletNotFoundException::new);
    }

    @Override
    public WalletEntity findBySegmentCodeAndStatusTrue(Set<WalletEntity> wallets, String segmentCode) {

        return wallets.stream()
                .filter(wallet -> filterBySegmentCode(segmentCode, wallet))
                .filter(WalletEntity::getStatus)
                .findAny()
                .orElseThrow(WalletInactiveException::new);
    }

    @Override
    public WalletEntity findByWalletId(Set<WalletEntity> walletEntities, UUID walletId) {

        return walletEntities.stream()
                .filter(walletFound -> filterByWalletId(walletId, walletFound))
                .findFirst()
                .orElseThrow(WalletNotFoundException::new);
    }

    @Override
    public WalletEntity findByWalletIdAndStatusTrue(Set<WalletEntity> walletEntities, UUID walletId) {

        return walletEntities.stream()
                .filter(walletFound -> filterByWalletId(walletId, walletFound))
                .filter(WalletEntity::getStatus)
                .findFirst()
                .orElseThrow(WalletInactiveException::new);
    }

    @Override
    public WalletEntity findByCardNumberAndSegmentCodeAndStatusTrue(String cardNumber, String segmentCode) {

        return walletRepository.findByCard_CardNumberAndSegmentCodeAndStatusTrue(cardNumber, segmentCode)
                .orElseThrow(WalletInactiveException::new);
    }

    @Override
    public WalletEntity findByCardNumberAndSegmentCode(String cardNumber, String segmentCode) {

        return walletRepository.findByCard_CardNumberAndSegmentCode(cardNumber, segmentCode)
                .orElseThrow(WalletNotFoundException::new);
    }

    @Override
    public WalletEntity findByMobileNumberAndSegmentCodeAndStatusTrue(String mobileNumber, String segmentCode) {

        return walletRepository.findByCard_Person_MobileNumberAndSegmentCodeAndStatusTrue(mobileNumber, segmentCode)
                .orElseThrow(WalletInactiveException::new);
    }

    @Override
    public WalletEntity findByMobileNumberAndSegmentCode(String mobileNumber, String segmentCode) {

        return walletRepository.findByCard_Person_MobileNumberAndSegmentCode(mobileNumber, segmentCode)
                .orElseThrow(WalletNotFoundException::new);
    }

    @Override
    public WalletEntity findByAccountNumber(String accountNumber) {

        return walletRepository.findByAccountNumber(accountNumber)
                .orElseThrow(WalletNotFoundException::new);
    }

    @Override
    public WalletEntity findByAccountNumberAndStatusTrue(String accountNumber) {

        return walletRepository.findByAccountNumberAndStatusTrue(accountNumber)
                .orElseThrow(WalletInactiveException::new);
    }

    @Override
    public WalletEntity findByLoanFileNumberAndStatusTrue(Long loanFileNumber) {

        return walletRepository.findByLoanFileNumberAndStatusTrue(loanFileNumber)
                .orElseThrow(WalletInactiveException::new);
    }

    @Override
    public Set<WalletEntity> findByNationalCodeAndCardType(String nationalCode, CardType cardType) {

        return walletRepository.findByCard_Person_NationalCodeAndCard_CardType(nationalCode, cardType);
    }

    @Override
    public WalletEntity findByPersonAndSegmentCodeAndStatusTrue(PersonEntity person, String segmentCode) {

        return walletRepository.findByCard_PersonAndSegmentCodeAndStatusTrue(person, segmentCode)
                .orElseThrow(WalletInactiveException::new);
    }

    @Override
    public WalletEntity findByCardAndSegmentCodeAndStatusTrue(CardEntity card, String segmentCode) {

        return walletRepository.findByCardAndSegmentCodeAndStatusTrue(card, segmentCode)
                .orElseThrow(WalletInactiveException::new);
    }

    @Override
    public Set<WalletEntity> findByCardAndStatusTrue(CardEntity card) {

        return walletRepository.findByCardAndStatusTrue(card);
    }

    private boolean filterBySegmentCode(String segmentCode, WalletEntity wallet) {

        return segmentCode == null ? wallet.getSegmentCode() == null : segmentCode.equalsIgnoreCase(wallet.getSegmentCode());
    }

    private boolean filterByWalletId(UUID walletId, WalletEntity wallet) {

        return wallet.getWalletId().equals(walletId);
    }
}
