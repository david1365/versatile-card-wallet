package ir.caspco.versatile.application.card.wallet.hampa.context.vo;

import ir.caspco.versatile.jms.client.common.enums.ChOrderStatus;
import ir.caspco.versatile.jms.client.common.enums.StatementSearchDirection;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
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
public class StatementEntranceVO {

    private Date fromDate;
    private Date toDate;
    private Date fromDateTime;
    private Date toDateTime;

    private BigDecimal fromAmount;
    private BigDecimal toAmount;

    private String receiptNo;

    @NotNull
    private Long length;

    @NotNull
    private Long offset;


    private ChOrderStatus order;

    @Builder.Default
    private Boolean showDealDescription = false;

    @Builder.Default
    private Boolean ignoreThersholdPageSize = false;

    @Builder.Default
    private Boolean needFirstBalance = false;

    @Builder.Default
    private StatementSearchDirection statementSearchDirection = StatementSearchDirection.END_TO_START;
}
