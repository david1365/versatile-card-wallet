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

@ResponseStatus(HttpStatus.NO_CONTENT)
public class CashOutResultException extends VersatileException {

    public CashOutResultException() {
    }

    public CashOutResultException(int resultNumber) {
        super(resultNumber);
    }

    public CashOutResultException(int resultNumber, Object... args) {
        super(resultNumber, args);
    }

    public CashOutResultException(Object... args) {
        super(args);
    }

    public CashOutResultException(String message) {
        super(message);
    }

    public CashOutResultException(String message, Object... args) {
        super(message, args);
    }

    public CashOutResultException(String message, Throwable cause, Object[] args) {
        super(message, cause, args);
    }

    public CashOutResultException(Throwable cause, Object... args) {
        super(cause, args);
    }

    public CashOutResultException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Object[] args) {
        super(message, cause, enableSuppression, writableStackTrace, args);
    }
}
