package ir.caspco.versatile.application.card.wallet.hampa.context.services.impl;

import ir.caspco.versatile.application.card.wallet.hampa.context.domains.CardEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.domains.InternalAccountTransferLogEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.domains.WalletEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.InternalAccountTransferService;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.WalletService;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.log.InternalAccountTransferLogService;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.*;
import ir.caspco.versatile.common.util.Shift;
import ir.caspco.versatile.context.jms.client.exceptions.CoreException;
import ir.caspco.versatile.jms.client.common.client.hampa.InternalAccountTransferClient;
import ir.caspco.versatile.jms.client.common.exceptions.CoreContentResultException;
import ir.caspco.versatile.jms.client.common.msg.hampa.InternalAccountTransferRequest;
import ir.caspco.versatile.jms.client.common.msg.hampa.InternalAccountTransferResponse;
import ir.caspco.versatile.jms.client.common.vo.hampa.InternalAccountTransferEntranceVO;
import ir.caspco.versatile.jms.client.common.vo.hampa.InternalAccountTransferResultVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

//TODO from davood akbari: Do not forget to test.

@Service
@Transactional(noRollbackFor = CoreException.class)
public class InternalAccountTransferServiceImpl implements InternalAccountTransferService {

    private final InternalAccountTransferClient internalAccountTransferClient;

    private final WalletService walletService;
    private final InternalAccountTransferLogService internalAccountTransferLogService;


    public InternalAccountTransferServiceImpl(InternalAccountTransferClient internalAccountTransferClient,
                                              WalletService walletService,
                                              InternalAccountTransferLogService internalAccountTransferLogService) {

        this.internalAccountTransferClient = internalAccountTransferClient;

        this.walletService = walletService;
        this.internalAccountTransferLogService = internalAccountTransferLogService;
    }

    @Override
    public TransferResultVO transferByCard(CardInternalAccountTransferEntranceVO cardInternalAccountTransferEntrance) {

        final WalletEntity sourceWallet =
                walletService.findByCardNumberAndSegmentCodeAndStatusTrue(
                        cardInternalAccountTransferEntrance.getCardNumber(),
                        cardInternalAccountTransferEntrance.getSegmentCode()

                );

        final WalletEntity destinationWallet =
                walletService.findByCardNumberAndSegmentCodeAndStatusTrue(
                        cardInternalAccountTransferEntrance.getDestinationCardNumber(),
                        cardInternalAccountTransferEntrance.getDestinationSegmentCode()
                );

        return transfer(

                TransferEntranceVO.builder()
                        .walletId(sourceWallet.getWalletId())
                        .cardNumber(cardInternalAccountTransferEntrance.getCardNumber())
                        .mobileNo(sourceWallet.getCard().getPerson().getMobileNumber())
                        .segmentCode(sourceWallet.getSegmentCode())

                        .destinationWalletId(destinationWallet.getWalletId())
                        .destinationCardNumber(cardInternalAccountTransferEntrance.getDestinationCardNumber())
                        .destinationMobileNo(destinationWallet.getCard().getPerson().getMobileNumber())
                        .destinationSegmentCode(destinationWallet.getSegmentCode())

                        .amount(cardInternalAccountTransferEntrance.getAmount())
                        .clientTrackingCode(cardInternalAccountTransferEntrance.getClientTrackingCode())

                        .build()
        );
    }

    @Override
    public TransferResultVO transferByWallet(WalletInternalAccountTransferEntranceVO walletInternalAccountTransferEntrance) {

        final WalletEntity sourceWallet =
                walletService.findByWalletIdAndStatusTrue(walletInternalAccountTransferEntrance.getWalletId());

        final WalletEntity destinationWallet =
                walletService.findByWalletIdAndStatusTrue(walletInternalAccountTransferEntrance.getDestinationWalletId());


        final CardEntity sourceCard = sourceWallet.getCard();
        final CardEntity destinationCard = destinationWallet.getCard();

        return transfer(

                TransferEntranceVO.builder()
                        .walletId(sourceWallet.getWalletId())
                        .cardNumber(sourceCard.getCardNumber())
                        .mobileNo(sourceCard.getPerson().getMobileNumber())
                        .segmentCode(sourceWallet.getSegmentCode())

                        .destinationWalletId(destinationWallet.getWalletId())
                        .destinationCardNumber(destinationCard.getCardNumber())
                        .destinationMobileNo(destinationCard.getPerson().getMobileNumber())
                        .destinationSegmentCode(destinationWallet.getSegmentCode())

                        .amount(walletInternalAccountTransferEntrance.getAmount())
                        .clientTrackingCode(walletInternalAccountTransferEntrance.getClientTrackingCode())

                        .build()
        );
    }

    @Override
    public TransferResultVO transferByMobile(MobileInternalAccountTransferEntranceVO mobileInternalAccountTransferEntrance) {

        final WalletEntity sourceWallet =
                walletService.findByMobileNumberAndSegmentCodeAndStatusTrue(
                        mobileInternalAccountTransferEntrance.getMobileNo(),
                        mobileInternalAccountTransferEntrance.getSegmentCode()

                );

        final WalletEntity destinationWallet =
                walletService.findByMobileNumberAndSegmentCodeAndStatusTrue(
                        mobileInternalAccountTransferEntrance.getDestinationMobileNo(),
                        mobileInternalAccountTransferEntrance.getDestinationSegmentCode()
                );

        return transfer(

                TransferEntranceVO.builder()
                        .walletId(sourceWallet.getWalletId())
                        .cardNumber(sourceWallet.getCard().getCardNumber())
                        .mobileNo(sourceWallet.getCard().getPerson().getMobileNumber())
                        .segmentCode(sourceWallet.getSegmentCode())

                        .destinationWalletId(destinationWallet.getWalletId())
                        .destinationCardNumber(destinationWallet.getCard().getCardNumber())
                        .destinationMobileNo(destinationWallet.getCard().getPerson().getMobileNumber())
                        .destinationSegmentCode(destinationWallet.getSegmentCode())

                        .amount(mobileInternalAccountTransferEntrance.getAmount())
                        .clientTrackingCode(mobileInternalAccountTransferEntrance.getClientTrackingCode())

                        .build()
        );
    }

    private TransferResultVO transfer(TransferEntranceVO transferEntrance) {

        InternalAccountTransferEntranceVO internalAccountTransferEntrance =
                Shift.just(transferEntrance).toShift(InternalAccountTransferEntranceVO.class).toObject();

        internalAccountTransferEntrance.setSourceCardNumber(transferEntrance.getCardNumber());
        internalAccountTransferEntrance.setSourceSegmentCode(transferEntrance.getSegmentCode());
        internalAccountTransferEntrance.setCheckUniqueTrackingCode(true);

        InternalAccountTransferRequest internalAccountTransferRequest = InternalAccountTransferRequest.builder()
                .internalAccountTransferEntrance(internalAccountTransferEntrance)
                .build();

        InternalAccountTransferLogEntity internalAccountTransferLogEntity = internalAccountTransferLogService.create(transferEntrance);

        InternalAccountTransferResponse internalAccountTransferResponse = internalAccountTransferClient.send(internalAccountTransferRequest)
                .onError(error -> internalAccountTransferLogService.fail(internalAccountTransferLogEntity))
                .throwException()
                .retrieve()
                .result()
                .orElseThrow(CoreContentResultException::new);


        InternalAccountTransferResultVO internalAccountTransferResult =
                internalAccountTransferResponse.getInternalAccountTransferResult();

        TransferResultVO transferResult =
                Shift.just(transferEntrance).toShift(TransferResultVO.class).toObject();


        transferResult.setCurrentBookBalance(internalAccountTransferResult.getCurrentBookBalance());
        transferResult.setDealReference(internalAccountTransferResult.getDealReference());
        transferResult.setServerTrackingCode(internalAccountTransferResult.getServerTrackingCode());
        transferResult.setTransactionDate(internalAccountTransferResult.getTransactionDate());

        internalAccountTransferLogService.success(transferResult, internalAccountTransferLogEntity);

        return transferResult;
    }
}
