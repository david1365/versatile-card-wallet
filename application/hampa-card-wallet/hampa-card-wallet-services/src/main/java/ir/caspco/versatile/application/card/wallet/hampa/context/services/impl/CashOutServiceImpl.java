package ir.caspco.versatile.application.card.wallet.hampa.context.services.impl;

import ir.caspco.versatile.application.card.wallet.hampa.context.domains.CashOutLogEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.domains.WalletEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.exceptions.CashOutResultException;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.CashOutService;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.WalletService;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.log.CashOutLogService;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.CashOutCardEntranceVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.CashOutCardWalletEntranceVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.CashOutResultVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.CashOutWalletEntranceVO;
import ir.caspco.versatile.context.jms.client.exceptions.CoreException;
import ir.caspco.versatile.jms.client.common.client.hampa.CashOutHampaCardByAccountClient;
import ir.caspco.versatile.jms.client.common.msg.hampa.CashOutHampaCardByAccountRequest;
import ir.caspco.versatile.jms.client.common.msg.hampa.CashOutHampaCardByAccountResponse;
import ir.caspco.versatile.jms.client.common.vo.hampa.CashOutHampaCardRequestVO;
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
@Transactional(noRollbackFor = CoreException.class)
public class CashOutServiceImpl implements CashOutService {

    private final CashOutHampaCardByAccountClient cashOutHampaCardByAccountClient;

    private final WalletService walletService;
    private final CashOutLogService cashOutLogService;


    public CashOutServiceImpl(CashOutHampaCardByAccountClient cashOutHampaCardByAccountClient,
                              WalletService walletService,
                              CashOutLogService cashOutLogService) {

        this.cashOutHampaCardByAccountClient = cashOutHampaCardByAccountClient;

        this.walletService = walletService;
        this.cashOutLogService = cashOutLogService;
    }


    @Override
    public CashOutResultVO cashOutCard(CashOutCardEntranceVO cashOutCardEntrance) {

        WalletEntity wallet = walletService.findByCardNumberAndSegmentCodeAndStatusTrue(
                cashOutCardEntrance.getCardNumber(), cashOutCardEntrance.getSegmentCode()
        );

        return cashOut(
                CashOutCardWalletEntranceVO.builder()
                        .walletId(wallet.getWalletId())
                        .cardNumber(cashOutCardEntrance.getCardNumber())
                        .segmentCode(cashOutCardEntrance.getSegmentCode())
                        .amount(cashOutCardEntrance.getAmount())
                        .accountNumber(cashOutCardEntrance.getAccountNumber())
                        .uniqueTrackingCode(cashOutCardEntrance.getClientTrackingCode())
                        .build()
        );
    }

    @Override
    public CashOutResultVO cashOutWallet(CashOutWalletEntranceVO cashOutWalletEntrance) {

        WalletEntity wallet = walletService.findByWalletIdAndStatusTrue(cashOutWalletEntrance.getWalletId());

        return cashOut(
                CashOutCardWalletEntranceVO.builder()
                        .walletId(wallet.getWalletId())
                        .cardNumber(wallet.getCard().getCardNumber())
                        .segmentCode(wallet.getSegmentCode())
                        .amount(cashOutWalletEntrance.getAmount())
                        .accountNumber(cashOutWalletEntrance.getAccountNumber())
                        .uniqueTrackingCode(cashOutWalletEntrance.getClientTrackingCode())
                        .build()
        );
    }

    private CashOutResultVO cashOut(CashOutCardWalletEntranceVO cashOutCardWalletEntrance) {

        CashOutHampaCardByAccountRequest cashOutHampaCardByAccountRequest = CashOutHampaCardByAccountRequest.builder()
                .cashOutHampaCardRequest(CashOutHampaCardRequestVO.builder()
                        .cardNumber(cashOutCardWalletEntrance.getCardNumber())
                        .segmentCode(cashOutCardWalletEntrance.getSegmentCode())
                        .accountNumber(cashOutCardWalletEntrance.getAccountNumber())
                        .amount(cashOutCardWalletEntrance.getAmount())
                        .checkUniqueTrackingCode(true)
                        .uniqueTrackingCode(cashOutCardWalletEntrance.getUniqueTrackingCode())
                        .build())
                .build();

        CashOutLogEntity cashOutLogEntity = cashOutLogService.create(cashOutCardWalletEntrance);

        CashOutHampaCardByAccountResponse cashOutHampaCardByAccountResponse = cashOutHampaCardByAccountClient.send(cashOutHampaCardByAccountRequest)
                .onError(error -> cashOutLogService.fail(cashOutLogEntity))
                .throwException()
                .retrieve()
                .result()
                .orElseThrow(CashOutResultException::new);

        CashOutHampaCardResponseVO cashOutHampaCardResponse = cashOutHampaCardByAccountResponse.getCashOutHampaCardResponse();

        cashOutLogService.success(cashOutHampaCardResponse, cashOutLogEntity);

        return CashOutResultVO.builder()
                .walletId(cashOutCardWalletEntrance.getWalletId())
                .cardNumber(cashOutCardWalletEntrance.getCardNumber())
                .segmentCode(cashOutCardWalletEntrance.getSegmentCode())
                .amount(cashOutCardWalletEntrance.getAmount())
                .accountNumber(cashOutCardWalletEntrance.getAccountNumber())
                .clientTrackingCode(cashOutHampaCardResponse.getClientTrackingCode())
                .bookBalance(cashOutHampaCardResponse.getCurrentBookBalance())
                .dealReference(cashOutHampaCardResponse.getDealReference())
                .serverTrackingCode(cashOutHampaCardResponse.getServerTrackingCode())
                .transactionDate(cashOutHampaCardResponse.getTransactionDate())
                .build();

    }
}
