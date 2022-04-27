package ir.caspco.versatile.application.card.wallet.hampa.controller;

import ir.caspco.versatile.application.card.wallet.hampa.context.services.PurchaseThirdPartyService;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.*;
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
 * @author Mohammad Javad Zahedi - 2021
 * mjavadzahedi0@gmail.com
 * 09377732642
 */


@RestController
@RequestMapping("${cardWallet.prefix}")
public class PurchaseChargeController {

    private final PurchaseThirdPartyService purchaseThirdPartyService;

    public PurchaseChargeController(PurchaseThirdPartyService purchaseThirdPartyService) {

        this.purchaseThirdPartyService = purchaseThirdPartyService;
    }


    @PreAuthorize("hasRole('ROLE_CW_PURCHASE_DIRECT_CHARGE_BY_CARD')")
    @PostMapping("/purchaseDirectChargeByCard")
    public Mono<PurchasePackageChargeResultVO> purchaseDirectChargeByCard(@RequestBody @Valid PurchaseDirectChargeByCardEntranceVO purchaseChargeEntranceVO) {

        return Mono.just(purchaseThirdPartyService.directChargeByCard(purchaseChargeEntranceVO));
    }

    @PreAuthorize("hasRole('ROLE_CW_PURCHASE_DIRECT_CHARGE_BY_WALLET')")
    @PostMapping("/purchaseDirectChargeByWallet")
    public Mono<PurchasePackageChargeResultVO> purchaseDirectChargeByWallet(@RequestBody @Valid PurchaseDirectChargeByWalletEntranceVO chargeWalletEntrance) {

        return Mono.just(purchaseThirdPartyService.directChargeByWallet(chargeWalletEntrance));
    }

    @PreAuthorize("hasRole('ROLE_CW_PURCHASE_PIN_CHARGE_BY_CARD')")
    @PostMapping("/purchasePinChargeByCard")
    public Mono<PurchasePinChargeResultVO> purchasePinChargeByCard(@RequestBody @Valid PurchasePinChargeCharityByCardEntranceVO purchaseByCardEntrance) {

        return Mono.just(purchaseThirdPartyService.pinChargeByCard(purchaseByCardEntrance));
    }

    @PreAuthorize("hasRole('ROLE_CW_PURCHASE_PIN_CHARGE_BY_WALLET')")
    @PostMapping("/purchasePinChargeByWallet")
    public Mono<PurchasePinChargeResultVO> purchasePinChargeByWallet(@RequestBody @Valid PurchasePinChargeCharityByWalletEntranceVO purchaseByWalletEntrance) {

        return Mono.just(purchaseThirdPartyService.pinChargeByWallet(purchaseByWalletEntrance));
    }
}
