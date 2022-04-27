package ir.caspco.versatile.application.card.wallet.hampa.context.services;

import ir.caspco.versatile.application.card.wallet.hampa.context.vo.InactiveCardWalletResultVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.InactiveWalletByCardEntranceVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.WalletIdEntranceVO;
import ir.caspco.versatile.jms.client.common.vo.hampa.InactiveWalletEntranceVO;

public interface InactiveWalletService {

    InactiveCardWalletResultVO inactive(InactiveWalletEntranceVO inactiveWalletEntranceVO);

    InactiveCardWalletResultVO inactiveByCard(InactiveWalletByCardEntranceVO inactiveWalletByCardEntranceVO);

    InactiveCardWalletResultVO inactiveByWallet(WalletIdEntranceVO walletIdEntranceVO);
}
