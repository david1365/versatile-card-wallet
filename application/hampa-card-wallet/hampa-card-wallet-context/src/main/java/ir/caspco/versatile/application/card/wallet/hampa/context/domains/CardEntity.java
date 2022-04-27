package ir.caspco.versatile.application.card.wallet.hampa.context.domains;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ir.caspco.versatile.context.domains.AuditEntity;
import ir.caspco.versatile.jms.client.common.enums.CardType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

import static ir.caspco.versatile.context.domains.Schema.WALLET;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Getter
@Setter
@RequiredArgsConstructor
@Audited
@SuperBuilder
@Entity
@Table(name = CardEntity.TABLE, schema = WALLET)
public class CardEntity extends AuditEntity {

    public static final String TABLE = "CARD";


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    @SequenceGenerator(name = "sequence", sequenceName = WALLET + ".SQ_" + CardEntity.TABLE + "_ID")
    private BigDecimal id;

    @Column(nullable = false, name = "CARD_NUMBER", unique = true)
    private String cardNumber;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "CARD_ID")
    @ToString.Exclude
    private Set<WalletEntity> wallets;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "PERSON_ID")
    @JsonIgnoreProperties("cards")
    @ToString.Exclude
    private PersonEntity person;

    @Enumerated(EnumType.ORDINAL)
    @ColumnDefault("0")
    @Column(nullable = false, name = "CARD_TYPE")
    private CardType cardType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CardEntity that = (CardEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
