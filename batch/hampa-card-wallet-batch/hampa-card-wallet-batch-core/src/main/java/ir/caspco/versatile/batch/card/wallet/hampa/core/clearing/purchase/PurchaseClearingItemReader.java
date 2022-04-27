package ir.caspco.versatile.batch.card.wallet.hampa.core.clearing.purchase;

import ir.caspco.versatile.application.card.wallet.hampa.context.repositories.PurchaseCardWalletLogRepository;
import ir.caspco.versatile.application.card.wallet.hampa.context.repositories.model.TotalAmountsModel;
import ir.caspco.versatile.batch.card.wallet.hampa.core.clearing.model.ClearingModel;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Component
@StepScope
public class PurchaseClearingItemReader implements ItemReader<ClearingModel> {

    @Value("${batch.pageable.pageSize.clearing.purchase}")
    private Integer pageSize;


    private final PurchaseCardWalletLogRepository purchaseCardWalletLogRepository;

    public PurchaseClearingItemReader(PurchaseCardWalletLogRepository purchaseCardWalletLogRepository) {

        this.purchaseCardWalletLogRepository = purchaseCardWalletLogRepository;
    }

    @Override
    public ClearingModel read() {

        ClearingModel clearingModel = ClearingModel.builder().build();

        List<TotalAmountsModel> totalAmountModels = purchaseCardWalletLogRepository.totalAmounts(Pageable.ofSize(pageSize));
        clearingModel.setTotalAmountModels(totalAmountModels);

        return totalAmountModels.isEmpty() ? null : clearingModel;
    }
}
