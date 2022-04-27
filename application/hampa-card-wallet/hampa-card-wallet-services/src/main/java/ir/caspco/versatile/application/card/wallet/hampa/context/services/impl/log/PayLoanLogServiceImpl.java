package ir.caspco.versatile.application.card.wallet.hampa.context.services.impl.log;

import ir.caspco.versatile.application.card.wallet.hampa.context.domains.CardEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.domains.PayLoanLogEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.domains.WalletEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.repositories.PayLoanLogRepository;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.log.PayLoanLogService;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.PayLoanResultVO;
import ir.caspco.versatile.context.enums.FlowStatus;
import ir.caspco.versatile.jms.client.common.vo.LoanPaymentVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Service
@Transactional(noRollbackFor = Exception.class)
public class PayLoanLogServiceImpl implements PayLoanLogService {

    private final PayLoanLogRepository payLoanLogRepository;


    public PayLoanLogServiceImpl(PayLoanLogRepository payLoanLogRepository) {

        this.payLoanLogRepository = payLoanLogRepository;
    }


    @Override
    public PayLoanLogEntity create(LoanPaymentVO loanPayment, WalletEntity wallet) {

        final CardEntity card = wallet.getCard();

        return payLoanLogRepository.saveAndFlush(
                PayLoanLogEntity.builder()
                        .cardNumber(card.getCardNumber())
                        .segmentCode(wallet.getSegmentCode())
                        .walletId(wallet.getWalletId())
                        .customerNumber(loanPayment.getCif())
                        .amount(loanPayment.getAmount())
                        .withdrawalAccountNumber(loanPayment.getCustomDepositNumber())
                        .paymentMethod(loanPayment.getPaymentMethod())
                        .loanNumber(loanPayment.getLoanNumber())
                        .build()
        );
    }

    @Override
    public PayLoanLogEntity fail(PayLoanLogEntity payLoanLog) {

        payLoanLog.setFlowStatus(FlowStatus.FAIL);
        return payLoanLogRepository.saveAndFlush(payLoanLog);
    }

    @Override
    public PayLoanLogEntity success(PayLoanResultVO payLoanResult, PayLoanLogEntity payLoanLog) {

        payLoanLog.setAppliedAmount(payLoanResult.getAppliedAmount());
        payLoanLog.setDocumentNumber(payLoanResult.getDocumentNumber());
        payLoanLog.setDocumentTitle(payLoanResult.getDocumentTitle());
        payLoanLog.setPaymentTimeStamp(payLoanResult.getPaymentTimeStamp());
        payLoanLog.setFlowStatus(FlowStatus.DONE);

        return payLoanLogRepository.saveAndFlush(payLoanLog);
    }
}
