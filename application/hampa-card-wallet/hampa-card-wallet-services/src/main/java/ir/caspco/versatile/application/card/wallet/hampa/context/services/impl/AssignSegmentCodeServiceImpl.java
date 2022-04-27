package ir.caspco.versatile.application.card.wallet.hampa.context.services.impl;

import ir.caspco.versatile.application.card.wallet.hampa.context.domains.CardEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.domains.PersonEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.domains.WalletEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.exceptions.IssueCardResultException;
import ir.caspco.versatile.application.card.wallet.hampa.context.repositories.WalletRepository;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.AssignSegmentCodeService;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.CardService;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.AssignSegmentCodeEntranceVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.CardResultVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.WalletResultVO;
import ir.caspco.versatile.common.util.Shift;
import ir.caspco.versatile.jms.client.common.client.hampa.IssueCardHampaClient;
import ir.caspco.versatile.jms.client.common.msg.hampa.IssueCardHampaRequest;
import ir.caspco.versatile.jms.client.common.msg.hampa.IssueCardHampaResponse;
import ir.caspco.versatile.jms.client.common.vo.CardIssueHampaRequestVO;
import ir.caspco.versatile.jms.client.common.vo.CardIssueHampaResponseVO;
import ir.caspco.versatile.jms.client.common.vo.NoNameCardCustomerHampaVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class AssignSegmentCodeServiceImpl implements AssignSegmentCodeService {

    private final IssueCardHampaClient issueCardHampaClient;

    private final CardService cardService;

    private final WalletRepository walletRepository;

    @Value("${cardWallet.cardProductId:643}")
    private Long cardProductId;

    public AssignSegmentCodeServiceImpl(CardService cardService,

                                        IssueCardHampaClient issueCardHampaClient,

                                        WalletRepository walletRepository) {

        this.cardService = cardService;

        this.issueCardHampaClient = issueCardHampaClient;

        this.walletRepository = walletRepository;
    }

    @Override
    public CardResultVO assignSegmentCode(AssignSegmentCodeEntranceVO assignSegmentCodeEntranceVO) {

        CardIssueHampaResponseVO cardIssueHampaResponse = assign(assignSegmentCodeEntranceVO);

        return save(cardIssueHampaResponse);
    }

    private CardIssueHampaResponseVO assign(AssignSegmentCodeEntranceVO assignSegmentCodeEntrance) {

        IssueCardHampaRequest issueCardHampaRequest = IssueCardHampaRequest.builder()
                .cardIssueHampaRequest(
                        CardIssueHampaRequestVO.builder()
                                .cardProductId(cardProductId)
                                .noNameCardCustomerHampaModel(
                                        NoNameCardCustomerHampaVO.builder()
                                                .nationalId(assignSegmentCodeEntrance.getNationalCode())
                                                .build()
                                )
                                .segmentCodes(assignSegmentCodeEntrance.getSegmentCodes())
                                .build()
                )
                .build();


        IssueCardHampaResponse issueCardHampaResponse = issueCardHampaClient.send(issueCardHampaRequest)
                .retrieve()
                .result()
                .orElseThrow(IssueCardResultException::new);

        return issueCardHampaResponse.getCardIssueHampaResponse();
    }


    private CardResultVO save(CardIssueHampaResponseVO cardIssueHampaResponse) {

        CardEntity card = cardService.findByCardNumber(cardIssueHampaResponse.getCardNumber());

        PersonEntity person = card.getPerson();

        List<WalletEntity> wallets = generateWallets(cardIssueHampaResponse, card);

        wallets = (List<WalletEntity>) walletRepository.saveAll(wallets);

        Set<WalletEntity> savedWallets = card.getWallets();
        savedWallets.addAll(wallets);

        return CardResultVO.builder()
                .cardNumber(cardIssueHampaResponse.getCardNumber())
                .customerNumber(person.getCustomerNumber())
                .mobileNumber(person.getMobileNumber())
                .nationalCode(person.getNationalCode())
                .wallets(
                        savedWallets.stream()
                                .map(walletIn -> Shift.just(walletIn).toShift(WalletResultVO.class).toObject())
                                .collect(Collectors.toSet())
                )
                .build();
    }

    private List<WalletEntity> generateWallets(CardIssueHampaResponseVO cardIssueHampaResponse, CardEntity cardEntity) {

        return cardIssueHampaResponse.getCardAccounts().stream()
                .map(cardAccountHampa -> WalletEntity.builder()
                        .card(cardEntity)
                        .accountNumber(cardAccountHampa.getAccountNumber())
                        .mainAccount(cardAccountHampa.getMainAccount())
                        .segmentCode(cardAccountHampa.getSegmentCode())
                        .status(cardAccountHampa.getStatus())
                        .build())
                .collect(Collectors.toList());
    }
}
