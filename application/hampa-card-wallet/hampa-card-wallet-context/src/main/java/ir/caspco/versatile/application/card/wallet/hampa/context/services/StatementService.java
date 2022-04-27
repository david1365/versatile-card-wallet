package ir.caspco.versatile.application.card.wallet.hampa.context.services;

import ir.caspco.versatile.application.card.wallet.hampa.context.vo.CardStatementEntranceVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.StatementResultVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.WalletStatementEntranceVO;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

public interface StatementService {

    StatementResultVO cardStatement(CardStatementEntranceVO cardStatementEntrance);

    StatementResultVO walletStatement(WalletStatementEntranceVO walletStatementEntrance);
}
