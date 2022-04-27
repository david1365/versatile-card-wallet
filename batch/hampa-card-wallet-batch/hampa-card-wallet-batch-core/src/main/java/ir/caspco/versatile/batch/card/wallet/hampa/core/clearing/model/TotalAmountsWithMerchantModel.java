package ir.caspco.versatile.batch.card.wallet.hampa.core.clearing.model;

import ir.caspco.versatile.application.card.wallet.hampa.context.domains.MerchantEntity;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Data
@SuperBuilder
public class TotalAmountsWithMerchantModel {

    private MerchantEntity merchant;
    private BigDecimal total;
}