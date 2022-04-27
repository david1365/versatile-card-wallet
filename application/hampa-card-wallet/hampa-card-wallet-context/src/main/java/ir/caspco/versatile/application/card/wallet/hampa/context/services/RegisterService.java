package ir.caspco.versatile.application.card.wallet.hampa.context.services;

import ir.caspco.versatile.application.card.wallet.hampa.context.vo.*;
import ir.caspco.versatile.jms.client.common.vo.hampa.HampaCardListRequestVO;
import ir.caspco.versatile.jms.client.common.vo.hampa.RegisterCreditCardResultVO;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

public interface RegisterService {

    CardResultVO register(CardWalletEntranceVO cardWalletEntrance);

    CardResultVO register(CaspianCardWalletEntranceVO caspianCardWalletEntrance);

    CreditCardInquireResultVO register(RegisterCreditCardResultVO registerCreditCardResult);

    HampaCardResultVO list(HampaCardListRequestVO hampaCardListRequest);
}
