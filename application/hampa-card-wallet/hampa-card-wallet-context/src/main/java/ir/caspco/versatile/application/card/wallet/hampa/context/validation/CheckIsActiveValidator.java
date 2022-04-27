package ir.caspco.versatile.application.card.wallet.hampa.context.validation;

import ir.caspco.versatile.application.card.wallet.hampa.context.validation.annotations.CheckIsActive;
import ir.caspco.versatile.common.util.ApplicationContextUtil;
import ir.caspco.versatile.jms.client.common.client.hampa.ValidateActiveHampaCardClient;
import ir.caspco.versatile.jms.client.common.exceptions.CoreContentResultException;
import ir.caspco.versatile.jms.client.common.msg.hampa.ValidateActiveHampaCardRequest;
import ir.caspco.versatile.jms.client.common.msg.hampa.ValidateActiveHampaCardResponse;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Davood Akbari - 1399
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

public class CheckIsActiveValidator implements ConstraintValidator<CheckIsActive, String> {

    @Override
    public boolean isValid(String cardNumber, ConstraintValidatorContext constraintValidatorContext) {

        if (cardNumber == null) {

            return true;
        }

        ValidateActiveHampaCardClient validateActiveHampaCardClient = ApplicationContextUtil.getBean(ValidateActiveHampaCardClient.class);


        ValidateActiveHampaCardRequest validateActiveHampaCardRequest = ValidateActiveHampaCardRequest.builder()
                .cardNumber(cardNumber)
                .build();

        ValidateActiveHampaCardResponse validateActiveHampaCardResponse = validateActiveHampaCardClient.send(validateActiveHampaCardRequest)
                .retrieve()
                .result()
                .orElseThrow(CoreContentResultException::new);


        return validateActiveHampaCardResponse.getResponse();
    }
}
