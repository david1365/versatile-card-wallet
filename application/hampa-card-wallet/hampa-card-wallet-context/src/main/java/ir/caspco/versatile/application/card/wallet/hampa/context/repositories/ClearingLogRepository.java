package ir.caspco.versatile.application.card.wallet.hampa.context.repositories;

import ir.caspco.versatile.application.card.wallet.hampa.context.domains.ClearingLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Repository
public interface ClearingLogRepository extends JpaRepository<ClearingLogEntity, BigDecimal> {

}
