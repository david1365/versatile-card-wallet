package ir.caspco.versatile.batch.card.wallet.hampa.core.clearing.charge;

import ir.caspco.versatile.application.card.wallet.hampa.context.repositories.ChargeCardWalletLogRepository;
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
public class ChargeClearingItemReader implements ItemReader<ClearingModel> {

    @Value("${batch.pageable.pageSize.clearing.charge}")
    private Integer pageSize;


    private final ChargeCardWalletLogRepository chargeCardWalletLogRepository;

    public ChargeClearingItemReader(ChargeCardWalletLogRepository chargeCardWalletLogRepository) {

        this.chargeCardWalletLogRepository = chargeCardWalletLogRepository;
    }

    @Override
    public ClearingModel read() {

        ClearingModel clearingModel = ClearingModel.builder().build();

        List<TotalAmountsModel> totalAmountModels = chargeCardWalletLogRepository.totalAmounts(Pageable.ofSize(pageSize));
        clearingModel.setTotalAmountModels(totalAmountModels);

        return totalAmountModels.isEmpty() ? null : clearingModel;
    }
}
