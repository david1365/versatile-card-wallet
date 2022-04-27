package ir.caspco.versatile.application.card.wallet.hampa.context.domains;

import ir.caspco.versatile.context.domains.BasicBusinessLogEntity;
import ir.caspco.versatile.jms.client.common.enums.loan.LoanPaymentMethod;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import static ir.caspco.versatile.context.domains.Schema.WALLET;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Audited
@SuperBuilder
@Entity
@Table(name = PayLoanLogEntity.TABLE, schema = WALLET)
public class PayLoanLogEntity extends BasicBusinessLogEntity {

    public static final String TABLE = "PAY_LOAN_LOG";


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    @SequenceGenerator(name = "sequence", sequenceName = WALLET + ".SQ_" + PayLoanLogEntity.TABLE + "_ID")
    private BigDecimal id;

    private String cardNumber;

    private String segmentCode;

    private UUID walletId;

    private BigDecimal amount;

    private String customerNumber;
    private String withdrawalAccountNumber;
    private String loanNumber;

    @Enumerated(EnumType.ORDINAL)
    private LoanPaymentMethod paymentMethod;

    private BigDecimal appliedAmount;
    private String documentNumber;
    private String documentTitle;
    private Date paymentTimeStamp;

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PayLoanLogEntity that = (PayLoanLogEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(cardNumber, that.cardNumber) &&
                Objects.equals(segmentCode, that.segmentCode) &&
                Objects.equals(walletId, that.walletId) &&
                Objects.equals(amount, that.amount) &&
                Objects.equals(customerNumber, that.customerNumber) &&
                Objects.equals(withdrawalAccountNumber, that.withdrawalAccountNumber) &&
                Objects.equals(loanNumber, that.loanNumber) &&
                paymentMethod == that.paymentMethod &&
                Objects.equals(appliedAmount, that.appliedAmount) &&
                Objects.equals(documentNumber, that.documentNumber) &&
                Objects.equals(documentTitle, that.documentTitle) &&
                Objects.equals(paymentTimeStamp, that.paymentTimeStamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(),
                id,
                cardNumber,
                segmentCode,
                walletId,
                amount,
                customerNumber,
                withdrawalAccountNumber,
                loanNumber,
                paymentMethod,
                appliedAmount,
                documentNumber,
                documentTitle,
                paymentTimeStamp);
    }
}
