package ir.caspco.versatile.application.card.wallet.hampa.context.services;

import ir.caspco.versatile.application.card.wallet.hampa.context.vo.CardExchangesEntranceVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.ExchangesResultVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.WalletExchangesEntranceVO;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

public interface ChargeService {

    ExchangesResultVO chargeTheCard(CardExchangesEntranceVO chargeTheCardEntrance);

    ExchangesResultVO chargeTheWallet(WalletExchangesEntranceVO chargeWalletEntrance);
}
