package ir.caspco.versatile.application.card.wallet.hampa.context.services.impl.log;

import ir.caspco.versatile.application.card.wallet.hampa.context.domains.CashOutLogEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.repositories.CashOutLogRepository;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.log.CashOutLogService;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.CashOutCardWalletEntranceVO;
import ir.caspco.versatile.context.enums.FlowStatus;
import ir.caspco.versatile.jms.client.common.vo.hampa.CashOutHampaCardResponseVO;
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
public class CashOutLogServiceImpl implements CashOutLogService {

    private final CashOutLogRepository cashOutLogRepository;

    public CashOutLogServiceImpl(CashOutLogRepository cashOutLogRepository) {

        this.cashOutLogRepository = cashOutLogRepository;
    }


    @Override
    public CashOutLogEntity create(CashOutCardWalletEntranceVO cashOutCardWalletEntrance) {

        return cashOutLogRepository.saveAndFlush(
                CashOutLogEntity.builder()
                        .walletId(cashOutCardWalletEntrance.getWalletId())
                        .cardNumber(cashOutCardWalletEntrance.getCardNumber())
                        .segmentCode(cashOutCardWalletEntrance.getSegmentCode())
                        .accountNumber(cashOutCardWalletEntrance.getAccountNumber())
                        .amount(cashOutCardWalletEntrance.getAmount())
                        .clientTrackingCode(cashOutCardWalletEntrance.getUniqueTrackingCode())
                        .build()
        );
    }

    @Override
    public CashOutLogEntity fail(CashOutLogEntity cashOutLog) {

        cashOutLog.setFlowStatus(FlowStatus.FAIL);
        return cashOutLogRepository.saveAndFlush(cashOutLog);
    }

    @Override
    public CashOutLogEntity success(CashOutHampaCardResponseVO cashOutHampaCardResponse, CashOutLogEntity cashOutLog) {

        cashOutLog.setDealReference(cashOutHampaCardResponse.getDealReference());
        cashOutLog.setServerTrackingCode(cashOutHampaCardResponse.getServerTrackingCode());
        cashOutLog.setTransactionDate(cashOutHampaCardResponse.getTransactionDate());
        cashOutLog.setFlowStatus(FlowStatus.DONE);

        return cashOutLogRepository.saveAndFlush(cashOutLog);
    }

    public void reverse(String dealReference) {

        cashOutLogRepository.findByDealReference(dealReference)
                .ifPresent(cashOutLog -> {

                    cashOutLog.setFlowStatus(FlowStatus.REVERSE);
                    cashOutLogRepository.saveAndFlush(cashOutLog);
                });
    }
}
