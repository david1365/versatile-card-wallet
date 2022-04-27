package ir.caspco.versatile.application.card.wallet.hampa.context.services.impl.log;

import ir.caspco.versatile.application.card.wallet.hampa.context.domains.MerchantEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.domains.PurchaseCardWalletLogEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.enums.PurchaseType;
import ir.caspco.versatile.application.card.wallet.hampa.context.repositories.PurchaseCardWalletLogRepository;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.log.PurchaseCardWalletLogService;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.CardWalletExchangesEntranceVO;
import ir.caspco.versatile.context.enums.FlowStatus;
import ir.caspco.versatile.jms.client.common.vo.hampa.BuyProductByHampaCardResponseVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Service
@Transactional(noRollbackFor = Exception.class)
public class PurchaseCardWalletLogServiceImpl implements PurchaseCardWalletLogService {

    private final PurchaseCardWalletLogRepository purchaseCardWalletLogRepository;

    public PurchaseCardWalletLogServiceImpl(PurchaseCardWalletLogRepository purchaseCardWalletLogRepository) {

        this.purchaseCardWalletLogRepository = purchaseCardWalletLogRepository;
    }

    @Override
    public PurchaseCardWalletLogEntity create(CardWalletExchangesEntranceVO cardWalletExchangesEntrance,
                                              MerchantEntity merchant) {

        return create(cardWalletExchangesEntrance, merchant, PurchaseType.PURCHASE);
    }

    @Override
    public PurchaseCardWalletLogEntity create(CardWalletExchangesEntranceVO cardWalletExchangesEntrance,
                                              MerchantEntity merchant,
                                              PurchaseType purchaseType) {

        return purchaseCardWalletLogRepository.saveAndFlush(
                PurchaseCardWalletLogEntity.builder()
                        .walletId(cardWalletExchangesEntrance.getWalletId())
                        .cardNumber(cardWalletExchangesEntrance.getCardNumber())
                        .segmentCode(cardWalletExchangesEntrance.getSegmentCode())
                        .merchant(merchant)
                        .amount(cardWalletExchangesEntrance.getAmount())
                        .purchaseType(purchaseType)
                        .clientTrackingCode(cardWalletExchangesEntrance.getClientTrackingCode())
                        .build()
        );
    }

    @Override
    public PurchaseCardWalletLogEntity fail(PurchaseCardWalletLogEntity purchaseCardWalletLog) {

        purchaseCardWalletLog.setFlowStatus(FlowStatus.FAIL);
        return purchaseCardWalletLogRepository.saveAndFlush(purchaseCardWalletLog);
    }

    @Override
    public PurchaseCardWalletLogEntity success(BuyProductByHampaCardResponseVO buyProductByHampaCardResponse,
                                               PurchaseCardWalletLogEntity purchaseCardWalletLog) {

        purchaseCardWalletLog.setDealReference(buyProductByHampaCardResponse.getDepositDealReference());
        purchaseCardWalletLog.setCreditDealReference(buyProductByHampaCardResponse.getCreditDealReference());
        purchaseCardWalletLog.setServerCreditTrackingCode(buyProductByHampaCardResponse.getServerCreditTrackingCode());
        purchaseCardWalletLog.setServerTrackingCode(buyProductByHampaCardResponse.getServerTrackingCode());
        purchaseCardWalletLog.setTransactionDate(buyProductByHampaCardResponse.getTransactionDate());
        purchaseCardWalletLog.setFlowStatus(FlowStatus.DONE);

        return purchaseCardWalletLogRepository.saveAndFlush(purchaseCardWalletLog);
    }

    public void reverse(String dealReference) {

        purchaseCardWalletLogRepository.findByDealReference(dealReference)
                .ifPresent(purchaseCardWalletLog -> {

                    purchaseCardWalletLog.setFlowStatus(FlowStatus.REVERSE);
                    purchaseCardWalletLogRepository.save(purchaseCardWalletLog);
                });

        purchaseCardWalletLogRepository.findByCreditDealReference(dealReference)
                .ifPresent(purchaseCardWalletLog -> {

                    purchaseCardWalletLog.setFlowStatus(FlowStatus.REVERSE);
                    purchaseCardWalletLogRepository.save(purchaseCardWalletLog);
                });
    }
}
