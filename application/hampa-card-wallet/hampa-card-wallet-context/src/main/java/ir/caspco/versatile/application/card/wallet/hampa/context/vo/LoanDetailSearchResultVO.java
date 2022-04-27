package ir.caspco.versatile.application.card.wallet.hampa.context.vo;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

import ir.caspco.versatile.jms.client.common.msg.LoanDetailResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LoanDetailSearchResultVO extends LoanDetailResponse {

    private UUID walletId;

    private String cardNumber;
    private String segmentCode;

    private String loanNumber;
}
