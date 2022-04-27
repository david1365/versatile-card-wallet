package ir.caspco.versatile.application.card.wallet.hampa.controller;

import ir.caspco.versatile.application.card.wallet.hampa.context.services.BalanceService;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.CardSegmentBookBalanceEntranceVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.SegmentBookBalanceResultVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.WalletIdEntranceVO;
import ir.caspco.versatile.jms.client.common.vo.hampa.HampaSegmentBookBalanceResponseVO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@RestController
@RequestMapping("${cardWallet.prefix}")
public class BalanceController {

    private final BalanceService balanceService;


    public BalanceController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }


    @PreAuthorize("hasRole('ROLE_CW_CARD_BALANCE')")
    @PostMapping("/cardBalance")
    public Mono<List<SegmentBookBalanceResultVO>> cardBalance(
            @RequestBody @Valid CardSegmentBookBalanceEntranceVO cardSegmentBookBalanceEntrance) {

        return Mono.just(balanceService.cardBalance(cardSegmentBookBalanceEntrance));

    }

    @PreAuthorize("hasRole('ROLE_CW_WALLET_BALANCE')")
    @PostMapping("/walletBalance")
    public Mono<HampaSegmentBookBalanceResponseVO> walletBalance(@RequestBody @Valid WalletIdEntranceVO walletIdEntrance) {

        return Mono.just(balanceService.walletBalance(walletIdEntrance));
    }
}
