package ir.caspco.versatile.application.card.wallet.hampa.context.services.impl;

import ir.caspco.versatile.application.card.wallet.hampa.context.services.BillPaymentService;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.CardBillPaymentEntranceVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.BillPaymentResultVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.WalletBillPaymentEntranceVO;
import ir.caspco.versatile.context.jms.client.exceptions.CoreException;
import ir.caspco.versatile.jms.client.common.client.hampa.BillPaymentByHampaCardClient;
import ir.caspco.versatile.jms.client.common.msg.hampa.BillPaymentByHampaCardRequest;
import ir.caspco.versatile.jms.client.common.msg.hampa.BillPaymentByHampaCardResponse;
import ir.caspco.versatile.jms.client.common.vo.*;
import ir.caspco.versatile.jms.client.common.vo.hampa.BillPaymentByHampaCardRequestVO;
import ir.caspco.versatile.jms.client.common.vo.hampa.BillPaymentByHampaCardResponseVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(noRollbackFor = CoreException.class)
public class BillPaymentServiceImpl implements BillPaymentService {

    private final BillPaymentByHampaCardClient billPaymentByHampaCardClient;

    public BillPaymentServiceImpl(BillPaymentByHampaCardClient billPaymentByHampaCardClient) {
        this.billPaymentByHampaCardClient = billPaymentByHampaCardClient;
    }
    
    @Value("${wallet-hampa.defaultThirdPartyConfigId}")
    private Long thirdPartyConfigId;

    @Override
    public BillPaymentResultVO billPaymentByCard(CardBillPaymentEntranceVO cardBillPaymentEntranceVO) {

        BillPaymentByHampaCardRequest billPaymentByHampaCardRequest = BillPaymentByHampaCardRequest.builder()
                .billPaymentByHampaCardRequest(
                        BillPaymentByHampaCardRequestVO.builder()
                                .hampaBillCriteria(createHampaBillCriteriaVO(cardBillPaymentEntranceVO.getHampaBillCriteria()))
                                .thirdPartyConfig(ThirdPartyConfigVO.builder().id(thirdPartyConfigId).build())
                                .build())
                .build();

        BillPaymentByHampaCardResponse billPaymentByHampaCardResponse = billPaymentByHampaCardClient.send(billPaymentByHampaCardRequest)
                .onError(error -> {System.out.println("ERROR IN BILL PAYMENT!");})
                .throwException()
                .retrieve()
                .result()
                .orElse(BillPaymentByHampaCardResponse.builder()
                        .billPaymentByHampaCardResponseVO(BillPaymentByHampaCardResponseVO.builder().build())
                        .build());

        return BillPaymentResultVO.builder()
                .payedBillInformation(createPayedBillInformationVO(billPaymentByHampaCardResponse.getBillPaymentByHampaCardResponseVO().getPayedBillInformation()))
                .build();
    }

    @Override
    public BillPaymentResultVO billPaymentByWallet(WalletBillPaymentEntranceVO walletBillPaymentEntranceVO) {
        return null;
    }

    PaymentInformationVO createPaymentInformationVO(PaymentInformationVO paymentInformation) {
        return PaymentInformationVO.builder()
                .accountRef(paymentInformation.getAccountRef())
                .isCash(paymentInformation.getIsCash())
                .amount(paymentInformation.getAmount())
                .signersRow(paymentInformation.getSignersRow())
                .freeCharge(paymentInformation.getFreeCharge())
                .ChequeDate(paymentInformation.getChequeDate())
                .ChequeNumber(paymentInformation.getChequeNumber())
                .neginAccNumber(paymentInformation.getNeginAccNumber())
                .paymentType(paymentInformation.getPaymentType())
                .build();
    }

    HampaBillCriteriaVO createHampaBillCriteriaVO(HampaBillCriteriaVO hampaBillCriteria) {
        return HampaBillCriteriaVO.builder()
                .segmentCode(hampaBillCriteria.getSegmentCode())
                .paymentNumber(hampaBillCriteria.getPaymentNumber())
                .billNumber(hampaBillCriteria.getBillNumber())
                .customerNumber(hampaBillCriteria.getCustomerNumber())
                .cardNumber(hampaBillCriteria.getCardNumber())
                .accountNumber(hampaBillCriteria.getAccountNumber())
                .trackingNumber(hampaBillCriteria.getTrackingNumber())
                .paymentInformation(createPaymentInformationVO(hampaBillCriteria.getPaymentInformation()))
                .rrn(hampaBillCriteria.getRrn())
                .channelType(hampaBillCriteria.getChannelType())
                .build();
    }

    PayedBillInformationVO createPayedBillInformationVO(PayedBillInformationVO payedBillInformation) {
        return PayedBillInformationVO.builder()
                .trackingNumber(payedBillInformation.getTrackingNumber())
                .reference(payedBillInformation.getReference())
                .paymentDate(payedBillInformation.getPaymentDate())
                .channelType(payedBillInformation.getChannelType())
                .billAmount(payedBillInformation.getBillAmount())
                .billId(payedBillInformation.getBillId())
                .billType(payedBillInformation.getBillType())
                .billPayment(payedBillInformation.getBillPayment())
                .billTitle(payedBillInformation.getBillTitle())
                .build();
    }
}
