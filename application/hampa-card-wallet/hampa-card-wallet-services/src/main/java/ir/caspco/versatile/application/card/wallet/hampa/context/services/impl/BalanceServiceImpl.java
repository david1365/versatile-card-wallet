package ir.caspco.versatile.application.card.wallet.hampa.context.services.impl;

import ir.caspco.versatile.application.card.wallet.hampa.context.domains.CardEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.domains.WalletEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.exceptions.WalletInactiveException;
import ir.caspco.versatile.application.card.wallet.hampa.context.repositories.WalletRepository;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.BalanceService;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.CardService;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.WalletService;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.CardSegmentBookBalanceEntranceVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.SegmentBookBalanceResultVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.WalletIdEntranceVO;
import ir.caspco.versatile.common.util.Shift;
import ir.caspco.versatile.jms.client.common.client.hampa.SegmentBookBalanceClient;
import ir.caspco.versatile.jms.client.common.enums.CardType;
import ir.caspco.versatile.jms.client.common.msg.hampa.HampaGetSegmentBookBalanceRequest;
import ir.caspco.versatile.jms.client.common.msg.hampa.HampaGetSegmentBookBalanceResponse;
import ir.caspco.versatile.jms.client.common.vo.hampa.HampaSegmentBookBalanceRequestVO;
import ir.caspco.versatile.jms.client.common.vo.hampa.HampaSegmentBookBalanceResponseVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Service
@Transactional
public class BalanceServiceImpl implements BalanceService {

    private final SegmentBookBalanceClient segmentBookBalanceClient;

    private final WalletRepository walletRepository;

    private final CardService cardService;
    private final WalletService walletService;


    public BalanceServiceImpl(SegmentBookBalanceClient segmentBookBalanceClient,
                              WalletRepository walletRepository,
                              CardService cardService,
                              WalletService walletService) {

        this.segmentBookBalanceClient = segmentBookBalanceClient;
        this.walletRepository = walletRepository;
        this.cardService = cardService;
        this.walletService = walletService;
    }


    @Override
    public List<SegmentBookBalanceResultVO> cardBalance(CardSegmentBookBalanceEntranceVO cardSegmentBookBalanceEntrance) {

        HampaSegmentBookBalanceRequestVO segmentBookBalanceRequest = Shift.just(cardSegmentBookBalanceEntrance)
                .toShift(HampaSegmentBookBalanceRequestVO.class).toObject();

        HampaGetSegmentBookBalanceRequest hampaGetSegmentBookBalanceRequest = HampaGetSegmentBookBalanceRequest.builder()
                .requestDto(segmentBookBalanceRequest)
                .build();

        HampaGetSegmentBookBalanceResponse hampaGetSegmentBookBalanceResponse = segmentBookBalanceClient.send(hampaGetSegmentBookBalanceRequest)
                .retrieve()
                .result()
                .orElse(
                        HampaGetSegmentBookBalanceResponse.builder()
                                .segmentBookBalanceResponseDto(Collections.emptyList())
                                .build()
                );

        CardEntity card = cardService.findByCardNumber(segmentBookBalanceRequest.getCardNumber());

        List<HampaSegmentBookBalanceResponseVO> segmentBookBalances =
                hampaGetSegmentBookBalanceResponse.getSegmentBookBalanceResponseDto();

        return segmentBookBalances.stream()
                .map(segmentBookBalance -> {

                    String segmentCode = segmentBookBalance.getSegmentCode();

                    WalletEntity walletFound = walletService.findByPersonAndSegmentCodeAndStatusTrue(card.getPerson(), segmentCode);

                    return SegmentBookBalanceResultVO.builder()
                            .walletId(walletFound.getWalletId())
                            .bookBalance(segmentBookBalance.getBookBalance())
                            .segmentCode(segmentBookBalance.getSegmentCode())
                            .build();
                })
                .collect(Collectors.toList());

    }

    @Override
    public SegmentBookBalanceResultVO walletBalance(WalletIdEntranceVO walletIdEntrance) {

        Optional<WalletEntity> optionalWallet = walletRepository.findByWalletIdAndStatusTrue(walletIdEntrance.getWalletId());

        return optionalWallet.map(wallet -> {

            final CardEntity card = cardService.findByPersonAndCardType(wallet.getCard().getPerson(), CardType.DEBIT);

            List<SegmentBookBalanceResultVO> segmentBookBalanceList = cardBalance(
                    CardSegmentBookBalanceEntranceVO.builder()
                            .cardNumber(card.getCardNumber())
                            .segmentCodeList(wallet.getSegmentCode() == null ? null : Collections.singletonList(wallet.getSegmentCode()))
                            .build()
            );

            return segmentBookBalanceList.isEmpty() ? null : segmentBookBalanceList.get(0);

        }).orElseThrow(WalletInactiveException::new);
    }
}
