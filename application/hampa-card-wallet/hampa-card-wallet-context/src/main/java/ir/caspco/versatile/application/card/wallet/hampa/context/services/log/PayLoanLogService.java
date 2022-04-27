package ir.caspco.versatile.application.card.wallet.hampa.context.services.log;

import ir.caspco.versatile.application.card.wallet.hampa.context.domains.PayLoanLogEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.domains.WalletEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.PayLoanResultVO;
import ir.caspco.versatile.jms.client.common.vo.LoanPaymentVO;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

public interface PayLoanLogService {

    PayLoanLogEntity create(LoanPaymentVO loanPayment, WalletEntity wallet);

    PayLoanLogEntity fail(PayLoanLogEntity payLoanLog);

    PayLoanLogEntity success(PayLoanResultVO payLoanResult, PayLoanLogEntity payLoanLog);
}
