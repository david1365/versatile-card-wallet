package ir.caspco.versatile.application.card.wallet.hampa.controller;

import ir.caspco.versatile.application.card.wallet.hampa.context.services.StatementService;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.CardStatementEntranceVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.StatementResultVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.WalletStatementEntranceVO;
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
public class StatementController {

    private final StatementService statementService;


    public StatementController(StatementService statementService) {
        this.statementService = statementService;
    }


    @PreAuthorize("hasRole('ROLE_CW_CARD_STATEMENT')")
    @PostMapping("/cardStatement")
    public Mono<StatementResultVO> cardStatement(@RequestBody @Valid CardStatementEntranceVO cardStatementEntrance) {

        return Mono.just(statementService.cardStatement(cardStatementEntrance));
    }

    @PreAuthorize("hasRole('ROLE_CW_WALLET_STATEMENT')")
    @PostMapping("/walletStatement")
    public Mono<StatementResultVO> walletStatement(@RequestBody @Valid WalletStatementEntranceVO walletStatementEntrance) {

        return Mono.just(statementService.walletStatement(walletStatementEntrance));
    }
}
