package ir.caspco.versatile.application.card.wallet.hampa.context.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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
public final class CreditBillsPaymentResultVO extends CollectPaymentResultVO {

    private String cardNumber;
}
