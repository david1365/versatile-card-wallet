package ir.caspco.versatile.application.card.wallet.hampa.context.services.impl;

import ir.caspco.versatile.application.card.wallet.hampa.context.services.PasswordService;
import ir.caspco.versatile.jms.client.common.client.SendSmsFirstPasswordCardClient;
import ir.caspco.versatile.jms.client.common.client.hampa.CardOtpDeactivateClient;
import ir.caspco.versatile.jms.client.common.client.hampa.CardOtpRegisterClient;
import ir.caspco.versatile.jms.client.common.client.hampa.ChangeFirstHampaPasswordClient;
import ir.caspco.versatile.jms.client.common.client.hampa.SendSmsHampaCardPasswordClient;
import ir.caspco.versatile.jms.client.common.msg.SendSmsFirstPasswordCardRequest;
import ir.caspco.versatile.jms.client.common.msg.SendSmsFirstPasswordCardResponse;
import ir.caspco.versatile.jms.client.common.msg.hampa.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Service
@Transactional
public class PasswordServiceImpl implements PasswordService {

    private final SendSmsHampaCardPasswordClient sendSmsHampaCardPasswordClient;
    private final SendSmsFirstPasswordCardClient sendSmsFirstPasswordCardClient;
    private final CardOtpRegisterClient cardOtpRegisterClient;
    private final CardOtpDeactivateClient cardOtpDeactivateClient;
    private final ChangeFirstHampaPasswordClient changeFirstHampaPasswordClient;


    public PasswordServiceImpl(SendSmsHampaCardPasswordClient sendSmsHampaCardPasswordClient,
                               SendSmsFirstPasswordCardClient sendSmsFirstPasswordCardClient,
                               CardOtpRegisterClient cardOtpRegisterClient,
                               CardOtpDeactivateClient cardOtpDeactivateClient,
                               ChangeFirstHampaPasswordClient changeFirstHampaPasswordClient) {

        this.sendSmsHampaCardPasswordClient = sendSmsHampaCardPasswordClient;
        this.sendSmsFirstPasswordCardClient = sendSmsFirstPasswordCardClient;
        this.cardOtpRegisterClient = cardOtpRegisterClient;
        this.cardOtpDeactivateClient = cardOtpDeactivateClient;
        this.changeFirstHampaPasswordClient = changeFirstHampaPasswordClient;
    }


    @Override
    public Boolean sendFirstPasswordCardSms(SendSmsHampaCardPasswordRequest sendSmsHampaCardPasswordRequest) {

        SendSmsHampaCardPasswordResponse sendSmsHampaCardPasswordResponse = sendSmsHampaCardPasswordClient.send(sendSmsHampaCardPasswordRequest)
                .retrieve()
                .result()
                .orElse(
                        SendSmsHampaCardPasswordResponse.builder()
                                .result(false)
                                .build()
                );

        return sendSmsHampaCardPasswordResponse.getResult();

    }

    @Override
    public Boolean sendChangePasswordCardSms(SendSmsFirstPasswordCardRequest sendSmsFirstPasswordCardRequest) {

        SendSmsFirstPasswordCardResponse sendSmsFirstPasswordCardResponse = sendSmsFirstPasswordCardClient.send(sendSmsFirstPasswordCardRequest)
                .retrieve()
                .result()
                .orElse(
                        SendSmsFirstPasswordCardResponse.builder()
                                .result(false)
                                .build()
                );

        return sendSmsFirstPasswordCardResponse.getResult();

    }

    @Override
    public Boolean registerCardOtp(CardOtpRegisterRequest cardOtpRegisterRequest) {

        CardOtpRegisterResponse cardOtpRegisterResponse = cardOtpRegisterClient.send(cardOtpRegisterRequest)
                .retrieve()
                .result()
                .orElse(CardOtpRegisterResponse.builder().result(false).build());

        return cardOtpRegisterResponse.getResult();

    }

    @Override
    public Boolean deactivateCardOtp(CardOtpDeactivateRequest cardOtpDeactivateRequest) {

        CardOtpDeactivateResponse cardOtpDeactivateResponse = cardOtpDeactivateClient.send(cardOtpDeactivateRequest)
                .retrieve()
                .result()
                .orElse(CardOtpDeactivateResponse.builder().deactivateResult(false).build());

        return cardOtpDeactivateResponse.getDeactivateResult();
    }

    @Override
    public Boolean changeFirstPassword(ChangeFirstHampaPasswordRequest changeFirstPasswordRequest) {

        ChangeFirstHampaPasswordResponse changeFirstPasswordResponse = changeFirstHampaPasswordClient.send(changeFirstPasswordRequest)
                .retrieve()
                .result()
                .orElse(ChangeFirstHampaPasswordResponse.builder().result(false).build());

        return changeFirstPasswordResponse.getResult();
    }
}
