package ir.caspco.versatile.application.card.wallet.hampa.context.services.impl;

import ir.caspco.versatile.application.card.wallet.hampa.context.domains.*;
import ir.caspco.versatile.application.card.wallet.hampa.context.enums.PurchaseType;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.MerchantService;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.PurchaseThirdPartyService;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.log.PurchaseCardWalletLogService;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.*;
import ir.caspco.versatile.common.util.CoreUtil;
import ir.caspco.versatile.common.util.Shift;
import ir.caspco.versatile.jms.client.common.client.hampa.PurchaseThirdPartyClient;
import ir.caspco.versatile.jms.client.common.enums.thirdparty.ChannelType;
import ir.caspco.versatile.jms.client.common.enums.thirdparty.InternetPackageOperator;
import ir.caspco.versatile.jms.client.common.enums.thirdparty.ServiceType;
import ir.caspco.versatile.jms.client.common.exceptions.CoreContentResultException;
import ir.caspco.versatile.jms.client.common.msg.hampa.PurchaseThirdPartyRequest;
import ir.caspco.versatile.jms.client.common.msg.hampa.PurchaseThirdPartyResponse;
import ir.caspco.versatile.jms.client.common.vo.ThirdPartyConfigVO;
import ir.caspco.versatile.jms.client.common.vo.ThirdPartyRequestVO;
import ir.caspco.versatile.jms.client.common.vo.hampa.BuyProductByHampaCardResponseVO;
import ir.caspco.versatile.jms.client.common.vo.hampa.PurchaseThirdPartyResultVO;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

public abstract class PurchaseBasicThirdPartyImpl implements PurchaseThirdPartyService {

    private final PurchaseThirdPartyClient purchaseThirdPartyClient;

    private final MerchantService merchantService;
    private final PurchaseCardWalletLogService purchaseCardWalletLogService;

    public PurchaseBasicThirdPartyImpl(PurchaseThirdPartyClient purchaseThirdPartyClient,

                                       MerchantService merchantService,
                                       PurchaseCardWalletLogService purchaseCardWalletLogService) {

        this.purchaseThirdPartyClient = purchaseThirdPartyClient;

        this.merchantService = merchantService;
        this.purchaseCardWalletLogService = purchaseCardWalletLogService;
    }


    public PurchaseThirdPartyFinalResultVO purchase(
            PurchaseThirdPartyBasicEntranceVO purchaseThirdPartyBasicEntrance,
            WalletEntity wallet,
            PurchaseType purchaseType,
            Long paymentServiceId
    ) {

        CardEntity card = wallet.getCard();
        PersonEntity person = card.getPerson();

        PurchaseThirdPartyEntranceVO purchaseThirdPartyEntrance = Shift.just(purchaseThirdPartyBasicEntrance)
                .toShift(PurchaseThirdPartyEntranceVO.class)
                .toObject();

        purchaseThirdPartyEntrance.setNationalCode(person.getNationalCode());
        purchaseThirdPartyEntrance.setWalletId(wallet.getWalletId());
        purchaseThirdPartyEntrance.setCardNumber(card.getCardNumber());
        purchaseThirdPartyEntrance.setSegmentCode(wallet.getSegmentCode());
        purchaseThirdPartyEntrance.setPurchaseType(purchaseType);
        purchaseThirdPartyEntrance.setPaymentServiceId(paymentServiceId);


        return purchaseThirdParty(purchaseThirdPartyEntrance);
    }


    @Override
    public PurchaseThirdPartyFinalResultVO purchaseThirdParty(PurchaseThirdPartyEntranceVO purchaseThirdPartyEntrance) {

        MerchantEntity merchant = merchantService.findByMerchantIdAndActiveTrue(purchaseThirdPartyEntrance.getMerchantId());

        PurchaseCardWalletLogEntity purchaseCardWalletLog = createLog(purchaseThirdPartyEntrance, merchant);

        final ServiceType serviceType = purchaseThirdPartyEntrance.getServiceType();
        final InternetPackageOperator operator = purchaseThirdPartyEntrance.getOperator();

        PurchaseThirdPartyRequest purchaseRequest = PurchaseThirdPartyRequest.builder()
                .purchaseThirdPartyEntrance(
                        ir.caspco.versatile.jms.client.common.vo.hampa.PurchaseThirdPartyEntranceVO.builder()
                                .segmentCode(purchaseThirdPartyEntrance.getSegmentCode())
                                .accountRef(merchant.getPurchaseInternalAccount())
                                .thirdPartyRequest(
                                        ThirdPartyRequestVO.builder()
                                                .thirdPartyConfig(
                                                        ThirdPartyConfigVO.builder()
                                                                .id(purchaseThirdPartyEntrance.getPaymentServiceId())//payment service id //config
                                                                .build()
                                                )
                                                .paymentAmount(purchaseThirdPartyEntrance.getAmount())
                                                .trackingNumber(CoreUtil.generateTrackingNumber(purchaseCardWalletLog.getId()))
                                                .customerNumber(Long.valueOf(purchaseThirdPartyEntrance.getNationalCode())) //if has credit card customer number // otherwise national code
                                                .cellNumber(purchaseThirdPartyEntrance.getMobileNumber())
                                                .serviceType(serviceType != null ? serviceType.id() : null)
                                                .operatorId(operator != null ? operator.value() : null)
                                                .productId(purchaseThirdPartyEntrance.getProductId())
                                                .channelType(ChannelType.PERSONAL_INTERNET_BANK.value())
                                                .cardNumber(purchaseThirdPartyEntrance.getCardNumber())
                                                .build())
                                .checkUniqueTrackingCode(true)
                                .clientTrackingCode(purchaseThirdPartyEntrance.getClientTrackingCode())
                                .build()
                )
                .build();

        PurchaseThirdPartyResponse purchaseThirdPartyResponse = purchaseThirdPartyClient.send(purchaseRequest)
                .onError(throwable -> purchaseCardWalletLogService.fail(purchaseCardWalletLog))
                .throwException()
                .retrieve()
                .result()
                .orElseThrow(CoreContentResultException::new);

        PurchaseThirdPartyResultVO purchaseThirdPartyResult = purchaseThirdPartyResponse.getPurchaseThirdPartyResult();

        successLog(purchaseThirdPartyResult, purchaseCardWalletLog);


        return convertToPurchaseThirdPartyFinalResultVO(purchaseThirdPartyEntrance, purchaseThirdPartyResult);
    }

    private PurchaseThirdPartyFinalResultVO convertToPurchaseThirdPartyFinalResultVO(
            PurchaseThirdPartyEntranceVO purchaseThirdPartyEntrance,
            PurchaseThirdPartyResultVO purchaseThirdPartyResult
    ) {

        PurchaseThirdPartyFinalResultVO purchaseThirdPartyFinalResult =
                Shift.just(purchaseThirdPartyResult).toShift(PurchaseThirdPartyFinalResultVO.class).toObject();

        purchaseThirdPartyFinalResult.setAmount(purchaseThirdPartyEntrance.getAmount());
        purchaseThirdPartyFinalResult.setCardNumber(purchaseThirdPartyEntrance.getCardNumber());
        purchaseThirdPartyFinalResult.setMobileNumber(purchaseThirdPartyEntrance.getMobileNumber());
        purchaseThirdPartyFinalResult.setWalletId(purchaseThirdPartyEntrance.getWalletId());
        purchaseThirdPartyFinalResult.setMerchantId(purchaseThirdPartyEntrance.getMerchantId());
        purchaseThirdPartyFinalResult.setNationalCode(purchaseThirdPartyEntrance.getNationalCode());
        purchaseThirdPartyFinalResult.setSegmentCode(purchaseThirdPartyEntrance.getSegmentCode());

        return purchaseThirdPartyFinalResult;
    }

    private PurchaseCardWalletLogEntity createLog(PurchaseThirdPartyEntranceVO purchaseThirdPartyEntrance,
                                                  MerchantEntity merchant) {
        CardWalletExchangesEntranceVO cardWalletExchangesEntrance =
                Shift.just(purchaseThirdPartyEntrance).toShift(CardWalletExchangesEntranceVO.class).toObject();

        return purchaseCardWalletLogService.create(
                cardWalletExchangesEntrance,
                merchant,
                purchaseThirdPartyEntrance.getPurchaseType()
        );
    }

    private void successLog(PurchaseThirdPartyResultVO purchaseThirdPartyResult, PurchaseCardWalletLogEntity purchaseCardWalletLog) {

        purchaseCardWalletLogService.success(
                BuyProductByHampaCardResponseVO.builder()
                        .depositDealReference(purchaseThirdPartyResult.getDealReference())
                        .clientTrackingCode(purchaseThirdPartyResult.getClientTrackingCode())
                        .serverTrackingCode(purchaseThirdPartyResult.getServerTrackingCode())
                        .transactionDate(purchaseThirdPartyResult.getThirdPartyResponse().getPaymentDate())
                        .build(),
                purchaseCardWalletLog);
    }

    public PurchaseCharityResultVO purchaseCharity(
            PurchaseThirdPartyBasicEntranceVO purchaseThirdPartyBasicEntrance,
            WalletEntity wallet,
            PurchaseType purchaseType,
            Long charityPaymentServiceId
    ) {

        PurchaseThirdPartyFinalResultVO purchaseThirdPartyFinalResult = purchase(purchaseThirdPartyBasicEntrance, wallet, purchaseType, charityPaymentServiceId);

        PurchaseCharityResultVO purchaseCharityResult = Shift.just(purchaseThirdPartyFinalResult)
                .toShift(PurchaseCharityResultVO.class).toObject();

        purchaseCharityResult.setPaymentDate(purchaseThirdPartyFinalResult.getThirdPartyResponse().getPaymentDate());
        purchaseCharityResult.setPaymentServiceId(charityPaymentServiceId);

        return purchaseCharityResult;
    }

    public PurchasePinChargeResultVO purchasePinCharge(
            PurchaseThirdPartyBasicEntranceVO purchaseThirdPartyBasicEntrance,
            WalletEntity wallet,
            PurchaseType purchaseType,
            Long paymentServiceId
    ) {

        PurchaseThirdPartyFinalResultVO purchaseThirdPartyFinalResult = purchase(purchaseThirdPartyBasicEntrance, wallet, purchaseType, paymentServiceId);

        PurchasePinChargeResultVO purchasePinChargeResult = Shift.just(purchaseThirdPartyFinalResult)
                .toShift(PurchasePinChargeResultVO.class).toObject();

        purchasePinChargeResult.setPaymentDate(purchaseThirdPartyFinalResult.getThirdPartyResponse().getPaymentDate());
        purchasePinChargeResult.setSerial(purchaseThirdPartyFinalResult.getThirdPartyResponse().getSerialNumber());
        purchasePinChargeResult.setPassword(purchaseThirdPartyFinalResult.getThirdPartyResponse().getPasswordNumber());
        purchasePinChargeResult.setPaymentServiceId(paymentServiceId);

        return purchasePinChargeResult;
    }


    public PurchasePackageChargeResultVO purchasePackageCharge(
            PurchaseThirdPartyBasicEntranceVO purchaseThirdPartyBasicEntrance,
            WalletEntity wallet,
            PurchaseType purchaseType,
            Long paymentServiceId
    ) {

        PurchaseThirdPartyFinalResultVO purchaseThirdPartyFinalResult = purchase(purchaseThirdPartyBasicEntrance, wallet, purchaseType, paymentServiceId);

        PurchasePackageChargeResultVO purchasePinChargeResult = Shift.just(purchaseThirdPartyFinalResult)
                .toShift(PurchasePackageChargeResultVO.class).toObject();
        purchasePinChargeResult.setPaymentDate(purchaseThirdPartyFinalResult.getThirdPartyResponse().getPaymentDate());

        return purchasePinChargeResult;
    }
}
