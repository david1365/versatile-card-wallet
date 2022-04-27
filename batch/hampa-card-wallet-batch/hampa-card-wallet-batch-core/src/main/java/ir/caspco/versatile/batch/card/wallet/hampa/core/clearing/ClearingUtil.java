package ir.caspco.versatile.batch.card.wallet.hampa.core.clearing;

import ir.caspco.versatile.application.card.wallet.hampa.context.domains.ClearingLogEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.domains.MerchantEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.domains.SettlementNextValidTimeEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.exceptions.CoreResultException;
import ir.caspco.versatile.application.card.wallet.hampa.context.exceptions.SettlementNextValidTimeNotFoundException;
import ir.caspco.versatile.application.card.wallet.hampa.context.repositories.ClearingLogRepository;
import ir.caspco.versatile.application.card.wallet.hampa.context.repositories.SettlementNextValidTimeRepository;
import ir.caspco.versatile.batch.card.wallet.hampa.core.clearing.model.TotalAmountsWithMerchantModel;
import ir.caspco.versatile.common.util.Cron;
import ir.caspco.versatile.jms.client.common.client.hampa.MerchantSettlementHampaCardClient;
import ir.caspco.versatile.jms.client.common.enums.hampa.HampaSettlementType;
import ir.caspco.versatile.jms.client.common.msg.hampa.MerchantSettlementHampaCardRequest;
import ir.caspco.versatile.jms.client.common.msg.hampa.MerchantSettlementHampaCardResponse;
import ir.caspco.versatile.jms.client.common.vo.hampa.MerchantSettlementHampaCardRequestVO;
import ir.caspco.versatile.jms.client.common.vo.hampa.MerchantSettlementHampaCardResponseVO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

//TODO from davood akbari: Do not forget to test.

@Component
public class ClearingUtil {

    private final MerchantSettlementHampaCardClient merchantSettlementHampaCardClient;

    private final ClearingLogRepository clearingLogRepository;
    private final SettlementNextValidTimeRepository settlementNextValidTimeRepository;

    public ClearingUtil(MerchantSettlementHampaCardClient merchantSettlementHampaCardClient,

                        ClearingLogRepository clearingLogRepository,
                        SettlementNextValidTimeRepository settlementNextValidTimeRepository) {

        this.merchantSettlementHampaCardClient = merchantSettlementHampaCardClient;

        this.clearingLogRepository = clearingLogRepository;
        this.settlementNextValidTimeRepository = settlementNextValidTimeRepository;
    }


    public MerchantSettlementHampaCardResponseVO clearingCharge(TotalAmountsWithMerchantModel totalAmounts, HampaSettlementType settlementType) {

        MerchantEntity merchant = totalAmounts.getMerchant();

        MerchantSettlementHampaCardRequest
                merchantSettlementHampaCardRequest = MerchantSettlementHampaCardRequest.builder()
                .requestDto(
                        MerchantSettlementHampaCardRequestVO.builder()
                                .amount(totalAmounts.getTotal())
                                .sourceAccountNumber(merchant.getChargeSettlementAccount())
                                .destAccountNumber(merchant.getChargeInternalAccount())
                                .settlementType(settlementType)
                                .build()
                )
                .build();

        MerchantSettlementHampaCardResponse merchantSettlementHampaCardResponse = merchantSettlementHampaCardClient.send(merchantSettlementHampaCardRequest)
                .retrieve()
                .result()
                .orElseThrow(CoreResultException::new);

        return merchantSettlementHampaCardResponse.getResponseDto();
    }

    public MerchantSettlementHampaCardResponseVO clearingPurchase(TotalAmountsWithMerchantModel totalAmounts, HampaSettlementType settlementType) {

        MerchantEntity merchant = totalAmounts.getMerchant();

        MerchantSettlementHampaCardRequest
                merchantSettlementHampaCardRequest = MerchantSettlementHampaCardRequest.builder()
                .requestDto(
                        MerchantSettlementHampaCardRequestVO.builder()
                                .amount(totalAmounts.getTotal())
                                .sourceAccountNumber(merchant.getPurchaseInternalAccount())
                                .destAccountNumber(merchant.getPurchaseSettlementAccount())
                                .settlementType(settlementType)
                                .build()
                )
                .build();

        MerchantSettlementHampaCardResponse merchantSettlementHampaCardResponse = merchantSettlementHampaCardClient.send(merchantSettlementHampaCardRequest)
                .retrieve()
                .result()
                .orElseThrow(CoreResultException::new);

        return merchantSettlementHampaCardResponse.getResponseDto();
    }

    public ClearingLogEntity logClearing(MerchantSettlementHampaCardResponseVO merchantSettlement,
                                         MerchantEntity merchant,
                                         HampaSettlementType settlementType) {

        return clearingLogRepository.saveAndFlush(
                ClearingLogEntity.builder()
                        .clearingDealReference(merchantSettlement.getDealReference())
                        .clearingBookDate(merchantSettlement.getBookDate())
                        .merchant(merchant)
                        .settlementType(settlementType)
                        .build()
        );
    }

    public boolean isItTimeToSettle(String cron, BigDecimal settlementNextValidTimeId) {

        SettlementNextValidTimeEntity settlementNextValidTime =
                settlementNextValidTimeRepository.findById(settlementNextValidTimeId)
                        .orElseThrow(SettlementNextValidTimeNotFoundException::new);

        Date lastNextValidTime = settlementNextValidTime.getNextValidTime();
        Date now = new Date();

        Cron corn = Cron.expression(cron);
        Date lastNextInvalidTime = corn.nextInvalidTimeAfter(lastNextValidTime);

        LocalDateTime localNow = now.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        LocalDateTime localLastNextValidTime = lastNextValidTime.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        LocalDateTime localLastNextInValidTime = lastNextInvalidTime.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();


        long settlementDiff = ChronoUnit.SECONDS.between(localLastNextValidTime, localNow);
        long nextSettlementDiff = ChronoUnit.SECONDS.between(localLastNextValidTime, localLastNextInValidTime);

        if (settlementDiff > nextSettlementDiff) { // بدست آورن آخرین تاریخ معتبر

            long settlementCount = settlementDiff / nextSettlementDiff;

            localLastNextValidTime = localLastNextValidTime.plusSeconds(nextSettlementDiff * settlementCount);

            lastNextValidTime = Timestamp.valueOf(localLastNextValidTime);

            lastNextInvalidTime = corn.nextInvalidTimeAfter(lastNextValidTime);
        }

        settlementNextValidTime.setNextValidTime(corn.nextValidTimeAfter(lastNextInvalidTime));
        settlementNextValidTimeRepository.saveAndFlush(settlementNextValidTime);

        return settlementDiff >= 0 && settlementDiff <= nextSettlementDiff;
    }
}
