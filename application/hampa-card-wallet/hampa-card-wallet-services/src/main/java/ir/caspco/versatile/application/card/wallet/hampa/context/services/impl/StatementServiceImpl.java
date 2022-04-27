package ir.caspco.versatile.application.card.wallet.hampa.context.services.impl;

import ir.caspco.versatile.application.card.wallet.hampa.context.domains.CardEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.domains.WalletEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.CardService;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.StatementService;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.WalletService;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.CardStatementEntranceVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.StatementEntranceVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.StatementResultVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.WalletStatementEntranceVO;
import ir.caspco.versatile.common.util.Shift;
import ir.caspco.versatile.jms.client.common.client.hampa.GetHampaStatementClient;
import ir.caspco.versatile.jms.client.common.enums.CardType;
import ir.caspco.versatile.jms.client.common.exceptions.CoreContentResultException;
import ir.caspco.versatile.jms.client.common.msg.hampa.GetHampaStatementRequest;
import ir.caspco.versatile.jms.client.common.msg.hampa.GetHampaStatementResponse;
import ir.caspco.versatile.jms.client.common.vo.hampa.StatementRequestVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Service
@Transactional
public class StatementServiceImpl implements StatementService {

    private final GetHampaStatementClient getHampaStatementClient;

    private final WalletService walletService;
    private final CardService cardService;


    public StatementServiceImpl(GetHampaStatementClient getHampaStatementClient,
                                WalletService walletService,
                                CardService cardService) {

        this.getHampaStatementClient = getHampaStatementClient;

        this.walletService = walletService;
        this.cardService = cardService;
    }


    @Override
    public StatementResultVO cardStatement(CardStatementEntranceVO cardStatementEntrance) {

        StatementRequestVO statementRequest = convertStatement(cardStatementEntrance);
        statementRequest.setCardNumber(cardStatementEntrance.getCardNumber());

        return statement(statementRequest);
    }

    @Override
    public StatementResultVO walletStatement(WalletStatementEntranceVO walletStatementEntrance) {

        final WalletEntity wallet = walletService.findByWalletIdAndStatusTrue(walletStatementEntrance.getWalletId());

        final CardEntity card = cardService.findByPersonAndCardType(wallet.getCard().getPerson(), CardType.DEBIT);

        StatementRequestVO statementRequest = convertStatement(walletStatementEntrance);
        statementRequest.setCardNumber(card.getCardNumber());
        statementRequest.setSegmentCode(wallet.getSegmentCode());

        return statement(statementRequest);
    }

    private StatementResultVO statement(StatementRequestVO statementRequest) {

        GetHampaStatementRequest getHampaStatementRequest = GetHampaStatementRequest.builder()
                .statementRequest(statementRequest)
                .build();

        GetHampaStatementResponse getHampaStatementResponse = getHampaStatementClient.send(getHampaStatementRequest)
                .retrieve()
                .result()
                .orElseThrow(CoreContentResultException::new);

        return StatementResultVO.builder()
                .statementsResponse(getHampaStatementResponse.getStatementsResponse())
                .totalRecord(getHampaStatementResponse.getTotalRecord())
                .build();
    }

    private StatementRequestVO convertStatement(StatementEntranceVO statementEntrance) {

        return Shift
                .just(statementEntrance)
                .toShift(StatementRequestVO.class)
                .toObject();
    }
}
