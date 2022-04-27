package ir.caspco.versatile.application.card.wallet.hampa.context.domains;

import ir.caspco.versatile.context.domains.AuditEntity;
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
@Table(name = PersonEntity.TABLE, schema = WALLET)
public class PersonEntity extends AuditEntity {

    public static final String TABLE = "PERSON";


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    @SequenceGenerator(name = "sequence", sequenceName = WALLET + ".SQ_" + PersonEntity.TABLE + "_ID")
    private BigDecimal id;

    @Column(nullable = false, name = "NATIONAL_CODE", unique = true)
    private String nationalCode;

    @Column(name = "CUSTOMER_NUMBER")
    private String customerNumber;

    @Column(nullable = false, name = "MOBILE_NUMBER", unique = true)
    private String mobileNumber;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "PERSON_ID")
    @ToString.Exclude
    private Set<CardEntity> cards;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PersonEntity that = (PersonEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
