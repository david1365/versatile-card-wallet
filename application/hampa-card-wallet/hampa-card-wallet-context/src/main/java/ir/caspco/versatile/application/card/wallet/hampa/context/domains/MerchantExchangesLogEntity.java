package ir.caspco.versatile.application.card.wallet.hampa.context.domains;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Getter
@Setter
@RequiredArgsConstructor
@SuperBuilder
@MappedSuperclass
@AllArgsConstructor
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class MerchantExchangesLogEntity extends BasicExchangesLogEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MERCHANT_ID", nullable = false)
    @JsonIgnoreProperties({"chargeCardWalletLogs", "purchaseCardWalletLogs"})
    @ToString.Exclude
    private MerchantEntity merchant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLEARING_LOG_ID")
    @ToString.Exclude
    private ClearingLogEntity clearingLog;
}
