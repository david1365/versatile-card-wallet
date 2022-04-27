package ir.caspco.versatile.application.card.wallet.hampa.context.vo;

import ir.caspco.versatile.jms.client.common.vo.hampa.PurchaseThirdPartyResultVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 * @author Mohammad Javad Zahedi - 2021
 * mjavadzahedi0@gmail.com
 * 09377732642
 */


@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PurchaseThirdPartyFinalResultVO extends PurchaseThirdPartyResultVO {

    private UUID walletId;
    private String nationalCode;
    private String mobileNumber;

    private String cardNumber;

    private String segmentCode;

    private UUID merchantId;

    private BigDecimal amount;
}

