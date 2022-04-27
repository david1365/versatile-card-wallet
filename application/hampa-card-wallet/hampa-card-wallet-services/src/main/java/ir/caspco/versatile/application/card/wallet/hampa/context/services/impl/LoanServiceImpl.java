package ir.caspco.versatile.application.card.wallet.hampa.context.services.impl;

import ir.caspco.versatile.application.card.wallet.hampa.context.domains.CardEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.domains.PayLoanLogEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.domains.PersonEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.domains.WalletEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.CardService;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.LoanService;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.PersonService;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.WalletService;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.log.PayLoanLogService;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.*;
import ir.caspco.versatile.common.util.Shift;
import ir.caspco.versatile.context.jms.client.exceptions.CoreException;
import ir.caspco.versatile.jms.client.common.client.ChangeBillToLoanClient;
import ir.caspco.versatile.jms.client.common.client.CreditLoanDetailClient;
import ir.caspco.versatile.jms.client.common.client.PayLoanClient;
import ir.caspco.versatile.jms.client.common.exceptions.CoreContentResultException;
import ir.caspco.versatile.jms.client.common.msg.PayLoanRequest;
import ir.caspco.versatile.jms.client.common.msg.PayLoanResponse;
import ir.caspco.versatile.jms.client.common.vo.ChangeToLoanVO;
import ir.caspco.versatile.jms.client.common.vo.LoanInfoVO;
import ir.caspco.versatile.jms.client.common.vo.LoanPaymentVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Service
@Transactional(noRollbackFor = CoreException.class)
public class LoanServiceImpl implements LoanService {

    private final ChangeBillToLoanClient changeBillToLoanClient;
    private final CreditLoanDetailClient creditLoanDetailClient;
    private final PayLoanClient payLoanClient;

    private final CardService cardService;
    private final PersonService personService;
    private final WalletService walletService;
    private final PayLoanLogService payLoanLogService;
    private final SegmentMapServiceImpl segmentMapService;


    public LoanServiceImpl(ChangeBillToLoanClient changeBillToLoanClient,
                           CreditLoanDetailClient creditLoanDetailClient,
                           PayLoanClient payLoanClient,

                           CardService cardService,
                           PersonService personService,
                           WalletService walletService,
                           PayLoanLogService payLoanLogService,
                           SegmentMapServiceImpl segmentMapService) {

        this.changeBillToLoanClient = changeBillToLoanClient;
        this.creditLoanDetailClient = creditLoanDetailClient;
        this.payLoanClient = payLoanClient;

        this.cardService = cardService;
        this.personService = personService;
        this.walletService = walletService;
        this.payLoanLogService = payLoanLogService;
        this.segmentMapService = segmentMapService;
    }

    @Override
    public boolean changeBillToLoanByCard(SegmentVO segment) {

        final CardEntity card = cardService.findByCardNumber(segment.getCardNumber());
        final CardEntity creditCard = cardService.findCreditCardByPerson(card.getPerson());

        final String creditSegmentCode = segmentMapService.toSegment(segment.getSegmentCode());

        return changeBillToLoanClient.changeBillToLoan(
                ChangeToLoanVO.builder()
                        .cardNumber(creditCard.getCardNumber())
                        .segmentCode(creditSegmentCode)
                        .build()
        );
    }

    @Override
    public boolean changeBillToLoanByWallet(UUID walletId) {

        final WalletEntity wallet = walletService.findByWalletIdAndStatusTrue(walletId);
        final String creditSegmentCode = segmentMapService.toSegment(wallet.getSegmentCode());

        final CardEntity creditCard = wallet.getCard();

        return changeBillToLoanClient.changeBillToLoan(
                ChangeToLoanVO.builder()
                        .cardNumber(creditCard.getCardNumber())
                        .segmentCode(creditSegmentCode)
                        .build()
        );
    }

    @Override
    public LoanInformationResultVO creditLoanDetailByCardNumber(String cardNumber) {

        final CardEntity card = cardService.findByCardNumber(cardNumber);

        return creditLoanDetail(card.getPerson());
    }

    @Override
    public LoanInformationResultVO creditLoanDetailByNationalCode(String nationalCode) {

        final PersonEntity person = personService.findByNationalCode(nationalCode);

        return creditLoanDetail(person);
    }

    @Override
    public LoanInformationResultVO creditLoanDetailByMobileNumber(String mobileNumber) {

        PersonEntity person = personService.findByMobileNumber(mobileNumber);

        return creditLoanDetail(person);
    }


    @Override
    public PayLoanResultVO payLoanByLoanNumber(LoanNumberLoanPaymentEntranceVO loanNumberLoanPaymentEntrance) {

        final WalletEntity wallet =
                walletService.findByLoanFileNumberAndStatusTrue(loanNumberLoanPaymentEntrance.getLoanNumber());

        return payLoan(loanNumberLoanPaymentEntrance, wallet);
    }

    @Override
    public PayLoanResultVO payLoanByCard(CardLoanPaymentEntranceVO cardLoanPaymentEntrance) {

        final CardEntity card = cardService.findByCardNumber(cardLoanPaymentEntrance.getCardNumber());
        final CardEntity creditCard = cardService.findCreditCardByPerson(card.getPerson());

        final WalletEntity wallet =
                walletService.findByCardNumberAndSegmentCodeAndStatusTrue(
                        creditCard.getCardNumber(),
                        cardLoanPaymentEntrance.getSegmentCode()
                );

        return payLoan(cardLoanPaymentEntrance, wallet);
    }

    @Override
    public PayLoanResultVO payLoanByWallet(WalletLoanPaymentEntranceVO walletLoanPaymentEntrance) {

        final WalletEntity wallet =
                walletService.findByWalletIdAndStatusTrue(walletLoanPaymentEntrance.getWalletId());

        return payLoan(walletLoanPaymentEntrance, wallet);
    }

    private LoanInformationResultVO creditLoanDetail(PersonEntity person) {

        CardEntity creditCard = cardService.findCreditCardByPerson(person);
        CardEntity debitCard = cardService.findDebitCardByPerson(person);

        List<LoanInfoVO> loanInfos = creditLoanDetailClient.creditLoanDetail(creditCard.getCardNumber());


        return LoanInformationResultVO.builder()
                .cardNumber(debitCard.getCardNumber())
                .loanInformation(
                        loanInfos.stream()
                                .map(loanInfo -> {

                                    LoanInformationVO loanInfoResult = Shift.just(loanInfo).toShift(LoanInformationVO.class).toObject();

                                    loanInfoResult.setFacilityCode(
                                            loanInfo.getFacility() != null ? loanInfo.getFacility().getFacilityCode() : null
                                    );

                                    //TODO from davood akbari:
                                    // In case of slowness, replace the method of reading the whole wallet and searching in it.
                                    WalletEntity wallet =
                                            walletService.findByLoanFileNumberAndStatusTrue(loanInfoResult.getLoanFileNumber());

                                    loanInfoResult.setSegmentCode(wallet.getSegmentCode());
                                    loanInfoResult.setWalletId(wallet.getWalletId());

                                    return loanInfoResult;

                                })
                                .collect(Collectors.toList())
                )
                .build();
    }

    private PayLoanResultVO payLoan(BaseLoanPaymentVO baseLoanPayment, WalletEntity wallet) {

        final String loanNumber = wallet.getLoanFileNumber().toString();

        final CardEntity card = wallet.getCard();
        final PersonEntity person = card.getPerson();

        LoanPaymentVO loanPayment = LoanPaymentVO.builder()
                .cif(person.getCustomerNumber())
                .loanNumber(loanNumber)
                .customDepositNumber(baseLoanPayment.getWithdrawalAccountNumber())
                .amount(baseLoanPayment.getAmount())
                .build();

        PayLoanRequest payLoanRequest = PayLoanRequest.builder()
                .loanPayment(loanPayment)
                .build();

        PayLoanLogEntity payLoanLog = payLoanLogService.create(loanPayment, wallet);

        PayLoanResponse payLoanResponse = payLoanClient.send(payLoanRequest)
                .onError(throwable -> payLoanLogService.fail(payLoanLog))
                .throwException()
                .retrieve()
                .result()
                .orElseThrow(CoreContentResultException::new);


        PayLoanResultVO payLoanResult = Shift.just(payLoanResponse)
                .toShift(PayLoanResultVO.class)
                .toObject();

        payLoanResult.setLoanFileNumber(loanNumber);
        payLoanResult.setCardNumber(card.getCardNumber());
        payLoanResult.setSegmentCode(wallet.getSegmentCode());
        payLoanResult.setWalletId(wallet.getWalletId());

        payLoanLogService.success(payLoanResult, payLoanLog);

        return payLoanResult;
    }
}
