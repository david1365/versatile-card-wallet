package ir.caspco.versatile.application.card.wallet.hampa.context.services;

import ir.caspco.versatile.jms.client.common.msg.SendSmsFirstPasswordCardRequest;
import ir.caspco.versatile.jms.client.common.msg.hampa.CardOtpDeactivateRequest;
import ir.caspco.versatile.jms.client.common.msg.hampa.CardOtpRegisterRequest;
import ir.caspco.versatile.jms.client.common.msg.hampa.ChangeFirstHampaPasswordRequest;
import ir.caspco.versatile.jms.client.common.msg.hampa.SendSmsHampaCardPasswordRequest;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

public interface PasswordService {

    Boolean sendFirstPasswordCardSms(SendSmsHampaCardPasswordRequest sendSmsHampaCardPasswordRequest);

    Boolean sendChangePasswordCardSms(SendSmsFirstPasswordCardRequest sendSmsFirstPasswordCardRequest);

    Boolean registerCardOtp(CardOtpRegisterRequest cardOtpRegisterRequest);

    Boolean deactivateCardOtp(CardOtpDeactivateRequest cardOtpDeactivateRequest);

    Boolean changeFirstPassword(ChangeFirstHampaPasswordRequest changeFirstPasswordRequest);

}
