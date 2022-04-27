package ir.caspco.versatile.application.card.wallet.hampa.context.services.impl;

import ir.caspco.versatile.application.card.wallet.hampa.context.domains.CardEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.domains.PersonEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.domains.WalletEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.exceptions.IssueCardResultException;
import ir.caspco.versatile.application.card.wallet.hampa.context.repositories.CardRepository;
import ir.caspco.versatile.application.card.wallet.hampa.context.repositories.PersonRepository;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.CardService;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.PersonService;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.RegisterService;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.WalletService;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.*;
import ir.caspco.versatile.common.util.Shift;
import ir.caspco.versatile.context.exceptions.DuplicateRecordException;
import ir.caspco.versatile.jms.client.common.client.CustomerClient;
import ir.caspco.versatile.jms.client.common.client.hampa.HampaCardListClient;
import ir.caspco.versatile.jms.client.common.client.hampa.IssueCardHampaClient;
import ir.caspco.versatile.jms.client.common.enums.CardType;
import ir.caspco.versatile.jms.client.common.enums.WalletType;
import ir.caspco.versatile.jms.client.common.msg.hampa.HampaCardListRequest;
import ir.caspco.versatile.jms.client.common.msg.hampa.HampaCardListResponse;
import ir.caspco.versatile.jms.client.common.msg.hampa.IssueCardHampaRequest;
import ir.caspco.versatile.jms.client.common.msg.hampa.IssueCardHampaResponse;
import ir.caspco.versatile.jms.client.common.vo.*;
import ir.caspco.versatile.jms.client.common.vo.hampa.HampaCardListRequestVO;
import ir.caspco.versatile.jms.client.common.vo.hampa.HampaCardListResponseVO;
import ir.caspco.versatile.jms.client.common.vo.hampa.HampaCardVO;
import ir.caspco.versatile.jms.client.common.vo.hampa.RegisterCreditCardResultVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Service
@Transactional
public class RegisterServiceImpl implements RegisterService {

    private final HampaCardListClient hampaCardListClient;
    private final CustomerClient customerClient;
    private final IssueCardHampaClient issueCardHampaClient;

    private final CardRepository cardRepository;
    private final PersonRepository personRepository;

    private final CardService cardService;
    private final PersonService personService;
    private final WalletService walletService;

//    @Value("${cardWallet.creditCardSegmentCode:10}")
//    private String creditCardSegmentCode;

    @Value("${cardWallet.cardProductId:643}")
    private Long cardProductId;

    @Value("${cardWallet.noIdentityNo.birthDate:1989-03-21}")
    private Date noIdentityNoBirthDate;

    @Value("${cardWallet.noIdentityNo.identityNo:0}")
    private String noIdentityNoIdentityNo;


    public RegisterServiceImpl(HampaCardListClient hampaCardListClient,
                               CustomerClient customerClient,
                               IssueCardHampaClient issueCardHampaClient,

                               CardRepository cardRepository,
                               PersonRepository personRepository,

                               CardService cardService,
                               PersonService personService,
                               WalletService walletService) {

        this.hampaCardListClient = hampaCardListClient;
        this.customerClient = customerClient;
        this.issueCardHampaClient = issueCardHampaClient;

        this.cardRepository = cardRepository;
        this.personRepository = personRepository;

        this.cardService = cardService;
        this.personService = personService;
        this.walletService = walletService;
    }

    @Override
    public CardResultVO register(CardWalletEntranceVO cardWalletEntrance) {

        IssueCardHampaResponse issueCardHampaResponse = issueCard(cardWalletEntrance);

        CardIssueHampaResponseVO cardIssueHampaResponse = getCardIssueHampaResponseVO(issueCardHampaResponse);

        CardEntity savedCard = save(cardWalletEntrance, cardIssueHampaResponse);

        CardResultVO cardResult = Shift.just(savedCard).toShift(CardResultVO.class).toObject();
        cardResult.setCustomerNumber(savedCard.getPerson().getCustomerNumber());
        cardResult.setNationalCode(savedCard.getPerson().getNationalCode());
        cardResult.setMobileNumber(savedCard.getPerson().getMobileNumber());

        return cardResult;

    }

    @Override
    public CardResultVO register(CaspianCardWalletEntranceVO caspianCardWalletEntrance) {

        CompleteCustomerInfoVO completeCustomerInfo = customerInfo(caspianCardWalletEntrance.getCustomerNumber());

        IssueCardHampaResponse issueCardHampaResponse = issueCard(caspianCardWalletEntrance, completeCustomerInfo);

        CardIssueHampaResponseVO cardIssueHampaResponse = getCardIssueHampaResponseVO(issueCardHampaResponse);

        CardEntity savedCard = save(completeCustomerInfo, cardIssueHampaResponse);

        return Shift.just(savedCard).toShift(CardResultVO.class).toObject();

    }

    @Override
    public CreditCardInquireResultVO register(RegisterCreditCardResultVO registerCreditCardResult) {

        CreditCardInquireResultVO creditCardInquireResult =
                Shift.just(registerCreditCardResult).toShift(CreditCardInquireResultVO.class).toObject();

        PersonEntity person = personService.findByNationalCode(registerCreditCardResult.getNationalCode());
        creditCardInquireResult.setMobileNumber(person.getMobileNumber());

        if (registerCreditCardResult.isCompleted()) {

            CardEntity newCard = cardRepository.findByCardNumber(registerCreditCardResult.getCardNumber())
                    .orElseGet(() -> {

                        person.setCustomerNumber(registerCreditCardResult.getCustomerNumber().toString());

                        CardEntity card = generateCard(registerCreditCardResult, person);
                        return cardRepository.save(card);
                    });

            WalletEntity wallet = walletService.findByCardNumberAndSegmentCode(
                    newCard.getCardNumber(),
                    registerCreditCardResult.getSegmentCode()
            );
            creditCardInquireResult.setWalletId(wallet.getWalletId());
            creditCardInquireResult.setAccountNumber(wallet.getAccountNumber());

            return creditCardInquireResult;
        }

        return creditCardInquireResult;
    }

    @Override
    public HampaCardResultVO list(HampaCardListRequestVO hampaCardListRequestVO) {

        HampaCardListRequest hampaCardListRequest = HampaCardListRequest.builder()
                .hampaCardListRequest(
                        hampaCardListRequestVO
                )
                .build();

        HampaCardListResponse hampaCardListResponse = hampaCardListClient.send(hampaCardListRequest)
                .retrieve()
                .result()
                .orElse(
                        HampaCardListResponse.builder()
                                .hampaCardListResponse(
                                        HampaCardListResponseVO.builder()
                                                .hampaCardDtoList(Collections.emptyList())
                                                .build()
                                )
                                .build()
                );

        HampaCardListResponseVO hampaCardList = hampaCardListResponse.getHampaCardListResponse();

        List<HampaCardResultVO> hampaCardResults = hampaCardList.getHampaCardDtoList().stream()
                .map(hampaCard -> {

                    CardEntity debitCard = cardService.findByCardNumber(hampaCard.getCardNumber());

                    Set<ListWalletResultVO> listWalletResults = getListWalletResultVOS(hampaCard, debitCard);

                    return HampaCardResultVO.builder()
                            .cardNumber(hampaCard.getCardNumber())
                            .embossedName(hampaCard.getEmbossedName())
                            .nationalCode(hampaCard.getNationalCode())
                            .status(hampaCard.getStatus())
                            .customerNumber(debitCard.getPerson().getCustomerNumber())
                            .mobileNumber(debitCard.getPerson().getMobileNumber())
                            .walletResults(listWalletResults)
                            .build();
                })
                .collect(Collectors.toList());

        return !hampaCardResults.isEmpty() ? hampaCardResults.get(0) : HampaCardResultVO.builder().build();
    }

    private Set<ListWalletResultVO> getListWalletResultVOS(HampaCardVO hampaCard, CardEntity debitCard) {

        Set<ListWalletResultVO> listWalletResults = debitCard.getWallets().stream()
                .map(wallet ->
                        ListWalletResultVO.builder()
                                .walletId(wallet.getWalletId())
                                .accountNumber(wallet.getAccountNumber())
                                .mainAccount(wallet.getMainAccount())
                                .segmentCode(wallet.getSegmentCode())
                                .status(wallet.getStatus())
                                .walletType(WalletType.DEBIT)
                                .build()
                )
                .collect(Collectors.toSet());

        Set<WalletEntity> creditWallets =
                walletService.findByNationalCodeAndCardType(hampaCard.getNationalCode(), CardType.CREDIT);

        listWalletResults.addAll(creditWallets.stream()
                .map(wallet ->
                        ListWalletResultVO.builder()
                                .walletId(wallet.getWalletId())
                                .accountNumber(wallet.getAccountNumber())
                                .mainAccount(wallet.getMainAccount())
                                .segmentCode(wallet.getSegmentCode())
                                .status(wallet.getStatus())
                                .walletType(WalletType.CREDIT)
                                .build()
                )
                .collect(Collectors.toSet()));

        return listWalletResults;
    }

    private CardIssueHampaResponseVO getCardIssueHampaResponseVO(IssueCardHampaResponse issueCardHampaResponse) {

        CardIssueHampaResponseVO cardIssueHampaResponse = issueCardHampaResponse.getCardIssueHampaResponse();
        if (cardIssueHampaResponse == null) {

            throw new IssueCardResultException("CardIssueHampaResponse is NULL!");

        }
        return cardIssueHampaResponse;
    }

    private CardEntity save(CompleteCustomerInfoVO completeCustomerInfo, CardIssueHampaResponseVO cardIssueHampaResponse) {

        PersonEntity person = PersonEntity.builder()
                .customerNumber(completeCustomerInfo.getCustomerNo())
                .nationalCode(completeCustomerInfo.getNationalNo())
                .mobileNumber(completeCustomerInfo.getMobileNo())
                .build();

        CardEntity card = generateCard(cardIssueHampaResponse, person);

        return cardRepository.save(card);
    }

    private CardEntity save(CardWalletEntranceVO cardWalletEntrance, CardIssueHampaResponseVO cardIssueHampaResponse) {

        PersonEntity person = PersonEntity.builder()
                .nationalCode(cardWalletEntrance.getNationalCode())
                .mobileNumber(cardWalletEntrance.getMobileNo())
                .build();

        CardEntity card = generateCard(cardIssueHampaResponse, person);

        return cardRepository.save(card);
    }

    private CardEntity generateCard(CardIssueHampaResponseVO cardIssueHampaResponse, PersonEntity person) {

        return CardEntity.builder()
                .cardNumber(cardIssueHampaResponse.getCardNumber())
                .cardType(CardType.DEBIT)
                .person(person)
                .wallets(
                        cardIssueHampaResponse.getCardAccounts().stream()
                                .map(cardAccountHampa ->
                                        WalletEntity.builder()
                                                .accountNumber(cardAccountHampa.getAccountNumber())
                                                .mainAccount(cardAccountHampa.getMainAccount())
                                                .segmentCode(cardAccountHampa.getSegmentCode())
                                                .status(cardAccountHampa.getStatus())
                                                .build()
                                )
                                .collect(Collectors.toSet())
                )
                .build();
    }

    private CardEntity generateCard(RegisterCreditCardResultVO registerCreditCardResult, PersonEntity person) {

        return CardEntity.builder()
                .cardNumber(registerCreditCardResult.getCardNumber())
                .cardType(CardType.CREDIT)
                .person(person)
                .wallets(
                        Stream.of(WalletEntity.builder()
                                        .accountNumber(registerCreditCardResult.getCreditAccountNumber())
                                        .mainAccount(registerCreditCardResult.getMainAccount())
                                        .segmentCode(registerCreditCardResult.getSegmentCode())//creditCardSegmentCode
                                        .status(registerCreditCardResult.getStatus())
                                        .loanFileNumber(registerCreditCardResult.getLoanFileNumber())
                                        .build())
                                .collect(Collectors.toSet())
                )
                .build();
    }

    private IssueCardHampaResponse issueCard(CaspianCardWalletEntranceVO caspianCardWalletEntrance, CompleteCustomerInfoVO completeCustomerInfo) {

        IssueCardHampaRequest issueCardHampaRequest = buildIssueCardHampaRequest(caspianCardWalletEntrance, completeCustomerInfo);

        return issueCard(issueCardHampaRequest);
    }

    private IssueCardHampaResponse issueCard(CardWalletEntranceVO cardWalletEntrance) {

        IssueCardHampaRequest issueCardHampaRequest = buildIssueCardHampaRequest(cardWalletEntrance);

        return issueCard(issueCardHampaRequest);
    }

    private IssueCardHampaResponse issueCard(IssueCardHampaRequest issueCardHampaRequest) {

        Optional<PersonEntity> person = personRepository.findByMobileNumber(
                issueCardHampaRequest.getCardIssueHampaRequest().getNoNameCardCustomerHampaModel().getMobileTel()
        );

        if (person.isPresent()) {
            throw new DuplicateRecordException();
        }

        return issueCardHampaClient.send(issueCardHampaRequest)
                .retrieve()
                .result()
                .orElseThrow(IssueCardResultException::new);

    }

    private IssueCardHampaRequest buildIssueCardHampaRequest(CaspianCardWalletEntranceVO caspianCardWalletEntrance, CompleteCustomerInfoVO completeCustomerInfo) {

        return IssueCardHampaRequest.builder()
                .cardIssueHampaRequest(
                        CardIssueHampaRequestVO.builder()
                                .cardProductId(cardProductId)
                                .noNameCardCustomerHampaModel(
                                        NoNameCardCustomerHampaVO.builder()
                                                .firstName(completeCustomerInfo.getName())
                                                .lastName(completeCustomerInfo.getFamily())
                                                .fullAddress(completeCustomerInfo.getAddress())
                                                .fullName(completeCustomerInfo.getName())//
                                                .gender(completeCustomerInfo.getGender())
                                                .nationalId(completeCustomerInfo.getNationalNo())
                                                .addressType("d")//
                                                .mobileTel(completeCustomerInfo.getMobileNo())
                                                .build()
                                )
                                .segmentCodes(caspianCardWalletEntrance.getSegmentCodes())
                                .build()
                )
                .build();

    }

    private IssueCardHampaRequest buildIssueCardHampaRequest(CardWalletEntranceVO cardWalletEntrance) {

        LocalDate localBirthDate = new Date(cardWalletEntrance.getBirthDate().getTime()).toLocalDate();
        LocalDate localNoIdentityNoBirthDate = noIdentityNoBirthDate.toLocalDate();

        boolean isNoIdentity = localNoIdentityNoBirthDate.isEqual(localBirthDate) || localNoIdentityNoBirthDate.isBefore(localBirthDate);

        return IssueCardHampaRequest.builder()
                .cardIssueHampaRequest(
                        CardIssueHampaRequestVO.builder()
                                .cardProductId(cardProductId)
                                .noNameCardCustomerHampaModel(
                                        NoNameCardCustomerHampaVO.builder()
                                                .firstName(cardWalletEntrance.getFirstName())
                                                .lastName(cardWalletEntrance.getLastName())
                                                .fullAddress(cardWalletEntrance.getFullAddress())
                                                .fullName(cardWalletEntrance.getFullName())
                                                .gender(cardWalletEntrance.getGender().value())
                                                .nationalId(cardWalletEntrance.getNationalCode())
                                                .addressType(cardWalletEntrance.getAddressType())
                                                .mobileTel(cardWalletEntrance.getMobileNo())
                                                .birthDate(cardWalletEntrance.getBirthDate())
                                                .birthPlace(cardWalletEntrance.getBirthPlace())
                                                .cityName(cardWalletEntrance.getCityName())
                                                .fatherName(cardWalletEntrance.getFatherName())
                                                .identityNo(isNoIdentity ? noIdentityNoIdentityNo : cardWalletEntrance.getIdentityNo())
                                                .identitySerialPart1(cardWalletEntrance.getIdentitySerialPart1())
                                                .identitySerialPart2(cardWalletEntrance.getIdentitySerialPart2())
                                                .identitySerialPart3(cardWalletEntrance.getIdentitySerialPart3())

                                                .identitySerial(cardWalletEntrance.getIdentitySerialPart1() +
                                                        cardWalletEntrance.getIdentitySerialPart2() +
                                                        cardWalletEntrance.getIdentitySerialPart3())

                                                .issuePlace(cardWalletEntrance.getIssuePlace())
                                                .postalCode(cardWalletEntrance.getPostalCode())
                                                .province(cardWalletEntrance.getProvince())
                                                .build()
                                )
                                .segmentCodes(cardWalletEntrance.getSegmentCodes())
                                .build()
                )
                .build();

    }

    private CompleteCustomerInfoVO customerInfo(String customerNumber) {

        UserRequestVO userRequest = UserRequestVO.builder()
                .customerNo(customerNumber)
                .build();

        return customerClient.getCustomerInfo(userRequest);

    }
}
