package ir.caspco.versatile.application.card.wallet.hampa.context.vo;

import ir.caspco.versatile.jms.client.common.enums.thirdparty.InternetPackageOperator;
import ir.caspco.versatile.jms.client.common.enums.thirdparty.ServiceType;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

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
public class PurchaseInternetPackageByCardEntranceVO extends PurchaseChargePackageByCardEntranceVO {

    @NotNull
    private InternetPackageOperator operator;

    @NotNull
    private Integer productId;

    @Builder.Default
    private ServiceType serviceType = ServiceType.INTERNET_PACKAGE;
}
