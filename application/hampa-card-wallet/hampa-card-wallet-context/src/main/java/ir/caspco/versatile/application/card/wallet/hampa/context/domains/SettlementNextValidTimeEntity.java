package ir.caspco.versatile.application.card.wallet.hampa.context.domains;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ir.caspco.versatile.application.card.wallet.hampa.context.enums.SettlementType;
import ir.caspco.versatile.context.domains.AuditEntity;
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
@Table(name = SettlementNextValidTimeEntity.TABLE,
        schema = WALLET,
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"MERCHANT_ID", "SETTLEMENT_TYPE"},
                        name = "UK_MI_ST"
                )
        })
public class SettlementNextValidTimeEntity extends AuditEntity {

    public static final String TABLE = "Settlement_Next_Valid_Time";


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    @SequenceGenerator(name = "sequence", sequenceName = WALLET + ".SQ_" + SettlementNextValidTimeEntity.TABLE + "_ID")
    private BigDecimal id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MERCHANT_ID")
    @JsonIgnoreProperties("settlementNextValidTimes")
    @ToString.Exclude
    private MerchantEntity merchant;

    @Column(nullable = false)
    private Date nextValidTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "SETTLEMENT_TYPE")
    private SettlementType settlementType;

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SettlementNextValidTimeEntity that = (SettlementNextValidTimeEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(merchant, that.merchant) &&
                Objects.equals(nextValidTime, that.nextValidTime) &&
                settlementType == that.settlementType;
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), id, merchant, nextValidTime, settlementType);
    }
}
