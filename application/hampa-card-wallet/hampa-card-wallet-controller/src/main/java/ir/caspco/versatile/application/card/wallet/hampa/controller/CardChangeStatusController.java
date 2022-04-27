package ir.caspco.versatile.application.card.wallet.hampa.controller;

import ir.caspco.versatile.application.card.wallet.hampa.context.services.CardService;
import ir.caspco.versatile.jms.client.common.msg.hampa.CardChangeStatusRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("${cardWallet.prefix}")
public class CardChangeStatusController {

    private final CardService cardService;

    public CardChangeStatusController(CardService cardService) {

        this.cardService = cardService;
    }


    @PreAuthorize("hasRole('ROLE_CW_CARD_CHANGE_STATUS')")
    @PostMapping("/cardChangeStatus")
    public Mono<Boolean> CardChangeStatus(@RequestBody @Valid CardChangeStatusRequest cardChangeStatusRequest) {

        return Mono.just(cardService.cardChangeStatus(cardChangeStatusRequest));
    }

    @PreAuthorize("hasRole('ROLE_CW_ACTIVE_HAMPA_CARD')")
    @PostMapping("/activeHampaCard")
    public Mono<Boolean> activeHampaCard(@RequestBody @Valid CardChangeStatusRequest cardChangeStatusRequest) {

        return Mono.just(cardService.activeHampaCard(cardChangeStatusRequest));
    }
}
