package ir.caspco.versatile.batch.card.wallet.hampa.core.clearing.purchase;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Configuration
@EnableBatchProcessing
@EnableJpaAuditing
@EnableJpaRepositories("ir.caspco.versatile.application.card.wallet.hampa.context.repositories")
public class TestPurchaseJobConfiguration {

    private PurchaseClearingBatchConfiguration purchaseClearingBatchConfiguration;

    public TestPurchaseJobConfiguration(PurchaseClearingBatchConfiguration purchaseClearingBatchConfiguration) {
        this.purchaseClearingBatchConfiguration = purchaseClearingBatchConfiguration;
    }


    @Bean
    public JobRepositoryTestUtils jobRepositoryTestUtils() {
        return new JobRepositoryTestUtils();
    }

    @Bean
    public JobLauncherTestUtils jobLauncherTestUtils() {
        JobLauncherTestUtils jobLauncherTestUtils = new JobLauncherTestUtils();
        jobLauncherTestUtils.setJob(purchaseClearingBatchConfiguration.clearingBatchJob());

        return jobLauncherTestUtils;
    }
}