package ir.caspco.versatile.application.card.wallet.hampa.controller;

import ir.caspco.versatile.application.card.wallet.hampa.context.services.PurchaseThirdPartyService;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.PurchaseCharityResultVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.PurchasePinChargeCharityByCardEntranceVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.PurchasePinChargeCharityByWalletEntranceVO;
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
public class PurchaseCharityController {

    private final PurchaseThirdPartyService purchaseThirdPartyService;


    public PurchaseCharityController(PurchaseThirdPartyService purchaseThirdPartyService) {
        this.purchaseThirdPartyService = purchaseThirdPartyService;
    }

    @PreAuthorize("hasRole('ROLE_CW_PURCHASE_CHARITY_BY_CARD')")
    @PostMapping("/purchaseCharityByCard")
    public Mono<PurchaseCharityResultVO> purchaseCharityByCard(@RequestBody @Valid PurchasePinChargeCharityByCardEntranceVO purchaseChargeEntranceVO) {

        return Mono.just(purchaseThirdPartyService.charityByCard(purchaseChargeEntranceVO));
    }

    @PreAuthorize("hasRole('ROLE_CW_PURCHASE_CHARITY_BY_WALLET')")
    @PostMapping("/purchaseCharityByWallet")
    public Mono<PurchaseCharityResultVO> purchaseCharityByWallet(@RequestBody @Valid PurchasePinChargeCharityByWalletEntranceVO purchaseChargeEntranceVO) {

        return Mono.just(purchaseThirdPartyService.charityByWallet(purchaseChargeEntranceVO));
    }
}
