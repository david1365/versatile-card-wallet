package ir.caspco.versatile.batch.card.wallet.hampa.core.clearing;

import ir.caspco.versatile.application.card.wallet.hampa.context.repositories.MerchantRepository;
import ir.caspco.versatile.batch.card.wallet.hampa.core.clearing.model.ClearingModel;
import ir.caspco.versatile.batch.card.wallet.hampa.core.clearing.model.ClearingWithMerchantModel;
import ir.caspco.versatile.batch.card.wallet.hampa.core.clearing.model.TotalAmountsWithMerchantModel;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Component
@StepScope
public class ClearingItemProcessor implements ItemProcessor<ClearingModel, ClearingWithMerchantModel> {

    private final MerchantRepository merchantRepository;


    public ClearingItemProcessor(MerchantRepository merchantRepository) {
        this.merchantRepository = merchantRepository;
    }


    @Override
    public ClearingWithMerchantModel process(ClearingModel clearing) throws Exception {

        return ClearingWithMerchantModel.builder()
                .selectedDate(clearing.getSelectedDate())
                .totalAmountModels(
                        clearing.getTotalAmountModels().stream()
                                .map(totalAmounts ->
                                        TotalAmountsWithMerchantModel.builder()
                                                .merchant(
                                                        merchantRepository.findByMerchantIdAndActiveTrue(totalAmounts.getMerchantId())
                                                                .orElse(null)
                                                )
                                                .total(totalAmounts.getTotal())
                                                .build()
                                )
                                .filter(totalAmountsWithMerchant -> totalAmountsWithMerchant.getMerchant() != null)
                                .collect(Collectors.toList())
                )
                .build();
    }
}
