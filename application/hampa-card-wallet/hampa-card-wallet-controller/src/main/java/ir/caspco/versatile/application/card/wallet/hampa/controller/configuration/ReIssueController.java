package ir.caspco.versatile.application.card.wallet.hampa.controller.configuration;


import ir.caspco.versatile.application.card.wallet.hampa.context.services.ReIssueService;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.ReIssueEntranceVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.ReIssueResultVO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("${cardWallet.prefix}")
public class ReIssueController {

    private final ReIssueService reIssueService;


    public ReIssueController(ReIssueService reIssueService) {

        this.reIssueService = reIssueService;
    }


    @PreAuthorize("hasRole('ROLE_CW_REISSUE')")
    @PostMapping("/reIssue")
    public Mono<ReIssueResultVO> reIssue(@RequestBody @Valid ReIssueEntranceVO reIssueEntrance) {

        return Mono.just(reIssueService.reIssue(reIssueEntrance.getCardNumber()));
    }
}
