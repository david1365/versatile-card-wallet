package ir.caspco.versatile.application.card.wallet.hampa.context.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;


@Data
@SuperBuilder
@NoArgsConstructor
public class ReIssueEntranceVO {

    @NonNull
    @NotBlank
    @NotEmpty
    private String cardNumber;
}
