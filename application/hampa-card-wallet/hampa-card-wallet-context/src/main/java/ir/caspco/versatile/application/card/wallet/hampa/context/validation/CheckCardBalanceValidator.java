package ir.caspco.versatile.application.card.wallet.hampa.context.validation;

import ir.caspco.versatile.application.card.wallet.hampa.context.services.BalanceService;
import ir.caspco.versatile.application.card.wallet.hampa.context.validation.annotations.CheckBookBalanceByCard;
import ir.caspco.versatile.application.card.wallet.hampa.context.validation.annotations.ThisIsCardNumber;
import ir.caspco.versatile.application.card.wallet.hampa.context.validation.annotations.ThisIsSegment;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.CardSegmentBookBalanceEntranceVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.SegmentBookBalanceResultVO;
import ir.caspco.versatile.common.util.ApplicationContextUtil;
import ir.caspco.versatile.common.util.reflect.ReflectUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Davood Akbari - 1399
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

public class CheckCardBalanceValidator implements ConstraintValidator<CheckBookBalanceByCard, Object> {

    @Override
    public boolean isValid(Object target, ConstraintValidatorContext constraintValidatorContext) {

        Optional<String> segment = ReflectUtil.fieldValue(target, ThisIsSegment.class);
        Optional<String> cardNumber = ReflectUtil.fieldValue(target, ThisIsCardNumber.class);

        if (!segment.isPresent()) {

            return false;
        }

        if (!cardNumber.isPresent()) {

            return false;
        }

        BalanceService balanceService = ApplicationContextUtil.getBean(BalanceService.class);

        List<String> segments = new ArrayList<>();
        segments.add(segment.get());

        List<SegmentBookBalanceResultVO> segmentBookBalanceResults =
                balanceService.cardBalance(
                        CardSegmentBookBalanceEntranceVO.builder()
                                .cardNumber(cardNumber.get())
                                .segmentCodeList(segments)
                                .build()
                );


        if (segmentBookBalanceResults.isEmpty()) {

            return false;
        }

        return segmentBookBalanceResults.get(0).getBookBalance().compareTo(BigDecimal.ZERO) > 0;
    }
}
