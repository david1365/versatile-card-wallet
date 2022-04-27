package ir.caspco.versatile.application.card.wallet.hampa.context.vo;

import ir.caspco.versatile.jms.client.common.enums.loan.LendingFileStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
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
public class LoanInformationVO {

    private UUID walletId;
    private String segmentCode;

    private Long loanFileNumber;
    private Long loanMemoNumber;

    private String facilityCode;

    private Date approvalDate;

    private Double interestRate;

    private BigDecimal loanAmount;

    private Integer installmentCount;

    private Integer repaidInstallmentCount;
    private BigDecimal repaidInstallmentAmount;

    private Integer unRepaidInstallmentCount;
    private BigDecimal unRepaidInstallmentAmount;

    private Integer unRepaidOverdueInstallmentCount;
    private BigDecimal unRepaidOverdueInstallmentAmount;

    private BigDecimal repaidPenaltyUpToNow;
    private BigDecimal unRepaidPenaltyUpToNow;

    private BigDecimal totalDiscountAmount;

    private LendingFileStatus loanStatus;

    private List<InstallmentInformationResultVO> repaymentTable;
}