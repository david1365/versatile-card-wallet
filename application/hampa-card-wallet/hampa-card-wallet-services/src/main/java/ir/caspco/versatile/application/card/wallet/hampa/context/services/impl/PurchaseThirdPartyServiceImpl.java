package ir.caspco.versatile.application.card.wallet.hampa.context.services.impl;

import ir.caspco.versatile.application.card.wallet.hampa.context.domains.WalletEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.enums.PurchaseType;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.MerchantService;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.WalletService;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.log.PurchaseCardWalletLogService;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.*;
import ir.caspco.versatile.context.jms.client.exceptions.CoreException;
import ir.caspco.versatile.jms.client.common.client.hampa.PurchaseThirdPartyClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Service
@Transactional(noRollbackFor = CoreException.class)
public class PurchaseThirdPartyServiceImpl extends PurchaseBasicThirdPartyImpl {

    private final WalletService walletService;


    @Value("${cardWallet.purchase.directCharge.paymentServiceId:0}")
    private Long directChargePaymentServiceId;

    @Value("${cardWallet.purchase.internetPackage.paymentServiceId:0}")
    private Long internetPackagePaymentServiceId;

    public PurchaseThirdPartyServiceImpl(PurchaseThirdPartyClient purchaseThirdPartyClient,

                                         MerchantService merchantService,
                                         PurchaseCardWalletLogService purchaseCardWalletLogService,
                                         WalletService walletService) {

        super(purchaseThirdPartyClient, merchantService, purchaseCardWalletLogService);

        this.walletService = walletService;
    }


    @Override
    public PurchaseCharityResultVO charityByCard(PurchasePinChargeCharityByCardEntranceVO cardEntrance) {

        WalletEntity wallet = findByCardAndSegmentCode(cardEntrance);

        return purchaseCharity(cardEntrance, wallet, PurchaseType.CHARITY, cardEntrance.getPaymentServiceId());
    }

    @Override
    public PurchaseCharityResultVO charityByWallet(PurchasePinChargeCharityByWalletEntranceVO walletEntrance) {

        WalletEntity wallet = findByWalletId(walletEntrance.getWalletId());

        return purchaseCharity(walletEntrance, wallet, PurchaseType.CHARITY, walletEntrance.getPaymentServiceId());
    }

    @Override
    public PurchasePinChargeResultVO pinChargeByCard(PurchasePinChargeCharityByCardEntranceVO cardEntrance) {

        WalletEntity wallet = findByCardAndSegmentCode(cardEntrance);

        return purchasePinCharge(cardEntrance, wallet, PurchaseType.PIN_CHARGE, cardEntrance.getPaymentServiceId());
    }

    @Override
    public PurchasePinChargeResultVO pinChargeByWallet(PurchasePinChargeCharityByWalletEntranceVO walletEntrance) {

        WalletEntity wallet = findByWalletId(walletEntrance.getWalletId());

        return purchasePinCharge(walletEntrance, wallet, PurchaseType.PIN_CHARGE, walletEntrance.getPaymentServiceId());
    }


    @Override
    public PurchasePackageChargeResultVO directChargeByCard(PurchaseDirectChargeByCardEntranceVO cardEntrance) {

        WalletEntity wallet = findByCardAndSegmentCode(cardEntrance);

        return purchasePackageCharge(cardEntrance, wallet, PurchaseType.DIRECT_CHARGE, directChargePaymentServiceId);
    }

    @Override
    public PurchasePackageChargeResultVO directChargeByWallet(PurchaseDirectChargeByWalletEntranceVO walletEntrance) {

        WalletEntity wallet = findByWalletId(walletEntrance.getWalletId());

        return purchasePackageCharge(walletEntrance, wallet, PurchaseType.DIRECT_CHARGE, directChargePaymentServiceId);
    }


    @Override
    public PurchasePackageChargeResultVO internetPackageByCard(PurchaseInternetPackageByCardEntranceVO purchaseInternetPackageByCardEntrance) {

        WalletEntity wallet = findByCardAndSegmentCode(purchaseInternetPackageByCardEntrance);

        return purchasePackageCharge(purchaseInternetPackageByCardEntrance, wallet, PurchaseType.INTERNET_PACKAGE, internetPackagePaymentServiceId);
    }

    @Override
    public PurchasePackageChargeResultVO internetPackageByWallet(PurchaseInternetPackageByWalletEntranceVO purchaseInternetPackageByWalletEntrance) {

        WalletEntity wallet = findByWalletId(purchaseInternetPackageByWalletEntrance.getWalletId());

        return purchasePackageCharge(purchaseInternetPackageByWalletEntrance, wallet, PurchaseType.INTERNET_PACKAGE, internetPackagePaymentServiceId);
    }

    private WalletEntity findByWalletId(UUID walletId) {

        return walletService.findByWalletIdAndStatusTrue(walletId);
    }

    private WalletEntity findByCardAndSegmentCode(PurchaseThirdPartyCardEntranceVO cardEntrance) {

        return walletService.findByCardNumberAndSegmentCodeAndStatusTrue(
                cardEntrance.getCardNumber(),
                cardEntrance.getSegmentCode()
        );
    }
}
