package ir.caspco.versatile.batch.card.wallet.hampa.core.clearing.charge;

import ir.caspco.versatile.application.card.wallet.hampa.context.domains.ClearingLogEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.domains.MerchantEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.enums.SettlementType;
import ir.caspco.versatile.application.card.wallet.hampa.context.repositories.ChargeCardWalletLogRepository;
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
public class ChargeClearingItemWriter implements ItemWriter<ClearingWithMerchantModel> {

    private final ClearingUtil clearingUtil;

    private final ChargeCardWalletLogRepository chargeCardWalletLogRepository;

    public ChargeClearingItemWriter(ClearingUtil clearingUtil,

                                    ChargeCardWalletLogRepository chargeCardWalletLogRepository) {

        this.clearingUtil = clearingUtil;

        this.chargeCardWalletLogRepository = chargeCardWalletLogRepository;
    }

    @Override
    public void write(List<? extends ClearingWithMerchantModel> list) {

        list.forEach(clearingJobModel ->

                clearingJobModel.getTotalAmountModels().forEach(totalAmounts -> {

                    MerchantEntity merchant = totalAmounts.getMerchant();

                    if (isTimeToSettle(merchant)) {

                        //TODO from davood akbari: Think about the rollback
                        MerchantSettlementHampaCardResponseVO merchantSettlement = clearingUtil.clearingCharge(totalAmounts, HampaSettlementType.CHARGE);

                        ClearingLogEntity clearingLog = clearingUtil.logClearing(
                                merchantSettlement,
                                merchant,
                                HampaSettlementType.CHARGE
                        );

                        chargeCardWalletLogRepository.clearedLog(
                                merchant.getMerchantId(),
                                clearingLog.getId(),
                                clearingJobModel.getSelectedDate()
                        );
                    }
                })
        );
    }

    private boolean isTimeToSettle(MerchantEntity merchant) {

        final String chargeCron = merchant.getChargeCron();

        return merchant.getSettlementNextValidTimes().stream()
                .filter(settlementNextValidTime -> SettlementType.Charge.equals(settlementNextValidTime.getSettlementType()))
                .findFirst()
                .map(settlementNextValidTime -> clearingUtil.isItTimeToSettle(chargeCron, settlementNextValidTime.getId()))
                .orElse(false);
    }
}
