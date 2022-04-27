package ir.caspco.versatile.application.card.wallet.hampa.context.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ir.caspco.versatile.jms.client.common.vo.CollectPaymentResponseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties("requestTrackingNumber")
public class CollectPaymentResultVO extends CollectPaymentResponseVO {

    private UUID walletId;

    private String clientTrackingCode;

    private String serverTrackingCode;
}
