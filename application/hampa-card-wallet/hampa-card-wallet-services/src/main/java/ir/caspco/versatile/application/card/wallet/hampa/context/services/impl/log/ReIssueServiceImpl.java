package ir.caspco.versatile.application.card.wallet.hampa.context.services.impl.log;

import ir.caspco.versatile.application.card.wallet.hampa.context.services.ReIssueService;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.ReIssueResultVO;
import ir.caspco.versatile.jms.client.common.client.hampa.ReIssueHampaCardClient;
import ir.caspco.versatile.jms.client.common.exceptions.CoreContentResultException;
import ir.caspco.versatile.jms.client.common.msg.hampa.ReIssueHampaCardRequest;
import ir.caspco.versatile.jms.client.common.msg.hampa.ReIssueHampaCardResponse;
import ir.caspco.versatile.jms.client.common.vo.hampa.ReIssueHampaCardEntranceVO;
import ir.caspco.versatile.jms.client.common.vo.hampa.ReIssueHampaCardResultVO;
import org.springframework.stereotype.Service;

@Service
public class ReIssueServiceImpl implements ReIssueService {

    private final ReIssueHampaCardClient reIssueHampaCardClient;

    public ReIssueServiceImpl(ReIssueHampaCardClient reIssueHampaCardClient) {

        this.reIssueHampaCardClient = reIssueHampaCardClient;
    }

    @Override
    public ReIssueResultVO reIssue(String cardNumber) {

        ReIssueHampaCardResponse reIssueHampaCardResponse = reIssueHampaCardClient.send(
                        ReIssueHampaCardRequest.builder()
                                .reIssueHampaCardEntrance(
                                        ReIssueHampaCardEntranceVO.builder()
                                                .cardNumber(cardNumber)
                                                .build()
                                )
                                .build())
                .retrieve()
                .result()
                .orElseThrow(CoreContentResultException::new);


        ReIssueHampaCardResultVO reIssueHampaCardResult = reIssueHampaCardResponse.getReIssueHampaCardResult();

        return ReIssueResultVO.builder()
                .cardNumber(reIssueHampaCardResult.getCardNumber())
                .build();
    }
}
