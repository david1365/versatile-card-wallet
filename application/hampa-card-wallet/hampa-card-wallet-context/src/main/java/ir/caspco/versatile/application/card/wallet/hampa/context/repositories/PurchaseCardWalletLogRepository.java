package ir.caspco.versatile.application.card.wallet.hampa.context.repositories;

import ir.caspco.versatile.application.card.wallet.hampa.context.domains.PurchaseCardWalletLogEntity;
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
public interface PurchaseCardWalletLogRepository extends JpaRepository<PurchaseCardWalletLogEntity, BigDecimal> {

    @Query("SELECT pl.merchant.merchantId AS merchantId, SUM(pl.amount) AS total " +
            "FROM PurchaseCardWalletLogEntity AS pl " +
            "WHERE pl.flowStatus = 'DONE' " +
            "AND CURRENT_DATE >= (SELECT s.nextValidTime FROM SettlementNextValidTimeEntity s " +
            "WHERE s.merchant.merchantId = pl.merchant.merchantId AND s.settlementType = 'Purchase' )" +
            "AND pl.merchant.active = true " +
            "GROUP BY pl.merchant.merchantId")
    List<TotalAmountsModel> totalAmounts(Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE PurchaseCardWalletLogEntity pl " +
            "SET pl.flowStatus = 'CLEARED', " +
            "pl.clearingLog.id = :clearingLogId " +
            "WHERE pl.merchant.merchantId = :merchantId " +
            "AND pl.flowStatus = 'DONE' " +
            "AND pl.createdDate <= :selectedDate")
    int clearedLog(@Param("merchantId") UUID merchantId,
                   @Param("clearingLogId") BigDecimal clearingLogId,
                   @Param("selectedDate") Date selectedDate);

    Optional<PurchaseCardWalletLogEntity> findByDealReference(String dealReference);

    Optional<PurchaseCardWalletLogEntity> findByCreditDealReference(String dealReference);
}
