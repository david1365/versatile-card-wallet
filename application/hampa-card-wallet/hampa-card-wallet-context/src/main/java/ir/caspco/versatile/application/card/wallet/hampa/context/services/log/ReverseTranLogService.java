package ir.caspco.versatile.application.card.wallet.hampa.context.services.log;

import ir.caspco.versatile.application.card.wallet.hampa.context.domains.ReverseTranLogEntity;
import ir.caspco.versatile.jms.client.common.msg.hampa.RevertTranRequest;
import ir.caspco.versatile.jms.client.common.msg.hampa.RevertTranResponse;
import ir.caspco.versatile.jms.client.common.vo.hampa.RevertTransactionEntranceVO;

public interface ReverseTranLogService {

    ReverseTranLogEntity create(RevertTransactionEntranceVO revertTransactionEntranceVO);

    ReverseTranLogEntity fail(ReverseTranLogEntity reverseTranLogEntity);

    ReverseTranLogEntity success(ReverseTranLogEntity reverseTranLogEntity,
                                 RevertTranResponse tranResponse,
                                 RevertTranRequest tranRequest);
}
