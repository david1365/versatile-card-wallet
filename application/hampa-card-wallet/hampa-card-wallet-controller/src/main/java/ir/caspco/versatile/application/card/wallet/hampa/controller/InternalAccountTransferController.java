package ir.caspco.versatile.application.card.wallet.hampa.controller;

import ir.caspco.versatile.application.card.wallet.hampa.context.services.InternalAccountTransferService;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.CardInternalAccountTransferEntranceVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.MobileInternalAccountTransferEntranceVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.TransferResultVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.WalletInternalAccountTransferEntranceVO;
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
public class InternalAccountTransferController {

    private final InternalAccountTransferService internalAccountTransferService;


    public InternalAccountTransferController(InternalAccountTransferService internalAccountTransferService) {
        this.internalAccountTransferService = internalAccountTransferService;
    }


    @PreAuthorize("hasRole('ROLE_CW_TRANSFER_BY_CARD')")
    @PostMapping("/transferByCard")
    public Mono<TransferResultVO> transferByCard(
            @RequestBody @Valid CardInternalAccountTransferEntranceVO cardInternalAccountTransferEntrance
    ) {

        return Mono.just(internalAccountTransferService.transferByCard(cardInternalAccountTransferEntrance));
    }

    @PreAuthorize("hasRole('ROLE_CW_TRANSFER_BY_WALLET')")
    @PostMapping("/transferByWallet")
    public Mono<TransferResultVO> transferByWallet(
            @RequestBody @Valid WalletInternalAccountTransferEntranceVO walletInternalAccountTransferEntrance
    ) {

        return Mono.just(internalAccountTransferService.transferByWallet(walletInternalAccountTransferEntrance));
    }

    @PreAuthorize("hasRole('ROLE_CW_TRANSFER_BY_MOBILE')")
    @PostMapping("/transferByMobile")
    public Mono<TransferResultVO> transferByMobile(
            @RequestBody @Valid MobileInternalAccountTransferEntranceVO mobileInternalAccountTransferEntrance
    ) {

        return Mono.just(internalAccountTransferService.transferByMobile(mobileInternalAccountTransferEntrance));
    }
}
