package ir.caspco.versatile.application.card.wallet.hampa.context.services;

import ir.caspco.versatile.application.card.wallet.hampa.context.domains.MerchantEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.MerchantVO;

import java.util.UUID;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

public interface MerchantService {

    MerchantEntity findByMerchantIdAndActiveTrue(UUID merchantId);

    MerchantVO save(MerchantVO merchantIn);

    MerchantVO update(MerchantVO merchantIn);

    Boolean deleteById(UUID merchantID);

    MerchantVO findById(UUID merchantId);
}
