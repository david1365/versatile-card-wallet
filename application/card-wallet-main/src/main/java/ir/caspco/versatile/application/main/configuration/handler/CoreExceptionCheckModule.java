package ir.caspco.versatile.application.main.configuration.handler;

import ir.caspco.versatile.context.handler.exceptions.CheckModule;
import ir.caspco.versatile.context.handler.exceptions.annotations.ForExceptionHandler;
import ir.caspco.versatile.context.jms.client.exceptions.CoreException;

import java.util.Arrays;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@ForExceptionHandler(CoreException.class)
public class CoreExceptionCheckModule implements CheckModule {

    @Override
    public Boolean check(Throwable error, String key) {

        return Arrays.stream((error.getSuppressed()[0]).getStackTrace())
                .anyMatch(stackTraceElement -> stackTraceElement.getClassName().contains(key));

    }

}
