package ir.caspco.versatile.application.card.wallet.hampa.context.repositories;

import ir.caspco.versatile.application.card.wallet.hampa.context.domains.CardEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.domains.PersonEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.domains.WalletEntity;
import ir.caspco.versatile.jms.client.common.enums.CardType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Repository
public interface WalletRepository extends CrudRepository<WalletEntity, UUID> {

    Optional<WalletEntity> findByCard_CardNumberAndSegmentCode(String cardNumber, String segmentCode);

    Optional<WalletEntity> findByCardAndSegmentCodeAndStatusTrue(CardEntity card, String segmentCode);

    Optional<WalletEntity> findByCard_CardNumberAndSegmentCodeAndStatusTrue(String cardNumber, String segmentCode);

    Optional<WalletEntity> findByWalletIdAndStatusTrue(UUID walletId);

    Optional<WalletEntity> findByCard_Person_MobileNumberAndSegmentCode(String mobileNumber, String segmentCode);

    Optional<WalletEntity> findByCard_Person_MobileNumberAndSegmentCodeAndStatusTrue(String mobileNumber, String segmentCode);

    Optional<WalletEntity> findByAccountNumber(String accountNumber);

    Optional<WalletEntity> findByAccountNumberAndStatusTrue(String accountNumber);

    Optional<WalletEntity> findByLoanFileNumberAndStatusTrue(Long loanFileNumber);

    boolean existsByCard_Person_NationalCodeAndSegmentCodeIn(String nationalCode, List<String> segmentCodes);

    Set<WalletEntity> findByCard_Person_NationalCodeAndCard_CardType(String nationalCode, CardType cardType);

    Optional<WalletEntity> findByCard_PersonAndSegmentCodeAndStatusTrue(PersonEntity person, String segmentCode);

    Set<WalletEntity> findByCardAndStatusTrue(CardEntity card);
}
