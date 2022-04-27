package ir.caspco.versatile.application.card.wallet.hampa.controller;


import ir.caspco.versatile.application.card.wallet.hampa.context.services.AssignSegmentCodeService;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.AssignSegmentCodeEntranceVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.CardResultVO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("${cardWallet.prefix}")
public class AssignSegmentCodeController {

    private final AssignSegmentCodeService assignSegmentCodeService;

    public AssignSegmentCodeController(AssignSegmentCodeService assignSegmentCodeService) {
        this.assignSegmentCodeService = assignSegmentCodeService;
    }

    @PreAuthorize("hasRole('ROLE_CW_ASSIGN_SEGMENT')")
    @PostMapping("/assignSegment")
    public Mono<CardResultVO> assignSegmentCode(@RequestBody @Valid AssignSegmentCodeEntranceVO assignSegmentCodeEntranceVO) {

        return Mono.just(assignSegmentCodeService.assignSegmentCode(assignSegmentCodeEntranceVO));
    }
}
