package ir.caspco.versatile.application.card.wallet.hampa.context.services;

import ir.caspco.versatile.application.card.wallet.hampa.context.vo.*;

import java.util.UUID;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

public interface LoanService {

    boolean changeBillToLoanByCard(SegmentVO segment);

    boolean changeBillToLoanByWallet(UUID walletId);

    LoanInformationResultVO creditLoanDetailByCardNumber(String cardNumber);

    LoanInformationResultVO creditLoanDetailByNationalCode(String nationalCode);

    LoanInformationResultVO creditLoanDetailByMobileNumber(String mobileNumber);

    PayLoanResultVO payLoanByLoanNumber(LoanNumberLoanPaymentEntranceVO loanNumberLoanPaymentEntrance);

    PayLoanResultVO payLoanByCard(CardLoanPaymentEntranceVO cardLoanPaymentEntrance);

    PayLoanResultVO payLoanByWallet(WalletLoanPaymentEntranceVO walletLoanPaymentEntrance);
}
