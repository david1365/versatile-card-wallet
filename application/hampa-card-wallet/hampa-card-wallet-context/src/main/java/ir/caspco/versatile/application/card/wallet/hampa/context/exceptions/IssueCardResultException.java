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
public class IssueCardResultException extends VersatileException {

    public IssueCardResultException() {
    }

    public IssueCardResultException(int resultNumber) {
        super(resultNumber);
    }

    public IssueCardResultException(int resultNumber, Object... args) {
        super(resultNumber, args);
    }

    public IssueCardResultException(Object... args) {
        super(args);
    }

    public IssueCardResultException(String message) {
        super(message);
    }

    public IssueCardResultException(String message, Object... args) {
        super(message, args);
    }

    public IssueCardResultException(String message, Throwable cause, Object[] args) {
        super(message, cause, args);
    }

    public IssueCardResultException(Throwable cause, Object... args) {
        super(cause, args);
    }

    public IssueCardResultException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Object[] args) {
        super(message, cause, enableSuppression, writableStackTrace, args);
    }
}
