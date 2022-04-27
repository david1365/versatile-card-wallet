package ir.caspco.versatile.application.card.wallet.hampa.context.services;

import ir.caspco.versatile.application.card.wallet.hampa.context.vo.AssignSegmentCodeEntranceVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.CardResultVO;

public interface AssignSegmentCodeService {

    CardResultVO assignSegmentCode(AssignSegmentCodeEntranceVO assignSegmentCodeEntranceVO);
}
