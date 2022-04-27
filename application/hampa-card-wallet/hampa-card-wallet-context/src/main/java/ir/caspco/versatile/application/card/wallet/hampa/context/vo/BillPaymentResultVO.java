package ir.caspco.versatile.application.card.wallet.hampa.context.vo;

import ir.caspco.versatile.jms.client.common.vo.PayedBillInformationVO;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@NoArgsConstructor
public class BillPaymentResultVO {

    private PayedBillInformationVO payedBillInformation;
}
