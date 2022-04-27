package ir.caspco.versatile.application.card.wallet.hampa.context.vo;

import ir.caspco.versatile.jms.client.common.vo.CardEmbossDataVO;
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
public class CardPhysicResultVO {

    private CardEmbossDataVO cardEmbossData;
    private CardPrintDataResultVO cardPrintData;
}
