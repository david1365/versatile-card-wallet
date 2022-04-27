package ir.caspco.versatile.application.card.wallet.hampa.controller;

import ir.caspco.versatile.application.card.wallet.hampa.context.services.PasswordService;
import ir.caspco.versatile.jms.client.common.msg.SendSmsFirstPasswordCardRequest;
import ir.caspco.versatile.jms.client.common.msg.hampa.CardOtpDeactivateRequest;
import ir.caspco.versatile.jms.client.common.msg.hampa.CardOtpRegisterRequest;
import ir.caspco.versatile.jms.client.common.msg.hampa.ChangeFirstHampaPasswordRequest;
import ir.caspco.versatile.jms.client.common.msg.hampa.SendSmsHampaCardPasswordRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@RestController
@RequestMapping("${cardWallet.prefix}")
public class PasswordController {

    private final PasswordService passwordService;


    public PasswordController(PasswordService passwordService) {
        this.passwordService = passwordService;
    }


    @PreAuthorize("hasRole('ROLE_CW_SEND_FIRST_PASSWORD_CARD_SMS')")
    @PostMapping("/sendFirstPasswordCardSms")
    public Mono<Boolean> sendFirstPasswordCardSms(@RequestBody @Valid SendSmsHampaCardPasswordRequest sendSmsHampaCardPasswordRequest) {

        return Mono.just(passwordService.sendFirstPasswordCardSms(sendSmsHampaCardPasswordRequest));
    }

    //    @PreAuthorize("hasRole('ROLE_CW_SEND_CHANGE_PASSWORD_CARD_SMS')")
//    @PostMapping("/sendChangePasswordCardSms")
    public Mono<Boolean> sendChangePasswordCardSms(@RequestBody @Valid SendSmsFirstPasswordCardRequest sendSmsFirstPasswordCardRequest) {

        return Mono.just(passwordService.sendChangePasswordCardSms(sendSmsFirstPasswordCardRequest));
    }

    @PreAuthorize("hasRole('ROLE_CW_REGISTER_CARD_OTP')")
    @PostMapping("/registerCardOtp")
    public Mono<Boolean> registerCardOtp(@RequestBody @Valid CardOtpRegisterRequest cardOtpRegisterRequest) {

        return Mono.just(passwordService.registerCardOtp(cardOtpRegisterRequest));
    }

    @PreAuthorize("hasRole('ROLE_CW_DEACTIVATE_CARD_OTP')")
    @PostMapping("/deactivateCardOtp")
    public Mono<Boolean> deactivateCardOtp(@RequestBody @Valid CardOtpDeactivateRequest cardOtpDeactivateRequest) {

        return Mono.just(passwordService.deactivateCardOtp(cardOtpDeactivateRequest));
    }

    @PreAuthorize("hasRole('ROLE_CW_CHANGE_FIRST_PASSWORD')")
    @PostMapping("/changeFirstPassword")
    public Mono<Boolean> changeFirstPassword(@RequestBody @Valid ChangeFirstHampaPasswordRequest changeFirstPasswordRequest) {

        return Mono.just(passwordService.changeFirstPassword(changeFirstPasswordRequest));
    }
}
