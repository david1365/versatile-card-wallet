package ir.caspco.versatile.application.card.wallet.hampa.context.services.impl.log;

import ir.caspco.versatile.application.card.wallet.hampa.context.domains.ReverseTranLogEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.repositories.ReverseTranLogRepository;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.log.ReverseTranLogService;
import ir.caspco.versatile.context.enums.FlowStatus;
import ir.caspco.versatile.jms.client.common.msg.hampa.RevertTranRequest;
import ir.caspco.versatile.jms.client.common.msg.hampa.RevertTranResponse;
import ir.caspco.versatile.jms.client.common.vo.hampa.RevertTransactionEntranceVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(noRollbackFor = Exception.class)
public class ReverseTranLogServiceImpl implements ReverseTranLogService {


    private final ReverseTranLogRepository reverseTranLogRepository;

    public ReverseTranLogServiceImpl(ReverseTranLogRepository reverseTranLogRepository) {
        this.reverseTranLogRepository = reverseTranLogRepository;
    }


    @Override
    public ReverseTranLogEntity create(RevertTransactionEntranceVO revertTransactionEntranceVO) {

        return reverseTranLogRepository.saveAndFlush(
                ReverseTranLogEntity.builder()
                        //.adjustDeal(tranResponse.getRevertTranResponseVO().getAdjustDeal())
                        .clientTrackingCode(revertTransactionEntranceVO.getClientTrackingCode())
                        .dealReference(revertTransactionEntranceVO.getDealReference())
                        .build()
        );
    }


    @Override
    public ReverseTranLogEntity fail(ReverseTranLogEntity reverseTranLogEntity) {

        reverseTranLogEntity.setFlowStatus(FlowStatus.FAIL);
        return reverseTranLogRepository.saveAndFlush(reverseTranLogEntity);
    }


    @Override
    public ReverseTranLogEntity success(ReverseTranLogEntity reverseTranLogEntity,
                                        RevertTranResponse tranResponse,
                                        RevertTranRequest tranRequest) {

        reverseTranLogEntity.setAdjustDeal(tranResponse.getRevertTranResponseVO().getAdjustDeal());
        reverseTranLogEntity.setClientTrackingCode(tranRequest.getRevertTransactionEntranceVO().getClientTrackingCode());
        reverseTranLogEntity.setDealReference(tranRequest.getRevertTransactionEntranceVO().getDealReference());
        reverseTranLogEntity.setFlowStatus(FlowStatus.DONE);
        return reverseTranLogRepository.saveAndFlush(reverseTranLogEntity);
    }
}
