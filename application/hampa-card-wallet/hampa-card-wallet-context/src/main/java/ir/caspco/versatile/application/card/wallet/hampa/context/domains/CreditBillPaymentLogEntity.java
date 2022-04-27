package ir.caspco.versatile.application.card.wallet.hampa.context.domains;

import ir.caspco.versatile.context.domains.BasicBusinessLogEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

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
@Table(name = CreditBillPaymentLogEntity.TABLE, schema = WALLET)
public class CreditBillPaymentLogEntity extends BasicBusinessLogEntity {

    public static final String TABLE = "CREDIT_BILL_PAYMENT_LOG";


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    @SequenceGenerator(name = "sequence", sequenceName = WALLET + ".SQ_" + CreditBillPaymentLogEntity.TABLE + "_ID")
    private BigDecimal id;

    private String cardNumber;

    private String segmentCode;

    private String creditSegmentCode;

    private BigDecimal amount;

    private String externalRef;

    private BigDecimal principleAmount;

    private BigDecimal interestAmount;

    private String clientTrackingCode;

    private String dealReference;

    private String serverTrackingCode;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CreditBillPaymentLogEntity that = (CreditBillPaymentLogEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(cardNumber, that.cardNumber) &&
                Objects.equals(segmentCode, that.segmentCode) &&
                Objects.equals(creditSegmentCode, that.creditSegmentCode) &&
                Objects.equals(amount, that.amount) &&
                Objects.equals(externalRef, that.externalRef) &&
                Objects.equals(principleAmount, that.principleAmount) &&
                Objects.equals(interestAmount, that.interestAmount) &&
                Objects.equals(clientTrackingCode, that.clientTrackingCode) &&
                Objects.equals(dealReference, that.dealReference) &&
                Objects.equals(serverTrackingCode, that.serverTrackingCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(),
                id,
                cardNumber,
                segmentCode,
                creditSegmentCode,
                amount,
                externalRef,
                principleAmount,
                interestAmount,
                clientTrackingCode,
                dealReference,
                serverTrackingCode);
    }
}
