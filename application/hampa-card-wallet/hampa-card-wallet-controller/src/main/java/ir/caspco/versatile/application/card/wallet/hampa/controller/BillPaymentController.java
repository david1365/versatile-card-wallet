package ir.caspco.versatile.application.card.wallet.hampa.controller;

import ir.caspco.versatile.application.card.wallet.hampa.context.services.BillPaymentService;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.CardBillPaymentEntranceVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.BillPaymentResultVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.WalletBillPaymentEntranceVO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("${cardWallet.prefix}")
public class BillPaymentController {

    private final BillPaymentService billPaymentService;

    public BillPaymentController(BillPaymentService billPaymentService) {
        this.billPaymentService = billPaymentService;
    }

    @PreAuthorize("hasRole('ROLE_CW_BILL_PAYMENT_BY_CARD')")
    @PostMapping("/billPaymentByCard")
    public Mono<BillPaymentResultVO> BillPaymentByCard(@RequestBody @Valid CardBillPaymentEntranceVO billPaymentEntrance) {

        return Mono.just(billPaymentService.billPaymentByCard(billPaymentEntrance));
    }

    @PreAuthorize("hasRole('ROLE_CW_BILL_PAYMENT_BY_WALLET')")
    @PostMapping("/billPaymentByWallet")
    public Mono<BillPaymentResultVO> BillPaymentByWallet(@RequestBody @Valid WalletBillPaymentEntranceVO walletBillPaymentEntranceVO) {

        return Mono.just(billPaymentService.billPaymentByWallet(walletBillPaymentEntranceVO));
    }
}
