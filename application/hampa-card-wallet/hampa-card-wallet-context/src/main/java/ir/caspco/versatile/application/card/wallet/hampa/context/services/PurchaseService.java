package ir.caspco.versatile.application.card.wallet.hampa.context.services;

import ir.caspco.versatile.application.card.wallet.hampa.context.vo.CardExchangesEntranceVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.PurchaseResultVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.WalletExchangesEntranceVO;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

public interface PurchaseService {

    PurchaseResultVO purchaseByCard(CardExchangesEntranceVO cardExchangesEntranceVO);

    PurchaseResultVO purchaseByWallet(WalletExchangesEntranceVO walletExchangesEntranceVO);
}
