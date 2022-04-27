package ir.caspco.versatile.application.card.wallet.hampa.context.domains;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ir.caspco.versatile.context.domains.AuditEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;
import org.hibernate.envers.Audited;

import javax.persistence.*;
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
@RequiredArgsConstructor
@Audited
@SuperBuilder
@Entity
@Table(name =
        WalletEntity.TABLE,
        schema = WALLET,
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"ACCOUNT_NUMBER", "SEGMENT_CODE"},
                        name = "UK_AC_SC"
                )
        }
)
public class WalletEntity extends AuditEntity {

    public static final String TABLE = "WALLET";

    public static final class Status {
        public static final int CONNECTED = 1;
        public static final int NOT_CONNECTED = 0;
    }

    @Id
    @GeneratedValue
    @Column(nullable = false, unique = true, name = "ID")
    private UUID walletId;

    @Column(nullable = false, unique = true, name = "ACCOUNT_NUMBER")
    private String accountNumber; //شماره حساب

    @Column(nullable = false)
    private Boolean mainAccount; //حساب اصلی است یا نه

    @Column(nullable = false)
    private Boolean status; //فعال و غیر فعال

    @Column(name = "SEGMENT_CODE")
    private String segmentCode; //شماره ارگان

    @Column(unique = true, name = "LOAN_FILE_NUMBER")
    private Long loanFileNumber;//شماره وام در صورت داشتن کارت اعتباری

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CARD_ID")
    @JsonIgnoreProperties("wallets")
    @ToString.Exclude
    private CardEntity card;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        WalletEntity that = (WalletEntity) o;
        return walletId != null && Objects.equals(walletId, that.walletId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
