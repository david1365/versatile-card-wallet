package ir.caspco.versatile.application.card.wallet.hampa.context.exceptions;

import ir.caspco.versatile.context.handler.exceptions.VersatileException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MerchantNotFoundException extends VersatileException {

    public MerchantNotFoundException() {
    }

    public MerchantNotFoundException(int resultNumber) {
        super(resultNumber);
    }

    public MerchantNotFoundException(int resultNumber, Object... args) {
        super(resultNumber, args);
    }

    public MerchantNotFoundException(Object... args) {
        super(args);
    }

    public MerchantNotFoundException(String message) {
        super(message);
    }

    public MerchantNotFoundException(String message, Object... args) {
        super(message, args);
    }

    public MerchantNotFoundException(String message, Throwable cause, Object[] args) {
        super(message, cause, args);
    }

    public MerchantNotFoundException(Throwable cause, Object... args) {
        super(cause, args);
    }

    public MerchantNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Object[] args) {
        super(message, cause, enableSuppression, writableStackTrace, args);
    }
}
