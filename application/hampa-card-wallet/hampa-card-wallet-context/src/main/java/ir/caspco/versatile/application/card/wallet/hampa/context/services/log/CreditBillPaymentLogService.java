package ir.caspco.versatile.application.card.wallet.hampa.context.services.log;

import ir.caspco.versatile.application.card.wallet.hampa.context.domains.CreditBillPaymentLogEntity;
import ir.caspco.versatile.jms.client.common.vo.CollectPaymentRequestVO;
import ir.caspco.versatile.jms.client.common.vo.CollectPaymentResponseVO;

import java.util.List;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

public interface CreditBillPaymentLogService {

    List<CreditBillPaymentLogEntity> createAndPeek(List<CollectPaymentRequestVO> collectPaymentRequests);

    List<CreditBillPaymentLogEntity> fail(List<CreditBillPaymentLogEntity> creditBillPaymentLogs);

    List<CreditBillPaymentLogEntity> success(List<CollectPaymentResponseVO> collectPaymentResponses,
                                             List<CreditBillPaymentLogEntity> creditBillPaymentLogs);
}
