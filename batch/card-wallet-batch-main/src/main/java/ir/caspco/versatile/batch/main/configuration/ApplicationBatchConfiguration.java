package ir.caspco.versatile.batch.main.configuration;

import ir.caspco.versatile.context.handler.exceptions.annotations.MessagesPath;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Configuration
@ComponentScan("ir.caspco")
@MessagesPath("classpath:/i18n/card-wallet-main-messages")
@PropertySource(value = "file:${override.home}/override.properties")
@EnableBatchProcessing
@EnableScheduling
public class ApplicationBatchConfiguration {

}

