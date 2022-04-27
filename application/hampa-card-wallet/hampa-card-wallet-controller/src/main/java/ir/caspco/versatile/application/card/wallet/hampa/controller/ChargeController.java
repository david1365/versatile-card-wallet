package ir.caspco.versatile.application.card.wallet.hampa.controller;

import ir.caspco.versatile.application.card.wallet.hampa.context.services.ChargeService;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.CardExchangesEntranceVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.ExchangesResultVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.WalletExchangesEntranceVO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@RestController
@RequestMapping("${cardWallet.prefix}")
public class ChargeController {

    private final ChargeService chargeService;


    public ChargeController(ChargeService chargeService) {
        this.chargeService = chargeService;
    }


    @PreAuthorize("hasRole('ROLE_CW_CHARGE_THE_CARD')")
    @PostMapping("/chargeTheCard")
    public Mono<ExchangesResultVO> chargeTheCard(@RequestBody @Valid CardExchangesEntranceVO chargeTheCardEntrance) {

        return Mono.just(chargeService.chargeTheCard(chargeTheCardEntrance));
    }

    @PreAuthorize("hasRole('ROLE_CW_CHARGE_THE_WALLET')")
    @PostMapping("/chargeTheWallet")
    public Mono<ExchangesResultVO> chargeTheWallet(@RequestBody @Valid WalletExchangesEntranceVO chargeWalletEntrance) {

        return Mono.just(chargeService.chargeTheWallet(chargeWalletEntrance));
    }
}
