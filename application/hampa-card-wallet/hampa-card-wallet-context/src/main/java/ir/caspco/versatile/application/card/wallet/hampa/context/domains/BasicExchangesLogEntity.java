package ir.caspco.versatile.application.card.wallet.hampa.context.domains;

import ir.caspco.versatile.context.domains.BasicBusinessLogEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.MappedSuperclass;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

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
@SuperBuilder
@MappedSuperclass
@AllArgsConstructor
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class BasicExchangesLogEntity extends BasicBusinessLogEntity {

    private String cardNumber;

    private String segmentCode;

    private UUID walletId;

    private BigDecimal amount;

    private String clientTrackingCode;

    private String dealReference;

    private String serverTrackingCode;

    private Date transactionDate;
}
