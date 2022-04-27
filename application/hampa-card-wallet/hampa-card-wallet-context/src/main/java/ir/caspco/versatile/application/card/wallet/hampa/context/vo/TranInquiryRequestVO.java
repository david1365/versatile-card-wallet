package ir.caspco.versatile.application.card.wallet.hampa.context.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class TranInquiryRequestVO {


    private String uniqueTrackingCode;
    private String serverTrackingCode;
}
