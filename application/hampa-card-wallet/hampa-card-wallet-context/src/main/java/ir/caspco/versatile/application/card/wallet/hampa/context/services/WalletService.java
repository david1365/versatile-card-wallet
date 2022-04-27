package ir.caspco.versatile.application.card.wallet.hampa.context.services;

import ir.caspco.versatile.application.card.wallet.hampa.context.domains.CardEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.domains.PersonEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.domains.WalletEntity;
import ir.caspco.versatile.jms.client.common.enums.CardType;

import java.util.Set;
import java.util.UUID;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

public interface WalletService {

    WalletEntity findByWalletIdAndStatusTrue(UUID walletId);

    WalletEntity findByWalletId(UUID walletId);

    WalletEntity findBySegmentCode(Set<WalletEntity> wallets, String segmentCode);

    WalletEntity findBySegmentCodeAndStatusTrue(Set<WalletEntity> wallets, String segmentCode);

    WalletEntity findByCardAndSegmentCodeAndStatusTrue(CardEntity card, String segmentCode);

    WalletEntity findByWalletId(Set<WalletEntity> walletEntities, UUID walletId);

    WalletEntity findByWalletIdAndStatusTrue(Set<WalletEntity> walletEntities, UUID walletId);

    WalletEntity findByCardNumberAndSegmentCodeAndStatusTrue(String cardNumber, String segmentCode);

    WalletEntity findByCardNumberAndSegmentCode(String cardNumber, String segmentCode);

    WalletEntity findByMobileNumberAndSegmentCodeAndStatusTrue(String mobileNumber, String segmentCode);

    WalletEntity findByMobileNumberAndSegmentCode(String mobileNumber, String segmentCode);

    WalletEntity findByAccountNumber(String accountNumber);

    WalletEntity findByAccountNumberAndStatusTrue(String accountNumber);

    WalletEntity findByLoanFileNumberAndStatusTrue(Long loanFileNumber);

    Set<WalletEntity> findByNationalCodeAndCardType(String nationalCode, CardType cardType);

    WalletEntity findByPersonAndSegmentCodeAndStatusTrue(PersonEntity person, String segmentCode);

    Set<WalletEntity> findByCardAndStatusTrue(CardEntity card);
}
