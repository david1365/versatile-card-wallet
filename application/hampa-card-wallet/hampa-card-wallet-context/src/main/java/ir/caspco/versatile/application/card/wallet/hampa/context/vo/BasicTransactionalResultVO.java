package ir.caspco.versatile.application.card.wallet.hampa.context.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.Date;

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
public class BasicTransactionalResultVO extends WalletIdEntranceVO {

    private BigDecimal amount;

    private String cardNumber;
    private String segmentCode;

    private BigDecimal bookBalance;

    private String dealReference;
    private String serverTrackingCode;
    private String clientTrackingCode;
    private Date transactionDate;
}
