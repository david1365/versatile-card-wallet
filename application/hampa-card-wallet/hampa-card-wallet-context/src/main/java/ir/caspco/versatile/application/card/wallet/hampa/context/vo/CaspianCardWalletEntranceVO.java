package ir.caspco.versatile.application.card.wallet.hampa.context.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
public class CaspianCardWalletEntranceVO {

    //    @NotNull
//    @NotEmpty
    private List<String> segmentCodes;

    @NotNull
    @NotEmpty
    @NotBlank
    private String customerNumber;

}
