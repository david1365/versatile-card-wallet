package ir.caspco.versatile.application.card.wallet.hampa.context.domains;

import ir.caspco.versatile.context.domains.BasicEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.Set;
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
@Table(name = MerchantEntity.TABLE, schema = WALLET)
public class MerchantEntity extends BasicEntity {

    public static final String TABLE = "MERCHANT";


    @Id
    @GeneratedValue
    @Column(nullable = false, unique = true, name = "ID")
    private UUID merchantId;

    @Column(nullable = false, name = "CHARGE_INTERNAL_ACCOUNT")
    private String chargeInternalAccount;

    @Column(nullable = false, name = "PURCHASE_INTERNAL_ACCOUNTT")
    private String purchaseInternalAccount;

    @Column(nullable = false, name = "CHARGE_SETTLEMEN_ACCOUNT")
    private String chargeSettlementAccount;

    @Column(nullable = false, name = "PURCHASE_SETTLEMENT_ACCOUNT")
    private String purchaseSettlementAccount;

    @Column(nullable = false, unique = true, name = "TITLE")
    private String title;

    @Column(nullable = false)
    private String chargeCron;

    @Column(nullable = false)
    private String purchaseCron;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "MERCHANT_ID")
    @ToString.Exclude
    private Set<SettlementNextValidTimeEntity> settlementNextValidTimes;
}
