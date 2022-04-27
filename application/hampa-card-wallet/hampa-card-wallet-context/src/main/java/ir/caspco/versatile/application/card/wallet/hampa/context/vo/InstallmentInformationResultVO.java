package ir.caspco.versatile.application.card.wallet.hampa.context.vo;

import ir.caspco.versatile.jms.client.common.enums.loan.RepaymentStatus;
import lombok.Data;
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
public class InstallmentInformationResultVO {

    private Date dueDate;
    private Integer installmentNo;
    private BigDecimal installmentAmount;
    private BigDecimal repaidInstallmentAmount;
    private BigDecimal unRepaidInstallmentAmount;
    private Integer delayedDuration;
    private BigDecimal penaltyAmount;
    private BigDecimal discountAmount;
    private RepaymentStatus repaymentStatus;
}
