package ir.caspco.versatile.application.card.wallet.hampa.context.services.impl.log;

import ir.caspco.versatile.application.card.wallet.hampa.context.domains.InternalAccountTransferLogEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.repositories.InternalAccountTransferLogRepository;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.log.InternalAccountTransferLogService;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.TransferEntranceVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.TransferResultVO;
import ir.caspco.versatile.context.enums.FlowStatus;
import org.springframework.stereotype.Service;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Service
public class InternalAccountTransferLogServiceImpl implements InternalAccountTransferLogService {

    private final InternalAccountTransferLogRepository internalAccountTransferLogRepository;

    public InternalAccountTransferLogServiceImpl(InternalAccountTransferLogRepository internalAccountTransferLogRepository) {

        this.internalAccountTransferLogRepository = internalAccountTransferLogRepository;
    }


    @Override
    public InternalAccountTransferLogEntity create(TransferEntranceVO transferEntrance) {

        return internalAccountTransferLogRepository.saveAndFlush(
                InternalAccountTransferLogEntity.builder()
                        .walletId(transferEntrance.getWalletId())
                        .cardNumber(transferEntrance.getCardNumber())
                        .segmentCode(transferEntrance.getSegmentCode())
                        .mobileNo(transferEntrance.getMobileNo())

                        .destinationWalletId(transferEntrance.getDestinationWalletId())
                        .destinationCardNumber(transferEntrance.getDestinationCardNumber())
                        .destinationSegmentCode(transferEntrance.getDestinationSegmentCode())
                        .destinationMobileNo(transferEntrance.getDestinationMobileNo())

                        .amount(transferEntrance.getAmount())
                        .clientTrackingCode(transferEntrance.getClientTrackingCode())

                        .build()
        );
    }

    @Override
    public InternalAccountTransferLogEntity fail(InternalAccountTransferLogEntity internalAccountTransferLog) {

        internalAccountTransferLog.setFlowStatus(FlowStatus.FAIL);
        return internalAccountTransferLogRepository.saveAndFlush(internalAccountTransferLog);
    }

    @Override
    public InternalAccountTransferLogEntity success(TransferResultVO transferResult, InternalAccountTransferLogEntity internalAccountTransferLog) {

        internalAccountTransferLog.setDealReference(transferResult.getDealReference());
        internalAccountTransferLog.setServerTrackingCode(transferResult.getServerTrackingCode());
        internalAccountTransferLog.setTransactionDate(transferResult.getTransactionDate());
        internalAccountTransferLog.setCurrentBookBalance(transferResult.getCurrentBookBalance());
        internalAccountTransferLog.setFlowStatus(FlowStatus.DONE);

        return internalAccountTransferLogRepository.saveAndFlush(internalAccountTransferLog);
    }

    public void reverse(String dealReference) {

        internalAccountTransferLogRepository.findByDealReference(dealReference)
                .ifPresent(internalAccountTransferLog -> {

                    internalAccountTransferLog.setFlowStatus(FlowStatus.REVERSE);

                    internalAccountTransferLogRepository.save(internalAccountTransferLog);
                });
    }
}
