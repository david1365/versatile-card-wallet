package ir.caspco.versatile.application.card.wallet.hampa.context.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class WalletCreditBillPaymentEntranceVO extends WalletCollectPaymentEntranceVO {

    @NotNull
    @NotBlank
    @NotEmpty
    private String externalRef;
}
