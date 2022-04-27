package ir.caspco.versatile.application.card.wallet.hampa.context.services.impl;

import ir.caspco.versatile.application.card.wallet.hampa.context.domains.PersonEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.CreditCardService;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.PersonService;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.RegisterService;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.SegmentMapService;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.CreditCardInquireResultVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.CreditCardRegistrationEntranceVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.CreditCardRegistrationResultVO;
import ir.caspco.versatile.common.util.Shift;
import ir.caspco.versatile.jms.client.common.client.LoadCompanyPlanClient;
import ir.caspco.versatile.jms.client.common.client.hampa.InquireCreditCardClient;
import ir.caspco.versatile.jms.client.common.client.hampa.RegisterCreditCardClient;
import ir.caspco.versatile.jms.client.common.exceptions.CoreContentResultException;
import ir.caspco.versatile.jms.client.common.msg.hampa.InquireCreditCardRequest;
import ir.caspco.versatile.jms.client.common.msg.hampa.InquireCreditCardResponse;
import ir.caspco.versatile.jms.client.common.msg.hampa.RegisterCreditCardRequest;
import ir.caspco.versatile.jms.client.common.msg.hampa.RegisterCreditCardResponse;
import ir.caspco.versatile.jms.client.common.vo.CompanyPlanRequestVO;
import ir.caspco.versatile.jms.client.common.vo.SegmentCompanyPlanVO;
import ir.caspco.versatile.jms.client.common.vo.hampa.RegisterCreditCardEntranceVO;
import ir.caspco.versatile.jms.client.common.vo.hampa.RegisterCreditCardResultVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Service
@Transactional
public class CreditCardServiceImpl implements CreditCardService {

    @Value("${cardWallet.creditCard.companyCode:C04}")
    private String companyCode;

    @Value("${cardWallet.creditCard.requesterCode:HAMPA}")
    private String requesterCode;


    private final RegisterService registerService;
    private final PersonService personService;
    private final SegmentMapService segmentMapService;

    private final RegisterCreditCardClient registerCreditCardClient;
    private final InquireCreditCardClient inquireCreditCardClient;
    private final LoadCompanyPlanClient loadCompanyPlanClient;


    public CreditCardServiceImpl(RegisterService registerService,
                                 PersonService personService,
                                 SegmentMapService segmentMapService,

                                 RegisterCreditCardClient registerCreditCardClient,
                                 InquireCreditCardClient inquireCreditCardClient,
                                 LoadCompanyPlanClient loadCompanyPlanClient) {

        this.registerService = registerService;
        this.personService = personService;
        this.segmentMapService = segmentMapService;

        this.registerCreditCardClient = registerCreditCardClient;
        this.inquireCreditCardClient = inquireCreditCardClient;
        this.loadCompanyPlanClient = loadCompanyPlanClient;
    }

    @Override
    public List<SegmentCompanyPlanVO> loadCompanyPlan() {

        return loadCompanyPlanClient.loadCompanyPlan(
                        CompanyPlanRequestVO.builder()
                                .requesterCode(requesterCode)
                                .companyCode(companyCode)
                                .build()
                ).stream()
//                .filter(segmentCompanyPlan -> segmentMapService.isAcceptableSegment(segmentCompanyPlan.getSegmentCode()))
                .peek(segmentCompanyPlan ->
                        segmentCompanyPlan.setSegmentCode(segmentMapService.toSegment(segmentCompanyPlan.getSegmentCode()))
                )
                .collect(Collectors.toList());
    }

    @Override
    public CreditCardRegistrationResultVO registerCreditCard(CreditCardRegistrationEntranceVO creditCardRegistrationEntrance) {

        String clientTrackingCode = toUpperUUID(creditCardRegistrationEntrance);

        RegisterCreditCardRequest registerCreditCardRequest = RegisterCreditCardRequest.builder()
                .registerCreditCardEntrance(
                        RegisterCreditCardEntranceVO.builder()
                                .nationalCode(creditCardRegistrationEntrance.getNationalCode())
                                .clientTrackingCode(clientTrackingCode)
                                .loanAmount(creditCardRegistrationEntrance.getLoanAmount())
                                .companyPlanID(creditCardRegistrationEntrance.getCompanyPlanID())
                                .build()
                )
                .build();

        RegisterCreditCardResponse registerCreditCardResponse = registerCreditCardClient.send(registerCreditCardRequest)
                .retrieve()
                .result()
                .orElseThrow(CoreContentResultException::new);

        return toCreditCardRegistrationResultVO(registerCreditCardResponse.getRegisterCreditCardResult());
    }

    @Override
    public CreditCardInquireResultVO inquireCreditCard(String nationalCode) {

        InquireCreditCardRequest inquireCreditCardRequest = InquireCreditCardRequest.builder()
                .nationalCode(nationalCode)
                .build();

        InquireCreditCardResponse inquireCreditCardResponse = inquireCreditCardClient.send(inquireCreditCardRequest)
                .retrieve()
                .result()
                .orElseThrow(CoreContentResultException::new);

        RegisterCreditCardResultVO registerCreditCardResult = inquireCreditCardResponse.getRegisterCreditCardResult();


        return registerService.register(registerCreditCardResult);
    }

    private CreditCardRegistrationResultVO toCreditCardRegistrationResultVO(
            RegisterCreditCardResultVO registerCreditCardResult) {

        CreditCardRegistrationResultVO creditCardRegistrationResult =
                Shift.just(registerCreditCardResult)
                        .toShift(CreditCardRegistrationResultVO.class)
                        .toObject();

        PersonEntity person = personService.findByNationalCode(registerCreditCardResult.getNationalCode());

        creditCardRegistrationResult.setMobileNumber(person.getMobileNumber());

        return creditCardRegistrationResult;
    }

    private String toUpperUUID(CreditCardRegistrationEntranceVO creditCardRegistrationEntrance) {

        return creditCardRegistrationEntrance.getClientTrackingCode().replace("-", "").toUpperCase();
    }
}
