package ir.caspco.versatile.application.card.wallet.hampa.context.services;

import ir.caspco.versatile.application.card.wallet.hampa.context.domains.WalletEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.enums.PurchaseType;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.*;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 * @author Mohammad Javad Zahedi - 2021
 * mjavadzahedi0@gmail.com
 * 09377732642
 */


public interface PurchaseThirdPartyService {

    PurchaseCharityResultVO charityByCard(PurchasePinChargeCharityByCardEntranceVO cardEntrance);

    PurchaseCharityResultVO charityByWallet(PurchasePinChargeCharityByWalletEntranceVO walletEntrance);

    PurchasePinChargeResultVO pinChargeByCard(PurchasePinChargeCharityByCardEntranceVO cardEntrance);

    PurchasePinChargeResultVO pinChargeByWallet(PurchasePinChargeCharityByWalletEntranceVO walletEntrance);

    PurchasePackageChargeResultVO directChargeByCard(PurchaseDirectChargeByCardEntranceVO cardEntrance);

    PurchasePackageChargeResultVO directChargeByWallet(PurchaseDirectChargeByWalletEntranceVO walletEntrance);

    PurchasePackageChargeResultVO internetPackageByCard(PurchaseInternetPackageByCardEntranceVO purchaseInternetPackageByCardEntranceVO);

    PurchasePackageChargeResultVO internetPackageByWallet(PurchaseInternetPackageByWalletEntranceVO purchaseInternetPackageByWalletEntrance);


    PurchaseThirdPartyFinalResultVO purchase(
            PurchaseThirdPartyBasicEntranceVO purchaseThirdPartyBasicEntrance,
            WalletEntity wallet,
            PurchaseType purchaseType,
            Long paymentServiceId
    );

    PurchaseThirdPartyFinalResultVO purchaseThirdParty(PurchaseThirdPartyEntranceVO purchaseThirdPartyEntrance);
}
