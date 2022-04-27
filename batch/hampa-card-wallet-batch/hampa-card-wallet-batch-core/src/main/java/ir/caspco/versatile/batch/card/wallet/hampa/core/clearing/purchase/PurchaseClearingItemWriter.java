package ir.caspco.versatile.batch.card.wallet.hampa.core.clearing.purchase;

import ir.caspco.versatile.application.card.wallet.hampa.context.domains.ClearingLogEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.domains.MerchantEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.enums.SettlementType;
import ir.caspco.versatile.application.card.wallet.hampa.context.repositories.PurchaseCardWalletLogRepository;
import ir.caspco.versatile.batch.card.wallet.hampa.core.clearing.ClearingUtil;
import ir.caspco.versatile.batch.card.wallet.hampa.core.clearing.model.ClearingWithMerchantModel;
import ir.caspco.versatile.jms.client.common.enums.hampa.HampaSettlementType;
import ir.caspco.versatile.jms.client.common.vo.hampa.MerchantSettlementHampaCardResponseVO;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Component
@StepScope
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class PurchaseClearingItemWriter implements ItemWriter<ClearingWithMerchantModel> {

    private final ClearingUtil clearingUtil;

    private final PurchaseCardWalletLogRepository purchaseCardWalletLogRepository;

    public PurchaseClearingItemWriter(ClearingUtil clearingUtil,

                                      PurchaseCardWalletLogRepository purchaseCardWalletLogRepository) {

        this.clearingUtil = clearingUtil;

        this.purchaseCardWalletLogRepository = purchaseCardWalletLogRepository;
    }

    @Override
    public void write(List<? extends ClearingWithMerchantModel> list) {

        list.forEach(clearingJobModel ->

                clearingJobModel.getTotalAmountModels().forEach(totalAmounts -> {

                    MerchantEntity merchant = totalAmounts.getMerchant();

                    if (isTimeToSettle(merchant)) {

                        //TODO from davood akbari: Think about the rollback
                        MerchantSettlementHampaCardResponseVO merchantSettlement = clearingUtil.clearingPurchase(totalAmounts, HampaSettlementType.BUY);

                        ClearingLogEntity clearingLog = clearingUtil.logClearing(
                                merchantSettlement,
                                merchant,
                                HampaSettlementType.BUY
                        );

                        purchaseCardWalletLogRepository.clearedLog(
                                merchant.getMerchantId(),
                                clearingLog.getId(),
                                clearingJobModel.getSelectedDate()
                        );
                    }
                })
        );
    }

    private boolean isTimeToSettle(MerchantEntity merchant) {

        final String purchaseCron = merchant.getPurchaseCron();

        return merchant.getSettlementNextValidTimes().stream()
                .filter(settlementNextValidTime -> SettlementType.Purchase.equals(settlementNextValidTime.getSettlementType()))
                .findFirst()
                .map(settlementNextValidTime -> clearingUtil.isItTimeToSettle(purchaseCron, settlementNextValidTime.getId()))
                .orElse(false);
    }
}
