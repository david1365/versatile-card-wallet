package ir.caspco.versatile.application.card.wallet.hampa.context.services.impl;

import ir.caspco.versatile.application.card.wallet.hampa.context.domains.WalletEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.repositories.WalletRepository;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.InactiveWalletService;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.WalletService;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.InactiveCardWalletResultVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.InactiveWalletByCardEntranceVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.WalletIdEntranceVO;
import ir.caspco.versatile.jms.client.common.client.hampa.InactiveWalletClient;
import ir.caspco.versatile.jms.client.common.exceptions.CoreContentResultException;
import ir.caspco.versatile.jms.client.common.msg.hampa.InactiveWalletRequest;
import ir.caspco.versatile.jms.client.common.msg.hampa.InactiveWalletResponse;
import ir.caspco.versatile.jms.client.common.vo.hampa.InactiveWalletEntranceVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class InactiveWalletServiceImpl implements InactiveWalletService {


    private final InactiveWalletClient inactiveWalletClient;

    private final WalletService walletService;

    private final WalletRepository walletRepository;

    public InactiveWalletServiceImpl(InactiveWalletClient inactiveWalletClient,
                                     WalletService walletService,
                                     WalletRepository walletRepository) {

        this.inactiveWalletClient = inactiveWalletClient;
        this.walletService = walletService;
        this.walletRepository = walletRepository;
    }

    @Override
    public InactiveCardWalletResultVO inactive(InactiveWalletEntranceVO inactiveWalletEntranceVO) {

        InactiveWalletRequest inactiveWalletRequest = InactiveWalletRequest.builder()
                .inactiveWalletEntranceVO(
                        InactiveWalletEntranceVO.builder()
                                .accountNumber(inactiveWalletEntranceVO.getAccountNumber())
                                .cardNumber(inactiveWalletEntranceVO.getCardNumber())
                                .segmentCode(inactiveWalletEntranceVO.getSegmentCode())
                                .build()
                )
                .build();

        InactiveWalletResponse response = inactiveWalletClient.send(inactiveWalletRequest)
                // .throwException()
                .retrieve()
                .result()
                .orElseThrow(CoreContentResultException::new);


        WalletEntity wallet = walletService.findByCardNumberAndSegmentCodeAndStatusTrue(
                inactiveWalletEntranceVO.getCardNumber(),
                inactiveWalletEntranceVO.getSegmentCode()
        );


        wallet.setStatus(false);
        wallet = walletRepository.save(wallet);

        return InactiveCardWalletResultVO.builder()
                .cardNumber(wallet.getCard().getCardNumber())
                .segmentCode(wallet.getSegmentCode())
                .walletId(wallet.getWalletId())
                .status(wallet.getStatus())
                .balanceAmount(response.getInactiveWalletResponseVO().getBalanceAmount())
                .build();
    }

    @Override
    public InactiveCardWalletResultVO inactiveByCard(InactiveWalletByCardEntranceVO inactiveWalletByCardEntranceVO) {

        WalletEntity wallet = walletService.findByCardNumberAndSegmentCodeAndStatusTrue(
                inactiveWalletByCardEntranceVO.getCardNumber(),
                inactiveWalletByCardEntranceVO.getSegmentCode()
        );


        return inactive(
                InactiveWalletEntranceVO.builder()
                        .segmentCode(inactiveWalletByCardEntranceVO.getSegmentCode())
                        .cardNumber(inactiveWalletByCardEntranceVO.getCardNumber())
                        .accountNumber(wallet.getAccountNumber())
                        .build()
        );
    }

    @Override
    public InactiveCardWalletResultVO inactiveByWallet(WalletIdEntranceVO walletIdEntranceVO) {

        WalletEntity wallet = walletService.findByWalletIdAndStatusTrue(walletIdEntranceVO.getWalletId());

        return inactive(
                InactiveWalletEntranceVO.builder()
                        .segmentCode(wallet.getSegmentCode())
                        .cardNumber(wallet.getCard().getCardNumber())
                        .accountNumber(wallet.getAccountNumber())
                        .build()
        );
    }
}
