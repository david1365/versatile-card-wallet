package ir.caspco.versatile.application.card.wallet.hampa.context.vo;

import ir.caspco.versatile.application.card.wallet.hampa.context.validation.annotations.CheckCreditByCard;
import ir.caspco.versatile.application.card.wallet.hampa.context.validation.annotations.CheckIsActive;
import ir.caspco.versatile.common.validation.annotations.IsValidUUID;
import lombok.Data;
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
public class CardExchangesEntranceVO {

    @NotNull
    @NotEmpty
    @NotBlank
    @CheckIsActive
    @CheckCreditByCard
    private String cardNumber;

    private String segmentCode;

    @NotNull
    private UUID merchantId;

    @NotNull
    private BigDecimal amount;

    @NotNull
    @NotEmpty
    @NotBlank
    @IsValidUUID
    private String clientTrackingCode;
}
