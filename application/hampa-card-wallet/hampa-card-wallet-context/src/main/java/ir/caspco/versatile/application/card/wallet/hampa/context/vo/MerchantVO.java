package ir.caspco.versatile.application.card.wallet.hampa.context.vo;

import ir.caspco.versatile.context.validation.groups.DDelete;
import ir.caspco.versatile.context.validation.groups.DInsert;
import ir.caspco.versatile.context.validation.groups.DSelective;
import ir.caspco.versatile.context.validation.groups.DUpdate;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */


//TODO from davood akbari: Validation for account number.
@Data
@SuperBuilder
@NoArgsConstructor
public class MerchantVO {

    @NotNull(groups = {DUpdate.class, DDelete.class, DSelective.class})
    private UUID merchantId;

    @NotNull(groups = {DUpdate.class, DInsert.class})
    @NotBlank(groups = {DUpdate.class, DInsert.class})
    @NotEmpty(groups = {DUpdate.class, DInsert.class})
    private String chargeInternalAccount;

    @NotNull(groups = {DUpdate.class, DInsert.class})
    @NotBlank(groups = {DUpdate.class, DInsert.class})
    @NotEmpty(groups = {DUpdate.class, DInsert.class})
    private String purchaseInternalAccount;

    @NotNull(groups = {DUpdate.class, DInsert.class})
    @NotBlank(groups = {DUpdate.class, DInsert.class})
    @NotEmpty(groups = {DUpdate.class, DInsert.class})
    private String chargeSettlementAccount;

    @NotNull(groups = {DUpdate.class, DInsert.class})
    @NotBlank(groups = {DUpdate.class, DInsert.class})
    @NotEmpty(groups = {DUpdate.class, DInsert.class})
    private String purchaseSettlementAccount;

    @NotNull(groups = {DUpdate.class, DInsert.class})
    @NotBlank(groups = {DUpdate.class, DInsert.class})
    @NotEmpty(groups = {DUpdate.class, DInsert.class})
    private String title;

//    @NotNull(groups = {DUpdate.class, DInsert.class})
//    @NotBlank(groups = {DUpdate.class, DInsert.class})
//    @NotEmpty(groups = {DUpdate.class, DInsert.class})
//    @IsValidCron(groups = {DUpdate.class, DInsert.class})
//    private String chargeCron;

    private Date chargeNextValidTime;

//    @NotNull(groups = {DUpdate.class, DInsert.class})
//    @NotBlank(groups = {DUpdate.class, DInsert.class})
//    @NotEmpty(groups = {DUpdate.class, DInsert.class})
//    @IsValidCron(groups = {DUpdate.class, DInsert.class})
//    private String purchaseCron;

    private Date purchaseNextValidTime;

    @NotNull(groups = DUpdate.class)
    private Boolean active;

    @NotNull(groups = DUpdate.class)
    private Timestamp version;
}
