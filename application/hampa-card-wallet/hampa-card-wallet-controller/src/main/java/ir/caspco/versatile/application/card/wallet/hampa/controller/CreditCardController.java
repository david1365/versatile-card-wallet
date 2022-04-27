package ir.caspco.versatile.application.card.wallet.hampa.controller;

import ir.caspco.versatile.application.card.wallet.hampa.context.services.CreditCardService;
import ir.caspco.versatile.application.card.wallet.hampa.context.validation.annotations.HasRegistered;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.CreditCardInquireResultVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.CreditCardRegistrationEntranceVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.CreditCardRegistrationResultVO;
import ir.caspco.versatile.common.validation.annotations.IsValidNationalCode;
import ir.caspco.versatile.jms.client.common.vo.SegmentCompanyPlanVO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@RestController
@RequestMapping("${cardWallet.prefix}")
public class CreditCardController {

    private final CreditCardService creditCardService;


    public CreditCardController(CreditCardService creditCardService) {
        this.creditCardService = creditCardService;
    }


    @PreAuthorize("hasRole('ROLE_CW_LOAD_COMPANY_PLAN')")
    @GetMapping("/loadCompanyPlan")
    public Mono<List<SegmentCompanyPlanVO>> loadCompanyPlan() {

        return Mono.just(creditCardService.loadCompanyPlan());
    }

    @PreAuthorize("hasRole('ROLE_CW_REGISTER_CREDIT_CARD')")
    @PostMapping("/registerCreditCard")
    public Mono<CreditCardRegistrationResultVO> registerCreditCard(
            @RequestBody @Valid CreditCardRegistrationEntranceVO creditCardRegistrationEntrance) {

        return Mono.just(creditCardService.registerCreditCard(creditCardRegistrationEntrance));

    }

    @PreAuthorize("hasRole('ROLE_CW_INQUIRE_CREDIT_CARD')")
    @GetMapping("/inquireCreditCard/{nationalCode}")
    public Mono<CreditCardInquireResultVO> inquireCreditCard(
            @NotEmpty @NotBlank @NotEmpty @IsValidNationalCode @HasRegistered @PathVariable String nationalCode
    ) {

        return Mono.just(creditCardService.inquireCreditCard(nationalCode));
    }
}
