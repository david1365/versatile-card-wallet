package ir.caspco.versatile.application.card.wallet.hampa.context.vo;

import ir.caspco.versatile.common.validation.annotations.IsValidMobileNumber;
import ir.caspco.versatile.common.validation.annotations.ThisIsUnequal;
import ir.caspco.versatile.common.validation.annotations.Unequal;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(callSuper = true)
@Unequal(message = "ir.caspco.versatile.application.card.wallet.hampa.context.vo.MobileInternalAccountTransferEntranceVO.Unequal.message")
public class MobileInternalAccountTransferEntranceVO extends InternalAccountTransferEntranceVO {

    @NotNull
    @NotEmpty
    @NotBlank
    @IsValidMobileNumber
    @ThisIsUnequal
    private String mobileNo;

    @NotNull
    @NotEmpty
    @NotBlank
    @IsValidMobileNumber
    @ThisIsUnequal
    private String destinationMobileNo;
}
