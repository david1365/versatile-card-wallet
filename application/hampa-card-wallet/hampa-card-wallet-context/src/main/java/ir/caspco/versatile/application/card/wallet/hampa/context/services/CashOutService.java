package ir.caspco.versatile.application.card.wallet.hampa.context.services;

import ir.caspco.versatile.application.card.wallet.hampa.context.vo.CashOutCardEntranceVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.CashOutResultVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.CashOutWalletEntranceVO;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

public interface CashOutService {

    CashOutResultVO cashOutCard(CashOutCardEntranceVO cashOutCardEntrance);

    CashOutResultVO cashOutWallet(CashOutWalletEntranceVO cashOutWalletEntrance);
}
