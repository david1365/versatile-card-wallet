package ir.caspco.versatile.application.card.wallet.hampa.context.services.log;

import ir.caspco.versatile.application.card.wallet.hampa.context.domains.CashOutLogEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.CashOutCardWalletEntranceVO;
import ir.caspco.versatile.jms.client.common.vo.hampa.CashOutHampaCardResponseVO;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

public interface CashOutLogService {

    CashOutLogEntity create(CashOutCardWalletEntranceVO cashOutCardWalletEntrance);

    CashOutLogEntity fail(CashOutLogEntity cashOutLog);

    CashOutLogEntity success(CashOutHampaCardResponseVO cashOutHampaCardResponse,
                             CashOutLogEntity cashOutLog);

    void reverse(String dealReference);
}
