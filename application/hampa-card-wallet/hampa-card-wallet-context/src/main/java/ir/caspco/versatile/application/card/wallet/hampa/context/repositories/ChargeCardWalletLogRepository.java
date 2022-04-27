package ir.caspco.versatile.application.card.wallet.hampa.context.repositories;

import ir.caspco.versatile.application.card.wallet.hampa.context.domains.ChargeCardWalletLogEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.repositories.model.TotalAmountsModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

//TODO from davood akbari: Do not forget to test.Do something to make the code duplicate
@Repository
public interface ChargeCardWalletLogRepository extends JpaRepository<ChargeCardWalletLogEntity, BigDecimal> {

    @Query("SELECT cl.merchant.merchantId AS merchantId, SUM(cl.amount) AS total " +
            "FROM ChargeCardWalletLogEntity AS cl " +
            "WHERE cl.flowStatus = 'DONE' " +
            "AND CURRENT_DATE >= (SELECT s.nextValidTime FROM SettlementNextValidTimeEntity s " +
            "WHERE s.merchant.merchantId = cl.merchant.merchantId AND s.settlementType = 'Charge' )" +
            "AND cl.merchant.active = true " +
            "GROUP BY cl.merchant.merchantId ")
    List<TotalAmountsModel> totalAmounts(Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE ChargeCardWalletLogEntity cl " +
            "SET cl.flowStatus = 'CLEARED', " +
            "cl.clearingLog.id = :clearingLogId " +
            "WHERE cl.merchant.merchantId = :merchantId " +
            "AND cl.flowStatus = 'DONE' " +
            "AND cl.createdDate <= :selectedDate")
    int clearedLog(@Param("merchantId") UUID merchantId,
                   @Param("clearingLogId") BigDecimal clearingLogId,
                   @Param("selectedDate") Date selectedDate);

    Optional<ChargeCardWalletLogEntity> findByDealReference(String dealReference);
}
