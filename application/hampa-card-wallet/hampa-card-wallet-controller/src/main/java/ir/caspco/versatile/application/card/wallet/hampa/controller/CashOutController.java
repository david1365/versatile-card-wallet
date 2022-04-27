package ir.caspco.versatile.application.card.wallet.hampa.controller;

import ir.caspco.versatile.application.card.wallet.hampa.context.services.CashOutService;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.CashOutCardEntranceVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.CashOutResultVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.CashOutWalletEntranceVO;
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
public class CashOutController {

    private final CashOutService cashOutService;


    public CashOutController(CashOutService cashOutService) {
        this.cashOutService = cashOutService;
    }


    @PreAuthorize("hasRole('ROLE_CW_CASHOUT_CARD')")
    @PostMapping("/cashOutCard")
    public Mono<CashOutResultVO> cashOutCard(@RequestBody @Valid CashOutCardEntranceVO cashOutCardEntrance) {

        return Mono.just(cashOutService.cashOutCard(cashOutCardEntrance));
    }

    @PreAuthorize("hasRole('ROLE_CW_CASHOUT_WALLET')")
    @PostMapping("/cashOutWallet")
    public Mono<CashOutResultVO> cashOutWallet(@RequestBody @Valid CashOutWalletEntranceVO cashOutWalletEntrance) {

        return Mono.just(cashOutService.cashOutWallet(cashOutWalletEntrance));
    }
}
