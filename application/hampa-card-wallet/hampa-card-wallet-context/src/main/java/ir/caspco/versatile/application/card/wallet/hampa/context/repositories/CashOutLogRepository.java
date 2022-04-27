package ir.caspco.versatile.application.card.wallet.hampa.context.repositories;

import ir.caspco.versatile.application.card.wallet.hampa.context.domains.CashOutLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
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
public interface CashOutLogRepository extends JpaRepository<CashOutLogEntity, BigDecimal> {
    Optional<CashOutLogEntity> findByDealReference(String dealReference);
}
