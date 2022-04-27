package ir.caspco.versatile.application.card.wallet.hampa.context.domains;


import ir.caspco.versatile.context.domains.BasicBusinessLogEntity;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

import static ir.caspco.versatile.context.domains.Schema.WALLET;

@Setter
@ToString
@RequiredArgsConstructor
@Audited
@SuperBuilder
@Entity
@Table(name = ReverseTranLogEntity.TABLE, schema = WALLET)
public class ReverseTranLogEntity extends BasicBusinessLogEntity {

    public static final String TABLE = "REVERSE_TRANSACTION_LOG";


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    @SequenceGenerator(name = "sequence", sequenceName = WALLET + ".SQ_" + ReverseTranLogEntity.TABLE + "_ID")
    private BigDecimal id;

    private String clientTrackingCode;
    private String dealReference;
    private String adjustDeal;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ReverseTranLogEntity that = (ReverseTranLogEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
