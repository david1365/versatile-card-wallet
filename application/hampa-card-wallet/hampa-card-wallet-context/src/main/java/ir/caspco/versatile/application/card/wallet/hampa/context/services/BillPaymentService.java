package ir.caspco.versatile.application.card.wallet.hampa.context.services;

import ir.caspco.versatile.application.card.wallet.hampa.context.vo.BillPaymentResultVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.CardBillPaymentEntranceVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.WalletBillPaymentEntranceVO;

public interface BillPaymentService {

    BillPaymentResultVO billPaymentByCard(CardBillPaymentEntranceVO cardBillPaymentEntranceVO);

    BillPaymentResultVO billPaymentByWallet(WalletBillPaymentEntranceVO walletBillPaymentEntranceVO);
}
