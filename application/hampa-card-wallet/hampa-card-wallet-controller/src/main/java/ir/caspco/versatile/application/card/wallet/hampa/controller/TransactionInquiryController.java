package ir.caspco.versatile.application.card.wallet.hampa.controller;

import ir.caspco.versatile.application.card.wallet.hampa.context.services.TranInquiryService;
import ir.caspco.versatile.jms.client.common.vo.RevertTranResponseVO;
import ir.caspco.versatile.jms.client.common.vo.hampa.RevertTransactionEntranceVO;
import ir.caspco.versatile.jms.client.common.vo.hampa.TranInqEntranceVO;
import ir.caspco.versatile.jms.client.common.vo.hampa.TranInqVO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;

/**
 * @author sadeghi
 * date:1400/09/02
 * TransactionInquiryService
 */

@RestController
@RequestMapping("${cardWallet.prefix}")
public class TransactionInquiryController {

    private final TranInquiryService tranInquiryService;


    public TransactionInquiryController(TranInquiryService tranInquiryService) {
        this.tranInquiryService = tranInquiryService;
    }

    @PreAuthorize("hasRole('ROLE_CW_TRAN_INQUIRY')")
    @PostMapping("/tranInquiry")
    public Mono<List<TranInqVO>> tranInquiry(@RequestBody @Valid TranInqEntranceVO tranInqEntranceVO) {

        return Mono.just(tranInquiryService.getTranInq(tranInqEntranceVO));
    }


    @PreAuthorize("hasRole('ROLE_CW_TRAN_REVERSE')")
    @PostMapping("/reverseTran")
    public Mono<RevertTranResponseVO> reverseTran(@RequestBody @Valid RevertTransactionEntranceVO revertTransactionEntranceVO) {

        return Mono.just(tranInquiryService.reverseTransaction(revertTransactionEntranceVO));
    }
}
