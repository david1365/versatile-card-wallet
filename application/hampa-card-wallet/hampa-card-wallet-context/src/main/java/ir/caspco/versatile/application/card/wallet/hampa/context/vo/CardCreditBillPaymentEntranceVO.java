package ir.caspco.versatile.application.card.wallet.hampa.context.vo;

import ir.caspco.versatile.application.card.wallet.hampa.context.validation.annotations.CheckIsActive;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Data
@SuperBuilder
@NoArgsConstructor
public class CardCreditBillPaymentEntranceVO extends SegmentCreditBillPaymentEntranceVO {

    @NotNull
    @NotEmpty
    @NotBlank
    @CheckIsActive
    private String cardNumber;

    @NotNull
    @NotBlank
    @NotEmpty
    private String externalRef;
}
