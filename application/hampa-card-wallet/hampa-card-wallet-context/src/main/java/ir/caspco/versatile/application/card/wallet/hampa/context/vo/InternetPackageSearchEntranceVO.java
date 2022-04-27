package ir.caspco.versatile.application.card.wallet.hampa.context.vo;


import ir.caspco.versatile.jms.client.common.enums.thirdparty.InternetPackageOperator;
import ir.caspco.versatile.jms.client.common.enums.thirdparty.PackageDurationCredit;
import ir.caspco.versatile.jms.client.common.enums.thirdparty.PackageType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
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
public class InternetPackageSearchEntranceVO {

    @NotNull
    private InternetPackageOperator operator;

    private PackageType packageType;

    private PackageDurationCredit durationCredit;

    private BigDecimal fromAmount;
    private BigDecimal toAmount;
}
