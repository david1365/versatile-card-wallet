package ir.caspco.versatile.application.card.wallet.hampa.context.domains;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ir.caspco.versatile.context.domains.AuditEntity;
import ir.caspco.versatile.jms.client.common.enums.hampa.HampaSettlementType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;
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
@Table(name = ClearingLogEntity.TABLE, schema = WALLET)
public class ClearingLogEntity extends AuditEntity {

    public static final String TABLE = "CLEARING_LOG";


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    @SequenceGenerator(name = "sequence", sequenceName = WALLET + ".SQ_" + ClearingLogEntity.TABLE + "_ID")
    private BigDecimal id;

    @Column(nullable = false)
    private String clearingDealReference;

    @Column(nullable = false)
    private Date clearingBookDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MERCHANT_ID", nullable = false)
    @JsonIgnoreProperties("clearingLogs")
    @ToString.Exclude
    private MerchantEntity merchant;

    @Enumerated(EnumType.STRING)
    private HampaSettlementType settlementType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ClearingLogEntity that = (ClearingLogEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
