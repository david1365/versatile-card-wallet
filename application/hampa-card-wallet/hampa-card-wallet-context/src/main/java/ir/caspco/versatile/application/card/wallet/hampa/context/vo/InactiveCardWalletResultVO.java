package ir.caspco.versatile.application.card.wallet.hampa.context.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.UUID;


@Data
@SuperBuilder
@NoArgsConstructor
public class InactiveCardWalletResultVO {

    private String cardNumber;
    private String segmentCode;
    private UUID walletId;
    private Boolean status;
    private BigDecimal balanceAmount;
}
