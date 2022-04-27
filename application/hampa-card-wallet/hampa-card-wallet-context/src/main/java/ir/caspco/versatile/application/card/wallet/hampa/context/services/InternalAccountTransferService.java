package ir.caspco.versatile.application.card.wallet.hampa.context.services;

import ir.caspco.versatile.application.card.wallet.hampa.context.vo.CardInternalAccountTransferEntranceVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.MobileInternalAccountTransferEntranceVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.TransferResultVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.WalletInternalAccountTransferEntranceVO;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */
public interface InternalAccountTransferService {

    TransferResultVO transferByCard(CardInternalAccountTransferEntranceVO cardInternalAccountTransferEntrance);

    TransferResultVO transferByWallet(WalletInternalAccountTransferEntranceVO walletInternalAccountTransferEntrance);

    TransferResultVO transferByMobile(MobileInternalAccountTransferEntranceVO mobileInternalAccountTransferEntrance);
}
