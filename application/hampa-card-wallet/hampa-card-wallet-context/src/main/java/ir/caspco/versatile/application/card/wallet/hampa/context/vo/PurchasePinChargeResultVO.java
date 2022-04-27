package ir.caspco.versatile.application.card.wallet.hampa.context.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 * @author Mohammad Javad Zahedi - 2021
 * mjavadzahedi0@gmail.com
 * 09377732642
 */

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties({"thirdPartyResponse", "mobileNumber"})
public class PurchasePinChargeResultVO extends PurchaseThirdPartyFinalResultVO {

    private String serial;
    private String password;
    private Date paymentDate;
    private Long paymentServiceId;
}
