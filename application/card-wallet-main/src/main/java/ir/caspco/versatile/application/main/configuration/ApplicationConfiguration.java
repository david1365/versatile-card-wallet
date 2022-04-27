package ir.caspco.versatile.application.main.configuration;

import ir.caspco.versatile.context.handler.exceptions.annotations.MessagesPath;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Configuration
@EnableWebFlux
@ComponentScan("ir.caspco")
@MessagesPath("classpath:/i18n/card-wallet-main-messages")
public class ApplicationConfiguration {
}
