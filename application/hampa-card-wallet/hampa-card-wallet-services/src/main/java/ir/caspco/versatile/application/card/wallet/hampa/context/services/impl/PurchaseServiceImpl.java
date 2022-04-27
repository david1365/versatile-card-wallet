package ir.caspco.versatile.application.card.wallet.hampa.context.services.impl;

import ir.caspco.versatile.application.card.wallet.hampa.context.domains.*;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.CardService;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.MerchantService;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.PurchaseService;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.WalletService;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.log.PurchaseCardWalletLogService;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.*;
import ir.caspco.versatile.context.jms.client.exceptions.CoreException;
import ir.caspco.versatile.jms.client.common.client.hampa.BuyProductByHampaCardClient;
import ir.caspco.versatile.jms.client.common.enums.CardType;
import ir.caspco.versatile.jms.client.common.exceptions.CoreContentResultException;
import ir.caspco.versatile.jms.client.common.msg.hampa.BuyProductByHampaCardRequest;
import ir.caspco.versatile.jms.client.common.msg.hampa.BuyProductByHampaCardResponse;
import ir.caspco.versatile.jms.client.common.vo.hampa.BuyProductByHampaCardRequestVO;
import ir.caspco.versatile.jms.client.common.vo.hampa.BuyProductByHampaCardResponseVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Service
@Transactional(noRollbackFor = CoreException.class)
public class PurchaseServiceImpl implements PurchaseService {

    private final BuyProductByHampaCardClient buyProductByHampaCardClient;

    private final MerchantService merchantService;
    private final CardService cardService;
    private final WalletService walletService;
    private final PurchaseCardWalletLogService purchaseCardWalletLogService;

    public PurchaseServiceImpl(BuyProductByHampaCardClient buyProductByHampaCardClient,

                               MerchantService merchantService,
                               CardService cardService, WalletService walletService,
                               PurchaseCardWalletLogService purchaseCardWalletLogService) {

        this.buyProductByHampaCardClient = buyProductByHampaCardClient;

        this.merchantService = merchantService;
        this.cardService = cardService;
        this.walletService = walletService;
        this.purchaseCardWalletLogService = purchaseCardWalletLogService;
    }


    @Override
    public PurchaseResultVO purchaseByCard(CardExchangesEntranceVO cardExchangesEntrance) {

        final CardEntity card = cardService.findByCardNumber(cardExchangesEntrance.getCardNumber());

        final WalletEntity wallet = walletService.findByPersonAndSegmentCodeAndStatusTrue(
                card.getPerson(),
                cardExchangesEntrance.getSegmentCode()
        );

        return purchase(
                CardWalletExchangesEntranceVO.builder()
                        .walletId(wallet.getWalletId())
                        .cardNumber(cardExchangesEntrance.getCardNumber())
                        .segmentCode(cardExchangesEntrance.getSegmentCode())
                        .merchantId(cardExchangesEntrance.getMerchantId())
                        .amount(cardExchangesEntrance.getAmount())
                        .clientTrackingCode(cardExchangesEntrance.getClientTrackingCode())
                        .build()
        );
    }

    @Override
    public PurchaseResultVO purchaseByWallet(WalletExchangesEntranceVO walletExchangesEntrance) {

        WalletEntity wallet = walletService.findByWalletIdAndStatusTrue(walletExchangesEntrance.getWalletId());

        CardEntity walletCard = wallet.getCard();
        PersonEntity person = walletCard.getPerson();

        String otherCardNumber = person.getCards().stream()
                .filter(card -> !card.getCardType().equals(walletCard.getCardType()))
                .findFirst()
                .map(CardEntity::getCardNumber)
                .orElse(null);

        final boolean isCredit = CardType.CREDIT.equals(walletCard.getCardType());

        final String depositCardNumber = !isCredit ? walletCard.getCardNumber() : otherCardNumber;
        final String creditCardNumber = isCredit ? walletCard.getCardNumber() : null;


        return purchase(
                CardWalletExchangesEntranceVO.builder()
                        .walletId(walletExchangesEntrance.getWalletId())
                        .cardNumber(depositCardNumber)
                        .creditCardNumber(creditCardNumber)
                        .segmentCode(wallet.getSegmentCode())
                        .merchantId(walletExchangesEntrance.getMerchantId())
                        .amount(walletExchangesEntrance.getAmount())
                        .clientTrackingCode(walletExchangesEntrance.getClientTrackingCode())
                        .build()
        );
    }

    private PurchaseResultVO purchase(CardWalletExchangesEntranceVO cardWalletExchangesEntrance) {

        MerchantEntity merchant = merchantService.findByMerchantIdAndActiveTrue(cardWalletExchangesEntrance.getMerchantId());

        BuyProductByHampaCardRequest cardOtpRegisterRequest = BuyProductByHampaCardRequest.builder()
                .buyProductByHampaCardRequest(
                        BuyProductByHampaCardRequestVO.builder()
                                .cardNumber(cardWalletExchangesEntrance.getCardNumber())
                                .creditCardNumber(cardWalletExchangesEntrance.getCreditCardNumber())
                                .segmentCode(cardWalletExchangesEntrance.getSegmentCode())
                                .accountRef(merchant.getPurchaseInternalAccount())
                                .amount(cardWalletExchangesEntrance.getAmount())
                                .checkUniqueTrackingCode(true)
                                .clientTrackingCode(cardWalletExchangesEntrance.getClientTrackingCode())
                                .build()
                )
                .build();

        PurchaseCardWalletLogEntity purchaseCardWalletLog = purchaseCardWalletLogService.create(cardWalletExchangesEntrance, merchant);

        BuyProductByHampaCardResponse buyProductByHampaCardResponse = buyProductByHampaCardClient.send(cardOtpRegisterRequest)
                .onError(error -> purchaseCardWalletLogService.fail(purchaseCardWalletLog))
                .throwException()
                .retrieve()
                .result()
                .orElseThrow(CoreContentResultException::new);

        BuyProductByHampaCardResponseVO buyProductByHampaCardResponseVO = buyProductByHampaCardResponse.getBuyProductByHampaCardResponse();

        purchaseCardWalletLogService.success(buyProductByHampaCardResponseVO, purchaseCardWalletLog);

        return PurchaseResultVO.builder()
                .walletId(cardWalletExchangesEntrance.getWalletId())
                .cardNumber(cardWalletExchangesEntrance.getCardNumber())
                .segmentCode(cardWalletExchangesEntrance.getSegmentCode())
                .merchantId(cardWalletExchangesEntrance.getMerchantId())
                .amount(cardWalletExchangesEntrance.getAmount())
                .dealReference(buyProductByHampaCardResponseVO.getDepositDealReference())
                .creditDealReference(buyProductByHampaCardResponseVO.getCreditDealReference())
                .serverCreditTrackingCode(buyProductByHampaCardResponseVO.getServerCreditTrackingCode())
                .clientTrackingCode(buyProductByHampaCardResponseVO.getClientTrackingCode())
                .serverTrackingCode(buyProductByHampaCardResponseVO.getServerTrackingCode())
                .transactionDate(buyProductByHampaCardResponseVO.getTransactionDate())
                .segmentsBookBalances(
                        buyProductByHampaCardResponseVO.getSegmentsBookBalanceList().stream()
                                .map(segmentBookBalanceQueryResult -> {

                                    WalletEntity wallet = walletService.findByAccountNumberAndStatusTrue(
                                            segmentBookBalanceQueryResult.getAccountNumber()
                                    );

                                    return SegmentBookBalanceResultVO.builder()
                                            .walletId(wallet.getWalletId())
                                            .segmentCode(segmentBookBalanceQueryResult.getSegmentCode())
                                            .bookBalance(segmentBookBalanceQueryResult.getBookBalance())
                                            .build();
                                })
                                .collect(Collectors.toList())
                )
                .build();
    }
}
