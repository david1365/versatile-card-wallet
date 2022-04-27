package ir.caspco.versatile.application.card.wallet.hampa.context.domains;

import ir.caspco.versatile.application.card.wallet.hampa.context.enums.PurchaseType;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;
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
@Table(name = PurchaseCardWalletLogEntity.TABLE, schema = WALLET)
public class PurchaseCardWalletLogEntity extends MerchantExchangesLogEntity {

    public static final String TABLE = "PURCHASE_CARD_WALLET_LOG";


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    @SequenceGenerator(name = "sequence", sequenceName = WALLET + ".SQ_" + PurchaseCardWalletLogEntity.TABLE + "_ID")
    private BigDecimal id;
    private String serverCreditTrackingCode;
    private String creditDealReference;

    @Enumerated(EnumType.ORDINAL)
    @Builder.Default
    private PurchaseType purchaseType = PurchaseType.PURCHASE;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PurchaseCardWalletLogEntity that = (PurchaseCardWalletLogEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
