package ir.caspco.versatile.application.card.wallet.hampa.context.vo;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ReIssueResultVO {

    private String cardNumber;
    private String resultReIssue;
}
