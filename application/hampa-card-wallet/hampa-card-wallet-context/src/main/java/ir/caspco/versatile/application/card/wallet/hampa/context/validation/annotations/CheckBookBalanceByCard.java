package ir.caspco.versatile.application.card.wallet.hampa.context.validation.annotations;

import ir.caspco.versatile.application.card.wallet.hampa.context.validation.CheckCardBalanceValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author Davood Akbari - 1399
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CheckCardBalanceValidator.class)
@Documented
public @interface CheckBookBalanceByCard {
    String message() default "{ir.caspco.versatile.application.card.wallet.hampa.context.validation.annotations.CheckBookBalanceByCard.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        CheckBookBalanceByCard[] value();
    }
}