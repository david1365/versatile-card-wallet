package ir.caspco.versatile.application.card.wallet.hampa.context.vo;

import ir.caspco.versatile.application.card.wallet.hampa.context.validation.annotations.CheckUniqueSegment;
import ir.caspco.versatile.application.card.wallet.hampa.context.validation.annotations.ThisIsNationalCode;
import ir.caspco.versatile.application.card.wallet.hampa.context.validation.annotations.ThisIsSegmentList;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@CheckUniqueSegment
public class AssignSegmentCodeEntranceVO {

    @NotNull
    @NotEmpty
    @ThisIsSegmentList
    private List<String> segmentCodes;


    @NotNull
    @NotBlank
    @NotEmpty
    @ThisIsNationalCode
    private String nationalCode;
}
