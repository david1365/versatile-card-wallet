package ir.caspco.versatile.application.card.wallet.hampa.context.services;

import ir.caspco.versatile.application.card.wallet.hampa.context.vo.*;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

public interface CreditBillsService {

    CreditBillsResultVO creditBillsByCardNumber(String cardNumber);

    CreditBillsResultVO creditBillsByNationalCode(String nationalCode);

    CreditBillsResultVO creditBillsByMobileNumber(String mobileNumber);

    CreditCardBillResultVO loadBillsByCardNumber(String cardNumber);

    CreditCardBillResultVO loadBillsByNationalCode(String nationalCode);

    CreditCardBillResultVO loadBillsByMobileNumber(String mobileNumber);

    CollectiveCreditBillsPaymentResultVO collectiveCreditBillPaymentByCard(
            CardCollectiveCreditBillPaymentEntranceVO cardCollectiveCreditBillPaymentEntrance);

    CollectiveCreditBillsPaymentResultVO collectiveCreditBillPaymentByWallet(
            WalletCollectiveCreditBillPaymentEntranceVO walletCollectiveCreditBillPaymentEntrance);

    CreditBillsPaymentResultVO creditBillPaymentByCard(CardCreditBillPaymentEntranceVO cardCreditBillPaymentEntrance);

    CreditBillsPaymentResultVO creditBillPaymentByWallet(WalletCreditBillPaymentEntranceVO walletCreditBillPaymentEntrance);
}
