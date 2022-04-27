package ir.caspco.versatile.application.card.wallet.hampa.context.vo;

import ir.caspco.versatile.jms.client.common.enums.FlowLevel;
import ir.caspco.versatile.jms.client.common.enums.FlowStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode
public class CreditCardRegistrationResultVO {

    private String nationalCode;
    private String mobileNumber;
    private Long customerNumber;
    private String macnaCode;
    private BigDecimal loanAmount;
    private Long loanFileNumber;
    private FlowLevel currentLevel;
    private FlowStatus currentStatus;
    private String exceptionCode;
    private String exceptionArgs;

    private Boolean completed;

    private String clientTrackingCode;
}
