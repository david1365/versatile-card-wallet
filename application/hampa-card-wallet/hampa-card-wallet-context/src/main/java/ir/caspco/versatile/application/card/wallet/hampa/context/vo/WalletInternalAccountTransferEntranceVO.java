package ir.caspco.versatile.application.card.wallet.hampa.context.vo;

import ir.caspco.versatile.common.validation.annotations.ThisIsUnequal;
import ir.caspco.versatile.common.validation.annotations.Unequal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
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
@Unequal(message = "ir.caspco.versatile.application.card.wallet.hampa.context.vo.WalletInternalAccountTransferEntranceVO.Unequal.message")
public class WalletInternalAccountTransferEntranceVO extends InternalAccountTransferEntranceVO {

    @NotNull
    @ThisIsUnequal
    private UUID walletId;

    @NotNull
    @ThisIsUnequal
    private UUID destinationWalletId;
}
