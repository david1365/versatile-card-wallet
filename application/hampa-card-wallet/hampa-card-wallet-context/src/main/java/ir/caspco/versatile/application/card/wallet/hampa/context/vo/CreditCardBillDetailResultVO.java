package ir.caspco.versatile.application.card.wallet.hampa.context.vo;

import ir.caspco.versatile.jms.client.common.vo.CollectPaymentVO;
import ir.caspco.versatile.jms.client.common.vo.CreditCardTransactionVO;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Data
@SuperBuilder
@NoArgsConstructor
public class CreditCardBillDetailResultVO {

    private Long id;
    private BigDecimal creditAmount;
    private Integer gracePeriod;
    private Integer protestDeadline;
    private Integer payoutType;
    private Integer sendType;
    private BigDecimal beginingCardBalance;
    private BigDecimal closingCardBalance;
    private BigDecimal totalAmount;
    private Date sendDate;

    private String insertUser;
    private String updateUser;
    private Date insertDate;
    private Date updateDate;
    private Long totalCount;
    private List<CreditCardTransactionVO> creditCardTransaction;

    private List<CreditCardTransactionVO> transactions;
    private List<CollectPaymentVO> collectPayments;
    private Date fromDate;
    private Date toDate;
    private Date calcDate;
    private String status;
    private BigDecimal interestAmount;
    private BigDecimal principleAmount;
    private BigDecimal accrualInterest;
    private Date accrualInterestDate;
    private BigDecimal fixBillAmount;
    private BigDecimal fixInterestAmount;
    private Date sitDurationEndDate;
    private BigDecimal sitDurationInterest;
    private Date nextDueDate;
}
