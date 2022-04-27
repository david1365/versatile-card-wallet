package ir.caspco.versatile.application.card.wallet.hampa.context.vo;

import ir.caspco.versatile.jms.client.common.vo.hampa.HampaCardVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class HampaCardResultVO extends HampaCardVO {

    private String customerNumber;
    private String mobileNumber;

    private Set<ListWalletResultVO> walletResults;
}
