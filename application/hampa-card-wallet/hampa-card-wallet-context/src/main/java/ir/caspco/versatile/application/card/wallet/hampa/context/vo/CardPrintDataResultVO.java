package ir.caspco.versatile.application.card.wallet.hampa.context.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Data
@SuperBuilder
@NoArgsConstructor
public class CardPrintDataResultVO {

    private String cardNumber;
    private String persianName;
    private String englishName;
    private String cvv2;
    private String issueDate;
    private String persianExpireDate;
    private String gregorianExpireDate;
}
