package ir.caspco.versatile.application.card.wallet.hampa.controller;

import ir.caspco.versatile.application.card.wallet.hampa.context.services.CardService;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.RegisterService;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.*;
import ir.caspco.versatile.jms.client.common.vo.hampa.HampaCardListRequestVO;
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
public class RegisterController {

    private final RegisterService registerService;
    private final CardService cardService;


    public RegisterController(RegisterService registerService, CardService cardService) {

        this.registerService = registerService;
        this.cardService = cardService;
    }

    @PreAuthorize("hasRole('ROLE_CW_LIST')")
    @PostMapping("/list")
    public Mono<HampaCardResultVO> list(@RequestBody @Valid HampaCardListRequestVO hampaCardListRequest) {

        return Mono.just(registerService.list(hampaCardListRequest));
    }

    @PreAuthorize("hasRole('ROLE_CW_REGISTER_WITH_CUSTOMER_INFO')")
    @PostMapping("/registerWithCustomerInfo")
    public Mono<CardResultVO> register(@RequestBody @Valid CaspianCardWalletEntranceVO caspianCardWalletEntrance) {

        return Mono.just(registerService.register(caspianCardWalletEntrance));
    }

    @PreAuthorize("hasRole('ROLE_CW_REGISTER')")
    @PostMapping("/register")
    public Mono<CardResultVO> register(@RequestBody @Valid CardWalletEntranceVO cardWalletEntrance) {

        return Mono.just(registerService.register(cardWalletEntrance));
    }

    @PreAuthorize("hasRole('ROLE_CW_CARD_INFO')")
    @GetMapping("/cardInfo/{cardNumber}")
    public Mono<EncryptedCardInfoResultVO> cardInfo(@NotEmpty @NotBlank @NotEmpty @PathVariable String cardNumber) {

        return Mono.just(cardService.cardInfo(cardNumber));
    }
}
