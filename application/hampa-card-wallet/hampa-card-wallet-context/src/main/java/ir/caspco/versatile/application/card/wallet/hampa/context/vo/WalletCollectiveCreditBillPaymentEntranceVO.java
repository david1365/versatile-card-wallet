package ir.caspco.versatile.application.card.wallet.hampa.context.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

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
public class WalletCollectiveCreditBillPaymentEntranceVO {

    @NotNull
    @NotBlank
    @NotEmpty
    private String externalRef;

    @NotNull
    @NotEmpty
    @Valid
    private List<WalletCollectPaymentEntranceVO> walletsInfos;
}
