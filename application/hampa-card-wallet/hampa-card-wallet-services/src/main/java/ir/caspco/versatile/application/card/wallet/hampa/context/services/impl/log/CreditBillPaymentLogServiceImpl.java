package ir.caspco.versatile.application.card.wallet.hampa.context.services.impl.log;

import ir.caspco.versatile.application.card.wallet.hampa.context.domains.CreditBillPaymentLogEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.repositories.CreditBillPaymentLogRepository;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.SegmentMapService;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.log.CreditBillPaymentLogService;
import ir.caspco.versatile.common.util.Shift;
import ir.caspco.versatile.context.enums.FlowStatus;
import ir.caspco.versatile.jms.client.common.vo.CollectPaymentRequestVO;
import ir.caspco.versatile.jms.client.common.vo.CollectPaymentResponseVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Service
@Transactional(noRollbackFor = Exception.class)
public class CreditBillPaymentLogServiceImpl implements CreditBillPaymentLogService {

    private final CreditBillPaymentLogRepository creditBillPaymentLogRepository;

    private final SegmentMapService segmentMapService;

    public CreditBillPaymentLogServiceImpl(CreditBillPaymentLogRepository creditBillPaymentLogRepository,
                                           SegmentMapService segmentMapService) {

        this.creditBillPaymentLogRepository = creditBillPaymentLogRepository;

        this.segmentMapService = segmentMapService;
    }

    @Override
    public List<CreditBillPaymentLogEntity> createAndPeek(List<CollectPaymentRequestVO> collectPaymentRequests) {

        List<CreditBillPaymentLogEntity> creditBillPaymentLogs = new ArrayList<>();

        for (CollectPaymentRequestVO collectPaymentRequest : collectPaymentRequests) {

            final String creditSegmentCode = segmentMapService.toSegment(collectPaymentRequest.getSegmentCode());

            CreditBillPaymentLogEntity creditBillPaymentLog = Shift.just(collectPaymentRequest)
                    .toShift(CreditBillPaymentLogEntity.class)
                    .toObject();

            creditBillPaymentLog.setCreditSegmentCode(creditSegmentCode);
            creditBillPaymentLogs.add(creditBillPaymentLog);

            collectPaymentRequest.setSegmentCode(creditSegmentCode);
        }

        return creditBillPaymentLogRepository.saveAllAndFlush(creditBillPaymentLogs);
    }

    @Override
    public List<CreditBillPaymentLogEntity> fail(List<CreditBillPaymentLogEntity> creditBillPaymentLogs) {

        return creditBillPaymentLogRepository.saveAllAndFlush(
                creditBillPaymentLogs.stream()
                        .peek(creditBillPaymentLog -> creditBillPaymentLog.setFlowStatus(FlowStatus.FAIL))
                        .collect(Collectors.toList())
        );
    }

    @Override
    public List<CreditBillPaymentLogEntity> success(List<CollectPaymentResponseVO> collectPaymentResponses,
                                                    List<CreditBillPaymentLogEntity> creditBillPaymentLogs) {

        return creditBillPaymentLogRepository.saveAllAndFlush(
                collectPaymentResponses.stream()
                        .map(collectPaymentResponse -> {

                            CreditBillPaymentLogEntity creditBillPaymentLog = creditBillPaymentLogs.stream()
                                    .filter(creditBillPaymentLogIn ->
                                            collectPaymentResponse.getSegmentCode()
                                                    .equals(creditBillPaymentLogIn.getCreditSegmentCode())
                                    )
                                    .findAny()
                                    .orElse(CreditBillPaymentLogEntity.builder().build());

                            creditBillPaymentLog.setInterestAmount(collectPaymentResponse.getInterestAmount());
                            creditBillPaymentLog.setPrincipleAmount(collectPaymentResponse.getPrincipleAmount());
                            creditBillPaymentLog.setServerTrackingCode(collectPaymentResponse.getRequestTrackingNumber());
                            creditBillPaymentLog.setDealReference(collectPaymentResponse.getDealReference());

                            creditBillPaymentLog.setFlowStatus(FlowStatus.DONE);

                            return creditBillPaymentLog;

                        })
                        .collect(Collectors.toList())
        );
    }
}
