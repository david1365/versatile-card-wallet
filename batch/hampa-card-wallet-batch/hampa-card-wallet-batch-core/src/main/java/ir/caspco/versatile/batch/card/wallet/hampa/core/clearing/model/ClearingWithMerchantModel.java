package ir.caspco.versatile.batch.card.wallet.hampa.core.clearing.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.List;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Data
@SuperBuilder
public class ClearingWithMerchantModel {

    private Date selectedDate;
    private List<TotalAmountsWithMerchantModel> totalAmountModels;
}
