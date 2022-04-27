package ir.caspco.versatile.application.card.wallet.hampa.context.validation;

import ir.caspco.versatile.application.card.wallet.hampa.context.domains.PersonEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.repositories.PersonRepository;
import ir.caspco.versatile.application.card.wallet.hampa.context.validation.annotations.HasRegistered;
import ir.caspco.versatile.common.util.ApplicationContextUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

/**
 * @author Davood Akbari - 1399
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

public class CheckHasRegisteredValidator implements ConstraintValidator<HasRegistered, String> {

    @Override
    public boolean isValid(String nationalCode, ConstraintValidatorContext constraintValidatorContext) {

        PersonRepository personRepository = ApplicationContextUtil.getBean(PersonRepository.class);

        Optional<PersonEntity> person = personRepository.findByNationalCode(nationalCode);

        return person.isPresent();
    }
}
