package ir.caspco.versatile.application.card.wallet.hampa.context.services.log;

import ir.caspco.versatile.application.card.wallet.hampa.context.domains.InternalAccountTransferLogEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.TransferEntranceVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.TransferResultVO;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */


public interface InternalAccountTransferLogService {

    InternalAccountTransferLogEntity create(TransferEntranceVO transferEntrance);

    InternalAccountTransferLogEntity fail(InternalAccountTransferLogEntity internalAccountTransferLog);

    InternalAccountTransferLogEntity success(TransferResultVO transferResult,
                                             InternalAccountTransferLogEntity internalAccountTransferLog);

    void reverse(String dealReference);
}
