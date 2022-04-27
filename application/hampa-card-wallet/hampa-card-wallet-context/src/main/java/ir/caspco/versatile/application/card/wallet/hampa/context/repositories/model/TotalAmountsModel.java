package ir.caspco.versatile.application.card.wallet.hampa.context.repositories.model;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

public interface TotalAmountsModel {

    UUID getMerchantId();

    BigDecimal getTotal();
}