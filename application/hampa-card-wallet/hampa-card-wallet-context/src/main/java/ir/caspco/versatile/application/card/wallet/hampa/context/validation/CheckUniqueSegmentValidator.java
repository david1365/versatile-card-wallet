package ir.caspco.versatile.application.card.wallet.hampa.context.validation;

import ir.caspco.versatile.application.card.wallet.hampa.context.repositories.WalletRepository;
import ir.caspco.versatile.application.card.wallet.hampa.context.validation.annotations.CheckUniqueSegment;
import ir.caspco.versatile.application.card.wallet.hampa.context.validation.annotations.ThisIsNationalCode;
import ir.caspco.versatile.application.card.wallet.hampa.context.validation.annotations.ThisIsSegmentList;
import ir.caspco.versatile.common.util.ApplicationContextUtil;
import ir.caspco.versatile.common.util.reflect.ReflectUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Optional;

/**
 * @author Davood Akbari - 1399
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

public class CheckUniqueSegmentValidator implements ConstraintValidator<CheckUniqueSegment, Object> {

    @Override
    public boolean isValid(Object target, ConstraintValidatorContext constraintValidatorContext) {

        Optional<List<String>> segments = ReflectUtil.fieldValue(target, ThisIsSegmentList.class);
        Optional<String> nationalCode = ReflectUtil.fieldValue(target, ThisIsNationalCode.class);

        if (!segments.isPresent()) {

            return false;
        }

        if (!nationalCode.isPresent()) {

            return false;
        }

        WalletRepository walletRepository = ApplicationContextUtil.getBean(WalletRepository.class);

        return !walletRepository.existsByCard_Person_NationalCodeAndSegmentCodeIn(nationalCode.get(), segments.get());
    }
}
