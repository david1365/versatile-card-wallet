package ir.caspco.versatile.application.card.wallet.hampa.context.configuration;

import ir.caspco.versatile.common.util.encryption.Cryptography;
import ir.caspco.versatile.common.util.encryption.Rsa;
import ir.caspco.versatile.context.handler.exceptions.annotations.MessagesPath;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Configuration
@MessagesPath({
        "classpath:/i18n/hampa-card-wallet-context-messages",
        "classpath:/i18n/hampa-card-wallet-context-field-validations-messages"
})
public class CardWalletConfiguration {

    @Value("${cryptography.caspian.key-store.path:caspian-keystore.jks}")
    private String caspianPath;

    @Value("${cryptography.caspian.key-store.password:ir.caspco}")
    private char[] caspianPassword;

    @Value("${cryptography.caspian.key-store.alias:caspian}")
    private String caspianPasswordAlias;

    @Value("${cryptography.hampa.key-store.path:hampa-keystore.jks}")
    private String hampaPath;

    @Value("${cryptography.hampa.key-store.password:hampa$1401}")
    private char[] hampaPassword;

    @Value("${cryptography.hampa.key-store.alias:hampa}")
    private String hampaAlias;

    @Bean("caspian-rsa")
    public Cryptography caspianRsa() {

        return new Rsa(caspianPath, caspianPassword, caspianPasswordAlias);
    }

    @Bean("hampa-rsa")
    public Cryptography hampaRsa() {

        return new Rsa(hampaPath, hampaPassword, hampaAlias);
    }
}
