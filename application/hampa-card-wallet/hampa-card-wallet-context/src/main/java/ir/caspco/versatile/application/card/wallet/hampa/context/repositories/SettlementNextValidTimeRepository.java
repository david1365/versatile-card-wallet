package ir.caspco.versatile.application.card.wallet.hampa.context.repositories;

import ir.caspco.versatile.application.card.wallet.hampa.context.domains.SettlementNextValidTimeEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.enums.SettlementType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
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
public interface SettlementNextValidTimeRepository extends JpaRepository<SettlementNextValidTimeEntity, BigDecimal> {

    Optional<SettlementNextValidTimeEntity> findAllByMerchant_MerchantIdAndSettlementType(UUID merchantId, SettlementType settlementType);

    Set<SettlementNextValidTimeEntity> findAllByMerchant_MerchantId(UUID merchantId);
}
