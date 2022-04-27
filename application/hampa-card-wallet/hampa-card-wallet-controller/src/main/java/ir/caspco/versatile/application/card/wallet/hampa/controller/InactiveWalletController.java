package ir.caspco.versatile.application.card.wallet.hampa.controller;


import ir.caspco.versatile.application.card.wallet.hampa.context.services.InactiveWalletService;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.InactiveCardWalletResultVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.InactiveWalletByCardEntranceVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.WalletIdEntranceVO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("${cardWallet.prefix}")
public class InactiveWalletController {

    private final InactiveWalletService inactiveWalletService;


    public InactiveWalletController(InactiveWalletService inactiveWalletService) {
        this.inactiveWalletService = inactiveWalletService;
    }

    @PreAuthorize("hasRole('ROLE_CW_INACTIVE_WALLET_BY_CARD')")
    @PostMapping("/inactiveWalletByCard")
    public Mono<InactiveCardWalletResultVO> inactiveWalletByCard(
            @RequestBody @Valid InactiveWalletByCardEntranceVO inactiveWalletByCardEntranceVO) {

        return Mono.just(inactiveWalletService.inactiveByCard(inactiveWalletByCardEntranceVO));
    }

    @PreAuthorize("hasRole('ROLE_CW_INACTIVE_WALLET_BY_WALLET')")
    @PostMapping("/inactiveWalletByWallet")
    public Mono<InactiveCardWalletResultVO> inactiveWalletByWallet(@RequestBody @Valid WalletIdEntranceVO walletIdEntrance) {

        return Mono.just(inactiveWalletService.inactiveByWallet(walletIdEntrance));
    }
}
