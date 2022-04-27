package ir.caspco.versatile.application.card.wallet.hampa.context.vo;

import ir.caspco.versatile.application.card.wallet.hampa.context.enums.PurchaseType;
import ir.caspco.versatile.jms.client.common.enums.thirdparty.InternetPackageOperator;
import ir.caspco.versatile.jms.client.common.enums.thirdparty.ServiceType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
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
public class PurchaseThirdPartyEntranceVO {

    private String cardNumber;
    private String segmentCode;
    private UUID merchantId;
    private InternetPackageOperator operator;
    private Integer productId;
    private BigDecimal amount;
    private UUID walletId;
    private String nationalCode;
    private Long paymentServiceId;
    private String mobileNumber;
    private ServiceType serviceType;
    private PurchaseType purchaseType;
    private String clientTrackingCode;
}
