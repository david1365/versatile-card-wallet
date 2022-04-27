package ir.caspco.versatile.application.card.wallet.hampa.context.validation;

import ir.caspco.versatile.application.card.wallet.hampa.context.domains.CardEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.domains.WalletEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.CardService;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.WalletService;
import ir.caspco.versatile.application.card.wallet.hampa.context.validation.annotations.CheckCreditByWallet;
import ir.caspco.versatile.common.util.ApplicationContextUtil;
import ir.caspco.versatile.jms.client.common.enums.CardType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.UUID;

/**
 * @author Davood Akbari - 1399
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

public class CheckCreditByWalletValidator implements ConstraintValidator<CheckCreditByWallet, UUID> {

    @Override
    public boolean isValid(UUID walletId, ConstraintValidatorContext constraintValidatorContext) {

        WalletService walletService = ApplicationContextUtil.getBean(WalletService.class);
        CardService cardService = ApplicationContextUtil.getBean(CardService.class);

        WalletEntity wallet = walletService.findByWalletIdAndStatusTrue(walletId);

        CardEntity card = cardService.findById(wallet.getCard().getId());

        return CardType.DEBIT.equals(card.getCardType());
    }
}
