package ir.caspco.versatile.application.card.wallet.hampa.context.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Data
@SuperBuilder
@NoArgsConstructor
public class CreditCardBillResultVO {

    private Long clientId;
    private String firstName;
    private String lastName;
    private String nationalCode;
    private String fileNo;

    private String cardNumber;

    List<CreditCardBillDetailResultVO> creditCardBillDetail;
}
