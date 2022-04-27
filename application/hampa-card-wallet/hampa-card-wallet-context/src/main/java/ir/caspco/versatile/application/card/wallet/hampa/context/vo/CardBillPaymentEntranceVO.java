package ir.caspco.versatile.application.card.wallet.hampa.context.vo;

import ir.caspco.versatile.jms.client.common.vo.HampaBillCriteriaVO;
import ir.caspco.versatile.jms.client.common.vo.ThirdPartyConfigVO;
import ir.caspco.versatile.jms.client.common.vo.ThirdPartyRequestVO;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@NoArgsConstructor
public class CardBillPaymentEntranceVO {

    private ThirdPartyRequestVO thirdPartyRequest;
    private ThirdPartyConfigVO thirdPartyConfig;
    private HampaBillCriteriaVO hampaBillCriteria;
}
