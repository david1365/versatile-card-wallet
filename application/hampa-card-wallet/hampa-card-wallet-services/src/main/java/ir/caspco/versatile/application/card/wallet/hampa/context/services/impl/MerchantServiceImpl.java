package ir.caspco.versatile.application.card.wallet.hampa.context.services.impl;

import ir.caspco.versatile.application.card.wallet.hampa.context.domains.MerchantEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.domains.SettlementNextValidTimeEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.enums.SettlementType;
import ir.caspco.versatile.application.card.wallet.hampa.context.exceptions.MerchantNotFoundException;
import ir.caspco.versatile.application.card.wallet.hampa.context.repositories.MerchantRepository;
import ir.caspco.versatile.application.card.wallet.hampa.context.repositories.SettlementNextValidTimeRepository;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.MerchantService;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.MerchantVO;
import ir.caspco.versatile.common.util.Cron;
import ir.caspco.versatile.common.util.Shift;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
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
@Transactional
public class MerchantServiceImpl implements MerchantService {

    private final MerchantRepository merchantRepository;
    private final SettlementNextValidTimeRepository settlementNextValidTimeRepository;


    @Value("${cardWallet.merchant.charge.cron:* 5 24 * * *}")
    private String chargeCron;

    @Value("${cardWallet.merchant.purchase.cron:* 5 24 * * *}")
    private String purchaseCron;

    public MerchantServiceImpl(MerchantRepository merchantRepository,
                               SettlementNextValidTimeRepository settlementNextValidTimeRepository) {

        this.merchantRepository = merchantRepository;
        this.settlementNextValidTimeRepository = settlementNextValidTimeRepository;
    }


    @Override
    public MerchantEntity findByMerchantIdAndActiveTrue(UUID merchantId) {

        return merchantRepository
                .findByMerchantIdAndActiveTrue(merchantId)
                .orElseThrow(MerchantNotFoundException::new);
    }

    @Override
    public MerchantVO save(MerchantVO merchantIn) {

        MerchantEntity merchant = convertToMerchantEntity(merchantIn);
        merchant.setActive(true);

        saveNextValidTimes(merchant);

        merchant = merchantRepository.save(merchant);

        return convertToMerchantVO(merchant);
    }


    @Override
    public MerchantVO update(MerchantVO merchantIn) {

        MerchantEntity merchant = merchantRepository.findById(merchantIn.getMerchantId())
                .orElseThrow(MerchantNotFoundException::new);

        convertToMerchantEntity(merchantIn, merchant);

        merchant = merchantRepository.save(merchant);

        return convertToMerchantVO(merchant);
    }

    @Override
    public Boolean deleteById(UUID merchantID) {

        Set<SettlementNextValidTimeEntity> settlementNextValidTimes =
                settlementNextValidTimeRepository.findAllByMerchant_MerchantId(merchantID);

        settlementNextValidTimeRepository.deleteAllById(
                settlementNextValidTimes.stream()
                        .map(SettlementNextValidTimeEntity::getId)
                        .collect(Collectors.toSet())
        );

        merchantRepository.deleteById(merchantID);

        return true;
    }

    @Override
    public MerchantVO findById(UUID merchantId) {

        MerchantEntity merchant = merchantRepository.findById(merchantId)
                .orElseThrow(MerchantNotFoundException::new);

        return convertToMerchantVO(merchant);
    }

    private MerchantEntity convertToMerchantEntity(MerchantVO merchantIn) {
        MerchantEntity merchant = Shift.just(merchantIn).toShift(MerchantEntity.class).toObject();

        merchant.setChargeCron(chargeCron);
        merchant.setPurchaseCron(purchaseCron);

        return merchant;
    }

    private MerchantVO convertToMerchantVO(MerchantEntity merchant) {

        MerchantVO merchantOut = MerchantVO.builder()
                .merchantId(merchant.getMerchantId())
                .active(merchant.getActive())
//                .chargeCron(merchant.getChargeCron())
//                .purchaseCron(merchant.getPurchaseCron())
                .chargeInternalAccount(merchant.getChargeInternalAccount())
                .chargeSettlementAccount(merchant.getChargeSettlementAccount())
                .purchaseInternalAccount(merchant.getPurchaseInternalAccount())
                .purchaseSettlementAccount(merchant.getPurchaseSettlementAccount())
                .title(merchant.getTitle())
                .version(merchant.getVersion())
                .build();

        merchant.getSettlementNextValidTimes().stream()
                .filter(settlementNextValidTime -> SettlementType.Charge.equals(settlementNextValidTime.getSettlementType()))
                .findFirst()
                .ifPresent(settlementNextValidTime -> merchantOut.setChargeNextValidTime(settlementNextValidTime.getNextValidTime()));

        merchant.getSettlementNextValidTimes().stream()
                .filter(settlementNextValidTime -> SettlementType.Purchase.equals(settlementNextValidTime.getSettlementType()))
                .findFirst()
                .ifPresent(settlementNextValidTime -> merchantOut.setPurchaseNextValidTime(settlementNextValidTime.getNextValidTime()));

        return merchantOut;
    }

    private void saveNextValidTimes(MerchantEntity merchant) {

        final Date chargeNextValidTime = Cron.expression(merchant.getChargeCron()).nextValidTimeAfterNow();
        final Date purchaseNextValidTime = Cron.expression(merchant.getPurchaseCron()).nextValidTimeAfterNow();


        Set<SettlementNextValidTimeEntity> settlementNextValidTimes = new HashSet<>();

        settlementNextValidTimes.add(
                SettlementNextValidTimeEntity.builder()
                        .merchant(merchant)
                        .nextValidTime(chargeNextValidTime)
                        .settlementType(SettlementType.Charge)
                        .build()
        );

        settlementNextValidTimes.add(
                SettlementNextValidTimeEntity.builder()
                        .merchant(merchant)
                        .nextValidTime(purchaseNextValidTime)
                        .settlementType(SettlementType.Purchase)
                        .build()
        );

        merchant.setSettlementNextValidTimes(settlementNextValidTimes);
    }

    private void updateNextValidTimes(MerchantEntity merchant) {

        final Date chargeNextValidTime = Cron.expression(merchant.getChargeCron()).nextValidTimeAfterNow();
        final Date purchaseNextValidTime = Cron.expression(merchant.getPurchaseCron()).nextValidTimeAfterNow();


        Set<SettlementNextValidTimeEntity> settlementNextValidTimes = merchant.getSettlementNextValidTimes();

        settlementNextValidTimes.stream()
                .filter(settlementNextValidTime -> SettlementType.Charge.equals(settlementNextValidTime.getSettlementType()))
                .findFirst()
                .ifPresent(settlementNextValidTime -> settlementNextValidTime.setNextValidTime(chargeNextValidTime));

        settlementNextValidTimes.stream()
                .filter(settlementNextValidTime -> SettlementType.Purchase.equals(settlementNextValidTime.getSettlementType()))
                .findFirst()
                .ifPresent(settlementNextValidTime -> settlementNextValidTime.setNextValidTime(purchaseNextValidTime));


        merchant.setSettlementNextValidTimes(settlementNextValidTimes);
    }

    private void convertToMerchantEntity(MerchantVO merchantIn, MerchantEntity merchant) {

        merchant.setTitle(merchantIn.getTitle());
        merchant.setActive(merchantIn.getActive());
        merchant.setChargeCron(/*merchantIn.getChargeCron()*/chargeCron);
        merchant.setPurchaseCron(/*merchantIn.getPurchaseCron()*/purchaseCron);
        merchant.setChargeInternalAccount(merchantIn.getChargeInternalAccount());
        merchant.setPurchaseInternalAccount(merchantIn.getPurchaseInternalAccount());
        merchant.setChargeSettlementAccount(merchantIn.getChargeSettlementAccount());
        merchant.setPurchaseSettlementAccount(merchantIn.getPurchaseSettlementAccount());

        updateNextValidTimes(merchant);
    }
}
