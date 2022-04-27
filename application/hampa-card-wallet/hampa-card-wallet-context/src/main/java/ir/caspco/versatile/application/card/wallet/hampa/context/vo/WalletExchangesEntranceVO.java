package ir.caspco.versatile.application.card.wallet.hampa.context.vo;

import ir.caspco.versatile.common.validation.annotations.IsValidUUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
@EqualsAndHashCode(callSuper = true)
public class WalletExchangesEntranceVO extends WalletIdEntranceVO {

    @NotNull
    private BigDecimal amount;

    @NotNull
    private UUID merchantId;

    @NotNull
    @NotEmpty
    @NotBlank
    @IsValidUUID
    private String clientTrackingCode;
}
