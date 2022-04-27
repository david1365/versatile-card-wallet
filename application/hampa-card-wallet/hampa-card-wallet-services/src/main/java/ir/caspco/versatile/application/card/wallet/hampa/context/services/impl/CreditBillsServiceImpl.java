package ir.caspco.versatile.application.card.wallet.hampa.context.services.impl;

import ir.caspco.versatile.application.card.wallet.hampa.context.domains.CardEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.domains.CreditBillPaymentLogEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.domains.PersonEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.domains.WalletEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.CardService;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.CreditBillsService;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.PersonService;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.WalletService;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.log.CreditBillPaymentLogService;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.*;
import ir.caspco.versatile.common.util.Shift;
import ir.caspco.versatile.context.jms.client.exceptions.CoreException;
import ir.caspco.versatile.jms.client.common.client.GetMultiSegmentBillInfoForCollectClient;
import ir.caspco.versatile.jms.client.common.client.InsertMultiSegmentCollectPaymentClient;
import ir.caspco.versatile.jms.client.common.client.LoadBillsClient;
import ir.caspco.versatile.jms.client.common.enums.CardType;
import ir.caspco.versatile.jms.client.common.exceptions.CoreContentResultException;
import ir.caspco.versatile.jms.client.common.msg.InsertMultiSegmentCollectPaymentRequest;
import ir.caspco.versatile.jms.client.common.msg.InsertMultiSegmentCollectPaymentResponse;
import ir.caspco.versatile.jms.client.common.vo.BillCollectionInfoVO;
import ir.caspco.versatile.jms.client.common.vo.CollectPaymentRequestVO;
import ir.caspco.versatile.jms.client.common.vo.CollectPaymentResponseVO;
import ir.caspco.versatile.jms.client.common.vo.CreditCardBillVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Service
@Transactional(noRollbackFor = CoreException.class)
public class CreditBillsServiceImpl implements CreditBillsService {

    private final GetMultiSegmentBillInfoForCollectClient getMultiSegmentBillInfoForCollectClient;
    private final InsertMultiSegmentCollectPaymentClient insertMultiSegmentCollectPaymentClient;
    private final LoadBillsClient loadBillsClient;

    private final CardService cardService;
    private final WalletService walletService;
    private final PersonService personService;
    private final CreditBillPaymentLogService creditBillPaymentLogService;
    private final SegmentMapServiceImpl segmentMapService;


    public CreditBillsServiceImpl(GetMultiSegmentBillInfoForCollectClient getMultiSegmentBillInfoForCollectClient,
                                  InsertMultiSegmentCollectPaymentClient insertMultiSegmentCollectPaymentClient,
                                  LoadBillsClient loadBillsClient,

                                  CardService cardService,
                                  WalletService walletService,
                                  PersonService personService,

                                  CreditBillPaymentLogService creditBillPaymentLogService,
                                  SegmentMapServiceImpl segmentMapService) {

        this.getMultiSegmentBillInfoForCollectClient = getMultiSegmentBillInfoForCollectClient;
        this.insertMultiSegmentCollectPaymentClient = insertMultiSegmentCollectPaymentClient;
        this.loadBillsClient = loadBillsClient;

        this.cardService = cardService;
        this.walletService = walletService;
        this.personService = personService;

        this.creditBillPaymentLogService = creditBillPaymentLogService;
        this.segmentMapService = segmentMapService;
    }

    @Override
    public CreditBillsResultVO creditBillsByCardNumber(String cardNumber) {

        final CardEntity creditCard = getCreditCard(cardNumber);

        return creditBills(creditCard);
    }

    @Override
    public CreditBillsResultVO creditBillsByNationalCode(String nationalCode) {

        final PersonEntity person = personService.findByNationalCode(nationalCode);
        final CardEntity creditCard = cardService.findCreditCardByPerson(person);

        return creditBills(creditCard);
    }

    @Override
    public CreditBillsResultVO creditBillsByMobileNumber(String mobileNumber) {

        PersonEntity person = personService.findByMobileNumber(mobileNumber);
        CardEntity creditCard = cardService.findCreditCardByPerson(person);

        return creditBills(creditCard);
    }

    @Override
    public CreditCardBillResultVO loadBillsByCardNumber(String cardNumber) {

        CardEntity card = cardService.findByCardNumber(cardNumber);

        return loadBills(card.getPerson());
    }

    @Override
    public CreditCardBillResultVO loadBillsByNationalCode(String nationalCode) {

        PersonEntity person = personService.findByNationalCode(nationalCode);

        return loadBills(person);
    }

    @Override
    public CreditCardBillResultVO loadBillsByMobileNumber(String mobileNumber) {

        PersonEntity person = personService.findByMobileNumber(mobileNumber);

        return loadBills(person);
    }

    @Override
    public CollectiveCreditBillsPaymentResultVO collectiveCreditBillPaymentByCard(
            CardCollectiveCreditBillPaymentEntranceVO cardCollectiveCreditBillPaymentEntrance) {

        final String cardNumber = cardCollectiveCreditBillPaymentEntrance.getCardNumber();
        final String externalRef = cardCollectiveCreditBillPaymentEntrance.getExternalRef();

        final CardEntity creditCard = getCreditCard(cardNumber);

        List<CollectPaymentRequestVO> collectPaymentRequests = cardCollectiveCreditBillPaymentEntrance.getSegmentsInfos().stream()
                .map(segmentInfo -> {

                    CollectPaymentRequestVO collectPaymentRequest = Shift.just(segmentInfo)
                            .toShift(CollectPaymentRequestVO.class)
                            .toObject();

                    collectPaymentRequest.setCardNumber(creditCard.getCardNumber());
                    collectPaymentRequest.setExternalRef(externalRef);

                    collectPaymentRequest.setClientTrackingCode(segmentInfo.getClientTrackingCode());

                    return collectPaymentRequest;
                })
                .collect(Collectors.toList());

        return collectiveCreditBillPayment(cardNumber, creditCard, collectPaymentRequests);
    }

    @Override
    public CollectiveCreditBillsPaymentResultVO collectiveCreditBillPaymentByWallet(
            WalletCollectiveCreditBillPaymentEntranceVO walletCollectiveCreditBillPaymentEntrance) {

        final String externalRef = walletCollectiveCreditBillPaymentEntrance.getExternalRef();

        final UUID walletId = walletCollectiveCreditBillPaymentEntrance.getWalletsInfos().get(0).getWalletId();
        final WalletEntity wallet = walletService.findByWalletId(walletId);

        final String cardNumber = wallet.getCard().getCardNumber();
        final CardEntity creditCard = getCreditCard(cardNumber);

        Set<WalletEntity> creditWallets = walletService.findByCardAndStatusTrue(creditCard);

        List<CollectPaymentRequestVO> collectPaymentRequests = walletCollectiveCreditBillPaymentEntrance.getWalletsInfos().stream()
                .map(walletInfo -> {

                    WalletEntity walletFound = walletService.findByWalletIdAndStatusTrue(creditWallets, walletInfo.getWalletId());

                    return CollectPaymentRequestVO.builder()
                            .cardNumber(cardNumber)
                            .segmentCode(walletFound.getSegmentCode())
                            .externalRef(externalRef)
                            .amount(walletInfo.getAmount())
                            .clientTrackingCode(walletInfo.getClientTrackingCode())
                            .build();
                })
                .collect(Collectors.toList());

        return collectiveCreditBillPayment(cardNumber, creditCard, collectPaymentRequests);
    }

    @Override
    public CreditBillsPaymentResultVO creditBillPaymentByCard(CardCreditBillPaymentEntranceVO cardCreditBillPaymentEntrance) {

        final String cardNumber = cardCreditBillPaymentEntrance.getCardNumber();
        final CardEntity creditCard = getCreditCard(cardNumber);
        final String clientTrackingCode = cardCreditBillPaymentEntrance.getClientTrackingCode();

        WalletEntity wallet =
                walletService.findByCardAndSegmentCodeAndStatusTrue(creditCard, cardCreditBillPaymentEntrance.getSegmentCode());

        CollectPaymentRequestVO collectPaymentRequest = Shift.just(cardCreditBillPaymentEntrance)
                .toShift(CollectPaymentRequestVO.class)
                .toObject();
        collectPaymentRequest.setCardNumber(creditCard.getCardNumber());

        return creditBillPayment(cardNumber, wallet, collectPaymentRequest, clientTrackingCode);
    }

    @Override
    public CreditBillsPaymentResultVO creditBillPaymentByWallet(WalletCreditBillPaymentEntranceVO walletCreditBillPaymentEntrance) {

        final WalletEntity wallet = walletService.findByWalletIdAndStatusTrue(walletCreditBillPaymentEntrance.getWalletId());

        final String cardNumber = wallet.getCard().getCardNumber();
        final CardEntity creditCard = getCreditCard(cardNumber);
        final String clientTrackingCode = walletCreditBillPaymentEntrance.getClientTrackingCode();

        CollectPaymentRequestVO collectPaymentRequest = Shift.just(walletCreditBillPaymentEntrance)
                .toShift(CollectPaymentRequestVO.class)
                .toObject();
        collectPaymentRequest.setCardNumber(creditCard.getCardNumber());
        collectPaymentRequest.setSegmentCode(wallet.getSegmentCode());

        return creditBillPayment(cardNumber, wallet, collectPaymentRequest, clientTrackingCode);
    }

    private CardEntity getCreditCard(String cardNumber) {

        final CardEntity card = cardService.findByCardNumber(cardNumber);

        return cardService.findCreditCardByPerson(card.getPerson());
    }

    private CreditBillsResultVO creditBills(CardEntity creditCard) {

        CardEntity debitCard = cardService.findByPersonAndCardType(creditCard.getPerson(), CardType.DEBIT);
        final String creditCardNumber = creditCard.getCardNumber();

        List<BillCollectionInfoVO> billCollectionInfos = getMultiSegmentBillInfoForCollectClient.creditBills(creditCardNumber);

        Set<WalletEntity> creditWallets = walletService.findByCardAndStatusTrue(creditCard);

        return CreditBillsResultVO.builder()
                .cardNumber(debitCard.getCardNumber())
                .billCollectionInfos(
                        billCollectionInfos.stream()
                                .map(billCollectionInfo -> {

                                    BillCollectionInfoResultVO billCollectionInfoResult = Shift.just(billCollectionInfo)
                                            .toShift(BillCollectionInfoResultVO.class)
                                            .toObject();

                                    String modernSegmentCode = segmentMapService.toSegment(billCollectionInfo.getSegmentCode());
                                    WalletEntity wallet = walletService.findBySegmentCodeAndStatusTrue(creditWallets, modernSegmentCode);

                                    billCollectionInfoResult.setSegmentCode(modernSegmentCode);
                                    billCollectionInfoResult.setWalletId(wallet.getWalletId());

                                    return billCollectionInfoResult;
                                })
                                .collect(Collectors.toList())
                )
                .build();
    }

    private CollectiveCreditBillsPaymentResultVO collectiveCreditBillPayment(String cardNumber,
                                                                             CardEntity creditCard,
                                                                             List<CollectPaymentRequestVO> collectPaymentRequests) {

        List<CollectPaymentResponseVO> collectPaymentResponses = collectiveCreditBillPayment(collectPaymentRequests);

        Set<WalletEntity> creditWallet = walletService.findByCardAndStatusTrue(creditCard);

        return CollectiveCreditBillsPaymentResultVO.builder()
                .cardNumber(cardNumber)
                .collectPaymentResults(
                        collectPaymentResponses.stream()
                                .map(collectPaymentResponse -> {

                                    CollectPaymentResultVO collectPaymentResult = Shift.just(collectPaymentResponse)
                                            .toShift(CollectPaymentResultVO.class)
                                            .toObject();

                                    String segmentCode = collectPaymentResponse.getSegmentCode();
                                    String modernSegmentCode = segmentMapService.toSegment(segmentCode);
                                    WalletEntity wallet = walletService.findBySegmentCodeAndStatusTrue(creditWallet, modernSegmentCode);

                                    collectPaymentResult.setWalletId(wallet.getWalletId());
                                    collectPaymentResult.setSegmentCode(modernSegmentCode);

                                    collectPaymentResult.setServerTrackingCode(collectPaymentResponse.getRequestTrackingNumber());
                                    collectPaymentResult.setDealReference(collectPaymentResponse.getDealReference());

                                    final String clientTrackingCode =
                                            getClientTrackingCode(creditCard.getCardNumber(), collectPaymentRequests, segmentCode);
                                    collectPaymentResult.setClientTrackingCode(clientTrackingCode);

                                    return collectPaymentResult;

                                })
                                .collect(Collectors.toList())
                )
                .build();
    }

    private String getClientTrackingCode(String creditCardNumber,
                                         List<CollectPaymentRequestVO> collectPaymentRequests,
                                         String segmentCode) {

        return collectPaymentRequests.stream()
                .filter(collectPaymentRequest ->
                        segmentCode.equals(collectPaymentRequest.getSegmentCode()) &&
                                creditCardNumber.equals(collectPaymentRequest.getCardNumber()))
                .findAny()
                .map(CollectPaymentRequestVO::getClientTrackingCode)
                .orElse(null);
    }

    private CreditBillsPaymentResultVO creditBillPayment(String cardNumber,
                                                         WalletEntity wallet,
                                                         CollectPaymentRequestVO collectPaymentRequest,
                                                         String clientTrackingCode) {

        List<CollectPaymentRequestVO> collectPaymentRequests = new ArrayList<>();
        collectPaymentRequests.add(collectPaymentRequest);

        List<CollectPaymentResponseVO> collectPaymentResponses = collectiveCreditBillPayment(collectPaymentRequests);

        return collectPaymentResponses.stream()
                .map(collectPaymentResponse -> {

                    CreditBillsPaymentResultVO creditBillsPaymentResult = Shift.just(collectPaymentResponse)
                            .toShift(CreditBillsPaymentResultVO.class)
                            .toObject();

                    creditBillsPaymentResult.setCardNumber(cardNumber);
                    creditBillsPaymentResult.setSegmentCode(wallet.getSegmentCode());
                    creditBillsPaymentResult.setWalletId(wallet.getWalletId());

                    creditBillsPaymentResult.setServerTrackingCode(collectPaymentResponse.getRequestTrackingNumber());
                    creditBillsPaymentResult.setDealReference(collectPaymentResponse.getDealReference());
                    creditBillsPaymentResult.setClientTrackingCode(clientTrackingCode);

                    return creditBillsPaymentResult;
                })
                .findFirst()
                .orElseThrow(CoreContentResultException::new);
    }

    private List<CollectPaymentResponseVO> collectiveCreditBillPayment(List<CollectPaymentRequestVO> collectPaymentRequests) {

        List<CreditBillPaymentLogEntity> creditBillPaymentLogs =
                creditBillPaymentLogService.createAndPeek(collectPaymentRequests);

        InsertMultiSegmentCollectPaymentRequest insertMultiSegmentCollectPaymentRequest =
                InsertMultiSegmentCollectPaymentRequest.builder()
                        .collectPaymentRequests(
                                collectPaymentRequests
                        )
                        .build();

        InsertMultiSegmentCollectPaymentResponse insertMultiSegmentCollectPaymentResponse =
                insertMultiSegmentCollectPaymentClient.send(insertMultiSegmentCollectPaymentRequest)
                        .onError(throwable -> creditBillPaymentLogService.fail(creditBillPaymentLogs))
                        .throwException()
                        .retrieve()
                        .result()
                        .orElseThrow(CoreContentResultException::new);

        List<CollectPaymentResponseVO> collectPaymentResponses =
                insertMultiSegmentCollectPaymentResponse.getCollectPaymentResponses();

        creditBillPaymentLogService.success(collectPaymentResponses, creditBillPaymentLogs);

        return collectPaymentResponses;
    }

    public CreditCardBillResultVO loadBills(PersonEntity person) {

        CardEntity creditCard = cardService.findCreditCardByPerson(person);
        CardEntity debitCard = cardService.findDebitCardByPerson(person);

        List<CreditCardBillVO> bills = loadBillsClient.loadBills(creditCard.getCardNumber());

        if (bills != null && !bills.isEmpty()) {

            CreditCardBillVO creditCardBill = bills.get(0);

            CreditCardBillResultVO creditCardBillResult = Shift.just(creditCardBill)
                    .toShift(CreditCardBillResultVO.class)
                    .toObject();

            creditCardBillResult.setCardNumber(debitCard.getCardNumber());

            creditCardBillResult.setCreditCardBillDetail(
                    bills.stream()
                            .map(creditCardBillIn -> {

                                CreditCardBillDetailResultVO creditCardBillDetailResult =
                                        Shift.just(creditCardBillIn)
                                                .toShift(CreditCardBillDetailResultVO.class)
                                                .toObject();

                                creditCardBillDetailResult.setCreditCardTransaction(creditCardBillIn.getCreditCardTransactionDTO());

                                return creditCardBillDetailResult;
                            })
                            .collect(Collectors.toList())
            );

            return creditCardBillResult;
        }

        return null;
    }
}
