package ir.caspco.versatile.application.card.wallet.hampa.context.services;

import ir.caspco.versatile.application.card.wallet.hampa.context.vo.CreditCardInquireResultVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.CreditCardRegistrationEntranceVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.CreditCardRegistrationResultVO;
import ir.caspco.versatile.jms.client.common.vo.SegmentCompanyPlanVO;

import java.util.List;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

public interface CreditCardService {

    List<SegmentCompanyPlanVO> loadCompanyPlan();

    CreditCardRegistrationResultVO registerCreditCard(CreditCardRegistrationEntranceVO creditCardRegistrationEntrance);

    CreditCardInquireResultVO inquireCreditCard(String nationalCode);
}
