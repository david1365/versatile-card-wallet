package ir.caspco.versatile.application.card.wallet.hampa.controller;

import ir.caspco.versatile.application.card.wallet.hampa.context.services.LoanService;
import ir.caspco.versatile.application.card.wallet.hampa.context.validation.annotations.CheckCreditByCard;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.*;
import ir.caspco.versatile.common.validation.annotations.IsValidNationalCode;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@RestController
@RequestMapping("${cardWallet.prefix}")
public class LoanController {

    private final LoanService loanService;


    public LoanController(LoanService loanService) {

        this.loanService = loanService;
    }

    @PreAuthorize("hasRole('ROLE_CW_CREDIT_LOAN_DETAIL_BY_CARD_NUMBER')")
    @GetMapping("/creditLoanDetailByCardNumber/{cardNumber}")
    public Mono<LoanInformationResultVO> creditLoanDetailByCardNumber(
            @NotEmpty @NotBlank @NotEmpty @CheckCreditByCard @PathVariable String cardNumber) {

        return Mono.just(loanService.creditLoanDetailByCardNumber(cardNumber));
    }

    @PreAuthorize("hasRole('ROLE_CW_CREDIT_LOAN_DETAIL_BY_NATIONAL_CODE')")
    @GetMapping("/creditLoanDetailByNationalCode/{nationalCode}")
    public Mono<LoanInformationResultVO> creditLoanDetailByNationalCode(
            @NotEmpty @NotBlank @NotEmpty @PathVariable @IsValidNationalCode String nationalCode) {

        return Mono.just(loanService.creditLoanDetailByNationalCode(nationalCode));
    }

    @PreAuthorize("hasRole('ROLE_CW_CREDIT_LOAN_DETAIL_BY_MOBILE_NUMBER')")
    @GetMapping("/creditLoanDetailByMobileNumber/{mobileNumber}")
    public Mono<LoanInformationResultVO> creditLoanDetailByMobileNumber(
            @NotEmpty @NotBlank @NotEmpty @PathVariable @IsValidNationalCode String mobileNumber) {

        return Mono.just(loanService.creditLoanDetailByMobileNumber(mobileNumber));
    }


    @PreAuthorize("hasRole('ROLE_CW_PAY_LOAN_BY_LOAN_NUMBER')")
    @PostMapping("/payLoanByLoanNumber")
    public Mono<PayLoanResultVO> payLoanByLoanNumber(
            @RequestBody @Valid LoanNumberLoanPaymentEntranceVO loanNumberLoanPaymentEntrance) {

        return Mono.just(loanService.payLoanByLoanNumber(loanNumberLoanPaymentEntrance));
    }

    @PreAuthorize("hasRole('ROLE_CW_PAY_LOAN_BY_CARD')")
    @PostMapping("/payLoanByCard")
    public Mono<PayLoanResultVO> payLoanByCard(
            @RequestBody @Valid CardLoanPaymentEntranceVO cardLoanPaymentEntrance) {

        return Mono.just(loanService.payLoanByCard(cardLoanPaymentEntrance));
    }

    @PreAuthorize("hasRole('ROLE_CW_PAY_LOAN_BY_WALLET')")
    @PostMapping("/payLoanByWallet")
    public Mono<PayLoanResultVO> payLoanByWallet(
            @RequestBody @Valid WalletLoanPaymentEntranceVO walletLoanPaymentEntrance) {

        return Mono.just(loanService.payLoanByWallet(walletLoanPaymentEntrance));
    }

    @PreAuthorize("hasRole('ROLE_CW_CHANGE_BILL_TO_LOAN_BY_CARD')")
    @PostMapping("/changeBillToLoanByCard")
    public Mono<Boolean> changeBillToLoanByCard(@RequestBody @Valid SegmentVO segment) {

        return Mono.just(loanService.changeBillToLoanByCard(segment));
    }

    @PreAuthorize("hasRole('ROLE_CW_CHANGE_BILL_TO_LOAN_BY_WALLET')")
    @PostMapping("/changeBillToLoanByWallet")
    public Mono<Boolean> changeBillToLoanByWallet(@RequestBody @Valid WalletIdEntranceVO walletIdEntrance) {

        return Mono.just(loanService.changeBillToLoanByWallet(walletIdEntrance.getWalletId()));
    }
}
