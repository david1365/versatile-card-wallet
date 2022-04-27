package ir.caspco.versatile.application.main.configuration.handler;

import ir.caspco.versatile.application.main.configuration.GlobalMessage;
import ir.caspco.versatile.context.handler.exceptions.annotations.MessageHandler;
import ir.caspco.versatile.context.handler.exceptions.message.ErrorDescription;
import ir.caspco.versatile.context.handler.exceptions.message.ErrorMessageHandler;
import ir.caspco.versatile.context.jms.client.exceptions.CoreException;
import ir.caspco.versatile.context.jms.client.model.FaultDetails;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.util.List;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Qualifier("applicationErrorMessageHandler")
@MessageHandler("ir.caspco.versatile.application")
public class ApplicationErrorMessageHandler implements ErrorMessageHandler<GlobalMessage> {

    @SneakyThrows
    @Override
    public GlobalMessage message(Throwable error, List<ErrorDescription> descriptions) {

        String errorKey = error.getClass().getName();

        if (error instanceof CoreException) {

            CoreException coreException = (CoreException) error;

            FaultDetails faultDetails = coreException.getFaultDetails();
            if (faultDetails != null) {

                errorKey = faultDetails.getCode() == null ? faultDetails.getFaultCode() : faultDetails.getCode();
            }
        }

        String hash = DatatypeConverter.printHexBinary(
                MessageDigest.getInstance("MD5").digest(errorKey.getBytes()));

        return GlobalMessage.builder()
                .error(true)
                .statusKey(hash)
                .data(descriptions)
                .build();
    }
}
