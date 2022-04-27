package ir.caspco.versatile.application.card.wallet.hampa.context.services;

import ir.caspco.versatile.application.card.wallet.hampa.context.vo.CardSegmentBookBalanceEntranceVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.SegmentBookBalanceResultVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.WalletIdEntranceVO;

import java.util.List;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

public interface BalanceService {

    List<SegmentBookBalanceResultVO> cardBalance(CardSegmentBookBalanceEntranceVO cardSegmentBookBalanceEntrance);

    SegmentBookBalanceResultVO walletBalance(WalletIdEntranceVO walletIdEntrance);
}
