package ir.caspco.versatile.application.card.wallet.hampa.context.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class TranInquiryResultVO {

    private String clientTrackingCode;
    private String serverTrackingCode;
    private String businessTrackingCode;
    private Boolean checkUniqueTrackingCode;
    private String channel;
    private String messageName;
    private Integer rowUpdSeq;
}
