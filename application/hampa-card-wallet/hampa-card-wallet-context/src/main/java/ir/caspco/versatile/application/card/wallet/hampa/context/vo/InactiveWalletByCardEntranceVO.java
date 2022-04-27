package ir.caspco.versatile.application.card.wallet.hampa.context.vo;


import ir.caspco.versatile.application.card.wallet.hampa.context.validation.annotations.CheckIsActive;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@SuperBuilder
@NoArgsConstructor
public class InactiveWalletByCardEntranceVO {

    @NotEmpty
    @NotNull
    @NotBlank
    @CheckIsActive
    private String cardNumber;

    @NotEmpty
    @NotNull
    @NotBlank
    private String segmentCode;
}
