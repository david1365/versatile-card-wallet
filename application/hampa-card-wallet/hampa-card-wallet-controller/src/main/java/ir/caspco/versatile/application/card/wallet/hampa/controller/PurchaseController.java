package ir.caspco.versatile.application.card.wallet.hampa.controller;

import ir.caspco.versatile.application.card.wallet.hampa.context.services.PurchaseService;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.CardExchangesEntranceVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.PurchaseResultVO;
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
public class PurchaseController {

    private final PurchaseService purchaseService;


    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }


    @PreAuthorize("hasRole('ROLE_CW_PURCHASE_BY_CARD')")
    @PostMapping("/purchaseByCard")
    public Mono<PurchaseResultVO> purchaseByCard(@RequestBody @Valid CardExchangesEntranceVO cardExchangesEntrance) {

        return Mono.just(purchaseService.purchaseByCard(cardExchangesEntrance));
    }

    @PreAuthorize("hasRole('ROLE_CW_PURCHASE_BY_WALLET')")
    @PostMapping("/purchaseByWallet")
    public Mono<PurchaseResultVO> purchaseByWallet(@RequestBody @Valid WalletExchangesEntranceVO walletExchangesEntrance) {

        return Mono.just(purchaseService.purchaseByWallet(walletExchangesEntrance));
    }
}
