package ir.caspco.versatile.application.card.wallet.hampa.context.validation;

import ir.caspco.versatile.application.card.wallet.hampa.context.services.BalanceService;
import ir.caspco.versatile.application.card.wallet.hampa.context.validation.annotations.CheckBookBalanceByWallet;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.SegmentBookBalanceResultVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.WalletIdEntranceVO;
import ir.caspco.versatile.common.util.ApplicationContextUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author Davood Akbari - 1399
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

public class CheckWalletBalanceValidator implements ConstraintValidator<CheckBookBalanceByWallet, UUID> {

    @Override
    public boolean isValid(UUID walletId, ConstraintValidatorContext constraintValidatorContext) {

        BalanceService balanceService = ApplicationContextUtil.getBean(BalanceService.class);

        SegmentBookBalanceResultVO segmentBookBalanceResult = balanceService.walletBalance(
                WalletIdEntranceVO.builder()
                        .walletId(walletId)
                        .build()
        );


        return segmentBookBalanceResult.getBookBalance().compareTo(BigDecimal.ZERO) > 0;
    }
}
