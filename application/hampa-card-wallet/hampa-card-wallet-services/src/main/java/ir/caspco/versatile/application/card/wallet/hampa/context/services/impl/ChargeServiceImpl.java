package ir.caspco.versatile.application.card.wallet.hampa.context.services.impl;

import ir.caspco.versatile.application.card.wallet.hampa.context.domains.ChargeCardWalletLogEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.domains.MerchantEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.domains.WalletEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.exceptions.WalletInactiveException;
import ir.caspco.versatile.application.card.wallet.hampa.context.repositories.ChargeCardWalletLogRepository;
import ir.caspco.versatile.application.card.wallet.hampa.context.repositories.WalletRepository;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.ChargeService;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.MerchantService;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.WalletService;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.CardExchangesEntranceVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.ExchangesResultVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.WalletExchangesEntranceVO;
import ir.caspco.versatile.context.enums.FlowStatus;
import ir.caspco.versatile.context.jms.client.exceptions.CoreException;
import ir.caspco.versatile.jms.client.common.client.hampa.HampaChargeTheCardClient;
import ir.caspco.versatile.jms.client.common.msg.hampa.HampaChargeTheCardRequest;
import ir.caspco.versatile.jms.client.common.msg.hampa.HampaChargeTheCardResponse;
import ir.caspco.versatile.jms.client.common.vo.hampa.HampaCardSegmentResponseVO;
import ir.caspco.versatile.jms.client.common.vo.hampa.HampaChargeTheCardSegmentRequestVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

//TODO from davood akbari: Do not forget to test.

@Service
@Transactional(noRollbackFor = CoreException.class)
public class ChargeServiceImpl implements ChargeService {

    private final HampaChargeTheCardClient hampaChargeTheCardClient;

    private final WalletRepository walletRepository;
    private final ChargeCardWalletLogRepository chargeCardWalletLogRepository;

    private final WalletService walletService;
    private final MerchantService merchantService;


    public ChargeServiceImpl(HampaChargeTheCardClient hampaChargeTheCardClient,

                             WalletRepository walletRepository,
                             ChargeCardWalletLogRepository chargeCardWalletLogRepository,

                             WalletService walletService,
                             MerchantService merchantService) {


        this.hampaChargeTheCardClient = hampaChargeTheCardClient;

        this.walletRepository = walletRepository;
        this.chargeCardWalletLogRepository = chargeCardWalletLogRepository;

        this.walletService = walletService;
        this.merchantService = merchantService;
    }

    @Override
    public ExchangesResultVO chargeTheCard(CardExchangesEntranceVO chargeTheCardEntrance) {

        MerchantEntity merchant = merchantService.findByMerchantIdAndActiveTrue(chargeTheCardEntrance.getMerchantId());

        WalletEntity wallet = walletService.findByCardNumberAndSegmentCodeAndStatusTrue(
                chargeTheCardEntrance.getCardNumber(),
                chargeTheCardEntrance.getSegmentCode()
        );

        ChargeCardWalletLogEntity chargeCardWalletLog = createChargeCardLog(chargeTheCardEntrance, merchant, wallet);

        return charge(
                merchant,
                wallet,
                chargeTheCardEntrance.getAmount(),
                chargeTheCardEntrance.getClientTrackingCode(),
                chargeCardWalletLog
        );


    }

    @Override
    public ExchangesResultVO chargeTheWallet(WalletExchangesEntranceVO chargeWalletEntrance) {

        MerchantEntity merchant = merchantService.findByMerchantIdAndActiveTrue(chargeWalletEntrance.getMerchantId());

        Optional<WalletEntity> optionalWallet = walletRepository.findByWalletIdAndStatusTrue(chargeWalletEntrance.getWalletId());

        return optionalWallet.map(wallet -> {

            ChargeCardWalletLogEntity chargeCardWalletLog = createChargeWalletLog(chargeWalletEntrance, merchant, wallet);

            return charge(
                    merchant,
                    wallet,
                    chargeWalletEntrance.getAmount(),
                    chargeWalletEntrance.getClientTrackingCode(),
                    chargeCardWalletLog
            );

        }).orElseThrow(WalletInactiveException::new);

    }

    private ExchangesResultVO charge(MerchantEntity merchant,
                                     WalletEntity wallet,
                                     BigDecimal amount,
                                     String clientTrackingCode,
                                     ChargeCardWalletLogEntity chargeCardWalletLog) {

        HampaCardSegmentResponseVO cardSegmentResponse = chargeTheCard(

                HampaChargeTheCardSegmentRequestVO.builder()
                        .cardNumber(wallet.getCard().getCardNumber())
                        .accountref(merchant.getChargeInternalAccount())
                        .amount(amount)
                        .segmentCode(wallet.getSegmentCode())
                        .checkUniqueTrackingCode(true)
                        .clientTrackingCode(clientTrackingCode)
                        .build(),

                chargeCardWalletLog

        );

        successChargeCardWalletLog(chargeCardWalletLog, cardSegmentResponse);

        return ExchangesResultVO.builder()
                .cardNumber(wallet.getCard().getCardNumber())
                .segmentCode(wallet.getSegmentCode())
                .walletId(wallet.getWalletId())
                .merchantId(merchant.getMerchantId())
                .amount(amount)
                .bookBalance(cardSegmentResponse.getCurrentBookBalance())
                .dealReference(cardSegmentResponse.getDealReference())
                .clientTrackingCode(cardSegmentResponse.getClientTrackingCode())
                .serverTrackingCode(cardSegmentResponse.getServerTrackingCode())
                .transactionDate(cardSegmentResponse.getTransactionDate())
                .build();
    }

    private HampaCardSegmentResponseVO chargeTheCard(HampaChargeTheCardSegmentRequestVO hampaChargeTheCardSegmentRequest,
                                                     ChargeCardWalletLogEntity chargeCardWalletLog) {

        HampaChargeTheCardRequest hampaChargeTheCardRequest = HampaChargeTheCardRequest.builder()
                .requestDto(hampaChargeTheCardSegmentRequest)
                .build();

        HampaChargeTheCardResponse hampaChargeTheCardResponse = hampaChargeTheCardClient.send(hampaChargeTheCardRequest)
                .onError(error -> failChargeCardWalletLog(chargeCardWalletLog))
                .throwException()
                .retrieve()
                .result()
                .orElse(
                        HampaChargeTheCardResponse.builder()
                                .responseDto(HampaCardSegmentResponseVO.builder().build())
                                .build()
                );

        return hampaChargeTheCardResponse.getResponseDto();

    }

    private ChargeCardWalletLogEntity createChargeCardLog(CardExchangesEntranceVO chargeTheCardEntrance,
                                                          MerchantEntity merchant,
                                                          WalletEntity wallet) {
        return chargeCardWalletLogRepository.saveAndFlush(
                ChargeCardWalletLogEntity.builder()
                        .amount(chargeTheCardEntrance.getAmount())
                        .cardNumber(wallet.getCard().getCardNumber())
                        .walletId(wallet.getWalletId())
                        .merchant(merchant)
                        .clientTrackingCode(chargeTheCardEntrance.getClientTrackingCode())
                        .segmentCode(wallet.getSegmentCode())
                        .flowStatus(FlowStatus.CREATION)
                        .build()
        );
    }

    private ChargeCardWalletLogEntity createChargeWalletLog(WalletExchangesEntranceVO chargeWalletEntrance,
                                                            MerchantEntity merchant,
                                                            WalletEntity wallet) {
        return chargeCardWalletLogRepository.saveAndFlush(
                ChargeCardWalletLogEntity.builder()
                        .amount(chargeWalletEntrance.getAmount())
                        .cardNumber(wallet.getCard().getCardNumber())
                        .walletId(wallet.getWalletId())
                        .merchant(merchant)
                        .clientTrackingCode(chargeWalletEntrance.getClientTrackingCode())
                        .segmentCode(wallet.getSegmentCode())
                        .flowStatus(FlowStatus.CREATION)
                        .build()
        );
    }

    private void successChargeCardWalletLog(ChargeCardWalletLogEntity chargeCardWalletLog, HampaCardSegmentResponseVO cardSegmentResponse) {
        chargeCardWalletLog.setFlowStatus(FlowStatus.DONE);
        chargeCardWalletLog.setDealReference(cardSegmentResponse.getDealReference());
        chargeCardWalletLog.setServerTrackingCode(cardSegmentResponse.getServerTrackingCode());
        chargeCardWalletLog.setTransactionDate(cardSegmentResponse.getTransactionDate());

        chargeCardWalletLogRepository.saveAndFlush(chargeCardWalletLog);
    }

    private void failChargeCardWalletLog(ChargeCardWalletLogEntity chargeCardWalletLog) {
        chargeCardWalletLog.setFlowStatus(FlowStatus.FAIL);
        chargeCardWalletLogRepository.saveAndFlush(chargeCardWalletLog);
    }
}
