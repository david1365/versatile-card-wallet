package ir.caspco.versatile.application.card.wallet.hampa.context.services;

import ir.caspco.versatile.jms.client.common.vo.RevertTranResponseVO;
import ir.caspco.versatile.jms.client.common.vo.hampa.RevertTransactionEntranceVO;
import ir.caspco.versatile.jms.client.common.vo.hampa.TranInqEntranceVO;
import ir.caspco.versatile.jms.client.common.vo.hampa.TranInqVO;

import java.util.List;

public interface TranInquiryService {

    List<TranInqVO> getTranInq(TranInqEntranceVO tranInqEntranceVO);

    RevertTranResponseVO reverseTransaction(RevertTransactionEntranceVO revertTransactionEntranceVO);
}
