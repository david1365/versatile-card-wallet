package ir.caspco.versatile.application.card.wallet.hampa.context.services;


import ir.caspco.versatile.application.card.wallet.hampa.context.vo.ReIssueResultVO;

public interface ReIssueService {

    ReIssueResultVO reIssue(String cardNumber);
}
