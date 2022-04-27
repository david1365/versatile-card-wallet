package ir.caspco.versatile.application.card.wallet.hampa.context.services.log;

import ir.caspco.versatile.application.card.wallet.hampa.context.domains.MerchantEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.domains.PurchaseCardWalletLogEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.enums.PurchaseType;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.CardWalletExchangesEntranceVO;
import ir.caspco.versatile.jms.client.common.vo.hampa.BuyProductByHampaCardResponseVO;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

public interface PurchaseCardWalletLogService {

    PurchaseCardWalletLogEntity create(CardWalletExchangesEntranceVO cardWalletExchangesEntrance, MerchantEntity merchant);

    PurchaseCardWalletLogEntity create(CardWalletExchangesEntranceVO cardWalletExchangesEntrance,
                                       MerchantEntity merchant,
                                       PurchaseType purchaseType);

    PurchaseCardWalletLogEntity fail(PurchaseCardWalletLogEntity purchaseCardWalletLogEntity);

    PurchaseCardWalletLogEntity success(BuyProductByHampaCardResponseVO buyProductByHampaCardResponse,
                                        PurchaseCardWalletLogEntity purchaseCardWalletLogEntity);

    void reverse(String dealReference);
}
