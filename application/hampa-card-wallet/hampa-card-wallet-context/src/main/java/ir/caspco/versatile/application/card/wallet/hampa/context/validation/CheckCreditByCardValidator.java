package ir.caspco.versatile.application.card.wallet.hampa.context.validation;

import ir.caspco.versatile.application.card.wallet.hampa.context.domains.CardEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.CardService;
import ir.caspco.versatile.application.card.wallet.hampa.context.validation.annotations.CheckCreditByCard;
import ir.caspco.versatile.common.util.ApplicationContextUtil;
import ir.caspco.versatile.jms.client.common.enums.CardType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Davood Akbari - 1399
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

public class CheckCreditByCardValidator implements ConstraintValidator<CheckCreditByCard, String> {

    @Override
    public boolean isValid(String cardNumber, ConstraintValidatorContext constraintValidatorContext) {

        CardService cardService = ApplicationContextUtil.getBean(CardService.class);

        CardEntity card = cardService.findByCardNumber(cardNumber);

        return CardType.DEBIT.equals(card.getCardType());
    }
}
