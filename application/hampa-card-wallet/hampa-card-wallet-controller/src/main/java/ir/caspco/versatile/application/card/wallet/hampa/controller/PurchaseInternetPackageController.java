package ir.caspco.versatile.application.card.wallet.hampa.controller;

import ir.caspco.versatile.application.card.wallet.hampa.context.services.PurchaseThirdPartyService;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.PurchaseInternetPackageByCardEntranceVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.PurchaseInternetPackageByWalletEntranceVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.PurchasePackageChargeResultVO;
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
public class PurchaseInternetPackageController {

    private final PurchaseThirdPartyService purchaseThirdPartyService;

    public PurchaseInternetPackageController(PurchaseThirdPartyService purchaseThirdPartyService) {

        this.purchaseThirdPartyService = purchaseThirdPartyService;
    }


    @PreAuthorize("hasRole('ROLE_CW_PURCHASE_INTERNET_PACKAGE_BY_CARD')")
    @PostMapping("/purchaseInternetPackageByCard")
    public Mono<PurchasePackageChargeResultVO> purchaseInternetPackageByCard(@RequestBody @Valid PurchaseInternetPackageByCardEntranceVO purchaseInternetPackageByCardEntrance) {

        return Mono.just(purchaseThirdPartyService.internetPackageByCard(purchaseInternetPackageByCardEntrance));
    }

    @PreAuthorize("hasRole('ROLE_CW_PURCHASE_INTERNET_PACKAGE_BY_WALLET')")
    @PostMapping("/purchaseInternetPackageByWallet")
    public Mono<PurchasePackageChargeResultVO> purchaseInternetPackageByWallet(@RequestBody @Valid PurchaseInternetPackageByWalletEntranceVO purchaseInternetPackageByWalletEntrance) {

        return Mono.just(purchaseThirdPartyService.internetPackageByWallet(purchaseInternetPackageByWalletEntrance));
    }
}
