package ir.caspco.versatile.batch.card.wallet.hampa.core.clearing.model;

import ir.caspco.versatile.application.card.wallet.hampa.context.repositories.model.TotalAmountsModel;
import lombok.Builder;
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
public class ClearingModel {

    @Builder.Default
    private Date selectedDate = new Date();

    private List<TotalAmountsModel> totalAmountModels;
}
