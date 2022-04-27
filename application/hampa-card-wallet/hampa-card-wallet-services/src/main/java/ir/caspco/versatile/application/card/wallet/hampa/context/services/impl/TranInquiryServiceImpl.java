package ir.caspco.versatile.application.card.wallet.hampa.context.services.impl;


import ir.caspco.versatile.application.card.wallet.hampa.context.domains.ReverseTranLogEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.repositories.ChargeCardWalletLogRepository;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.TranInquiryService;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.log.CashOutLogService;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.log.InternalAccountTransferLogService;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.log.PurchaseCardWalletLogService;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.log.ReverseTranLogService;
import ir.caspco.versatile.context.enums.FlowStatus;
import ir.caspco.versatile.context.jms.client.exceptions.CoreException;
import ir.caspco.versatile.jms.client.common.client.hampa.RevertTranClient;
import ir.caspco.versatile.jms.client.common.client.hampa.TranInqClient;
import ir.caspco.versatile.jms.client.common.exceptions.CoreContentResultException;
import ir.caspco.versatile.jms.client.common.msg.hampa.RevertTranRequest;
import ir.caspco.versatile.jms.client.common.msg.hampa.RevertTranResponse;
import ir.caspco.versatile.jms.client.common.msg.hampa.TranInqRequest;
import ir.caspco.versatile.jms.client.common.msg.hampa.TranInqResponse;
import ir.caspco.versatile.jms.client.common.vo.RevertTranResponseVO;
import ir.caspco.versatile.jms.client.common.vo.hampa.RevertTransactionEntranceVO;
import ir.caspco.versatile.jms.client.common.vo.hampa.TranInqEntranceVO;
import ir.caspco.versatile.jms.client.common.vo.hampa.TranInqVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author sadeghi
 * date:1400/08/30
 * TransactionInquiryService
 */

@Service
@Transactional(noRollbackFor = CoreException.class)
public class TranInquiryServiceImpl implements TranInquiryService {

    private final TranInqClient tranInqClient;
    private final RevertTranClient revertTranClient;

    private final ReverseTranLogService reverseTranLogService;

    private final ChargeCardWalletLogRepository chargeCardWalletLogRepository;
    private final PurchaseCardWalletLogService purchaseCardWalletLogService;
    private final InternalAccountTransferLogService internalAccountTransferLogService;
    private final CashOutLogService cashOutLogService;


    public TranInquiryServiceImpl(TranInqClient tranInqClient,
                                  RevertTranClient revertTranClient,
                                  ReverseTranLogService reverseTranLogService,
                                  ChargeCardWalletLogRepository chargeCardWalletLogRepository, PurchaseCardWalletLogService purchaseCardWalletLogService, InternalAccountTransferLogService internalAccountTransferLogService, CashOutLogService cashOutLogService) {

        this.tranInqClient = tranInqClient;
        this.revertTranClient = revertTranClient;

        this.reverseTranLogService = reverseTranLogService;
        this.chargeCardWalletLogRepository = chargeCardWalletLogRepository;
        this.purchaseCardWalletLogService = purchaseCardWalletLogService;
        this.internalAccountTransferLogService = internalAccountTransferLogService;
        this.cashOutLogService = cashOutLogService;
    }


    @Override
    public List<TranInqVO> getTranInq(TranInqEntranceVO tranInqEntranceVO) {

        TranInqRequest tranInqRequest = TranInqRequest.builder()
                .tranInqEntranceVO(tranInqEntranceVO)
                .build();

        TranInqResponse tranInqResponse = tranInqClient.send(tranInqRequest)
                .retrieve()
                .result()
                .orElseThrow(CoreContentResultException::new);

        return tranInqResponse.getTranInqResult().getTranInqResult();
    }


    @Override
    public RevertTranResponseVO reverseTransaction(RevertTransactionEntranceVO revertTransactionEntrance) {

        RevertTranRequest request = RevertTranRequest.builder()
                .revertTransactionEntranceVO(revertTransactionEntrance)
                .build();

        ReverseTranLogEntity reverseTranLog = reverseTranLogService.create(revertTransactionEntrance);

        RevertTranResponse response = revertTranClient.send(request)
                .onError(error -> reverseTranLogService.fail(reverseTranLog))
                .throwException()
                .retrieve()
                .result()
                .orElseThrow(CoreContentResultException::new);

        reverseTranLogService.success(reverseTranLog, response, request);

        revers(revertTransactionEntrance.getDealReference());

        return response.getRevertTranResponseVO();
    }

    private void revers(String dealReference) {

        purchaseCardWalletLogService.reverse(dealReference);
        internalAccountTransferLogService.reverse(dealReference);
        cashOutLogService.reverse(dealReference);

        chargeCardWalletLogRepository.findByDealReference(dealReference)
                .ifPresent(chargeCardWalletLog -> {

                    chargeCardWalletLog.setFlowStatus(FlowStatus.REVERSE);
                    chargeCardWalletLogRepository.save(chargeCardWalletLog);
                });
    }
}
