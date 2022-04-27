package ir.caspco.versatile.application.card.wallet.hampa.context.domains;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.math.BigDecimal;
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
@Table(name = InternalAccountTransferLogEntity.TABLE, schema = WALLET)
public class InternalAccountTransferLogEntity extends BasicExchangesLogEntity {

    public static final String TABLE = "Int_Acc_Tra_Log";


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    @SequenceGenerator(name = "sequence", sequenceName = WALLET + ".SQ_" + InternalAccountTransferLogEntity.TABLE + "_ID")
    private BigDecimal id;

    private String mobileNo;

    private UUID destinationWalletId;
    private String destinationCardNumber;
    private String destinationSegmentCode;
    private String destinationMobileNo;

    private BigDecimal currentBookBalance;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        InternalAccountTransferLogEntity that = (InternalAccountTransferLogEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
