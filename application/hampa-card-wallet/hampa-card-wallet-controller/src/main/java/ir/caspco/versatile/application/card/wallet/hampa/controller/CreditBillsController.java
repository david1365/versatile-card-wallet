package ir.caspco.versatile.application.card.wallet.hampa.controller;

import ir.caspco.versatile.application.card.wallet.hampa.context.services.CreditBillsService;
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
public class CreditBillsController {

    private final CreditBillsService creditBillsService;


    public CreditBillsController(CreditBillsService creditBillsService) {

        this.creditBillsService = creditBillsService;
    }

    @PreAuthorize("hasRole('ROLE_CW_CREDIT_BILLS_BY_CARD_NUMBER')")
    @GetMapping("/creditBillsByCardNumber/{cardNumber}")
    public Mono<CreditBillsResultVO> creditBillsByCardNumber(
            @NotEmpty @NotBlank @NotEmpty @CheckCreditByCard @PathVariable String cardNumber) {

        return Mono.just(creditBillsService.creditBillsByCardNumber(cardNumber));
    }

    @PreAuthorize("hasRole('ROLE_CW_CREDIT_BILLS_BY_NATIONAL_CODE')")
    @GetMapping("/creditBillsByNationalCode/{nationalCode}")
    public Mono<CreditBillsResultVO> creditBillsByNationalCode(
            @NotEmpty @NotBlank @NotEmpty @PathVariable @IsValidNationalCode String nationalCode) {

        return Mono.just(creditBillsService.creditBillsByNationalCode(nationalCode));
    }

    @PreAuthorize("hasRole('ROLE_CW_CREDIT_BILLS_BY_MOBILE_NUMBERE')")
    @GetMapping("/creditBillsByMobileNumber/{mobileNumber}")
    public Mono<CreditBillsResultVO> creditBillsByMobileNumber(
            @NotEmpty @NotBlank @NotEmpty @PathVariable @IsValidNationalCode String mobileNumber) {

        return Mono.just(creditBillsService.creditBillsByMobileNumber(mobileNumber));
    }

    @PreAuthorize("hasRole('ROLE_CW_LOAD_BILLS_BY_CARD_NUMBER')")
    @GetMapping("/loadBillsByCardNumber/{cardNumber}")
    public Mono<CreditCardBillResultVO> loadBillsByCardNumber(
            @NotEmpty @NotBlank @NotEmpty @CheckCreditByCard @PathVariable String cardNumber) {

        return Mono.just(creditBillsService.loadBillsByCardNumber(cardNumber));
    }

    @PreAuthorize("hasRole('ROLE_CW_LOAD_BILLS_BY_NATIONAL_CODE')")
    @GetMapping("/loadBillsByNationalCode/{nationalCode}")
    public Mono<CreditCardBillResultVO> loadBillsByNationalCode(
            @NotEmpty @NotBlank @NotEmpty @PathVariable @IsValidNationalCode String nationalCode) {

        return Mono.just(creditBillsService.loadBillsByNationalCode(nationalCode));
    }

    @PreAuthorize("hasRole('ROLE_CW_LOAD_BILLS_BY_MOBILE_NUMBER')")
    @GetMapping("/loadBillsByMobileNumber/{mobileNumber}")
    public Mono<CreditCardBillResultVO> loadBillsByMobileNumber(
            @NotEmpty @NotBlank @NotEmpty @PathVariable @IsValidNationalCode String mobileNumber) {

        return Mono.just(creditBillsService.loadBillsByMobileNumber(mobileNumber));
    }

    @PreAuthorize("hasRole('ROLE_CW_CREDIT_BILL_PAYMENT_BY_CARD')")
    @PostMapping("/creditBillPaymentByCard")
    public Mono<CreditBillsPaymentResultVO> creditBillPaymentByCard(
            @RequestBody @Valid CardCreditBillPaymentEntranceVO cardCreditBillPaymentEntrance) {

        return Mono.just(creditBillsService.creditBillPaymentByCard(cardCreditBillPaymentEntrance));
    }

    @PreAuthorize("hasRole('ROLE_CW_CREDIT_BILL_PAYMENT_BY_WALLET')")
    @PostMapping("/creditBillPaymentByWallet")
    public Mono<CreditBillsPaymentResultVO> creditBillPaymentByWallet(
            @RequestBody @Valid WalletCreditBillPaymentEntranceVO walletCreditBillPaymentEntrance) {

        return Mono.just(creditBillsService.creditBillPaymentByWallet(walletCreditBillPaymentEntrance));
    }

    @PreAuthorize("hasRole('ROLE_CW_COLLECTIVE_CREDIT_BILL_PAYMENT_BY_CARD')")
    @PostMapping("/collectiveCreditBillPaymentByCard")
    public Mono<CollectiveCreditBillsPaymentResultVO> collectiveCreditBillPaymentByCard(
            @RequestBody @Valid CardCollectiveCreditBillPaymentEntranceVO cardCollectiveCreditBillPaymentEntrance) {

        return Mono.just(creditBillsService.collectiveCreditBillPaymentByCard(cardCollectiveCreditBillPaymentEntrance));
    }

    @PreAuthorize("hasRole('ROLE_CW_COLLECTIVE_CREDIT_BILL_PAYMENT_BY_WALLET')")
    @PostMapping("/collectiveCreditBillPaymentByWallet")
    public Mono<CollectiveCreditBillsPaymentResultVO> collectiveCreditBillPaymentByWallet(
            @RequestBody @Valid WalletCollectiveCreditBillPaymentEntranceVO walletCollectiveCreditBillPaymentEntrance) {

        return Mono.just(creditBillsService.collectiveCreditBillPaymentByWallet(walletCollectiveCreditBillPaymentEntrance));
    }
}
