package ir.caspco.versatile.batch.card.wallet.hampa.core.clearing.purchase;

import ir.caspco.versatile.batch.card.wallet.hampa.core.clearing.ClearingItemProcessor;
import ir.caspco.versatile.batch.card.wallet.hampa.core.clearing.ClearingUtil;
import ir.caspco.versatile.batch.listener.BatchExceptionConfiguration;
import ir.caspco.versatile.batch.listener.ExceptionListener;
import ir.caspco.versatile.jms.client.JmsClientConfiguration;
import ir.caspco.versatile.jms.client.common.client.hampa.MerchantSettlementHampaCardClient;
import ir.caspco.versatile.jms.client.context.JmsClientProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@ExtendWith(SpringExtension.class)
@SpringBatchTest
@EnableAutoConfiguration
@ContextConfiguration(classes = {
        PurchaseClearingBatchConfiguration.class,
        TestPurchaseJobConfiguration.class,
        PurchaseClearingItemReader.class,
        ClearingItemProcessor.class,
        PurchaseClearingItemWriter.class,
        ClearingUtil.class,
        MerchantSettlementHampaCardClient.class,
        JmsClientProperties.class,
        JmsClientConfiguration.class,
        BatchExceptionConfiguration.class,
        ExceptionListener.class
})
@TestPropertySource("classpath:test-application.properties")
class PurchaseClearingBatchConfigurationTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @BeforeEach
    public void clearJobExecutions() {
        this.jobRepositoryTestUtils.removeJobExecutions();
    }

    @Test
    void launchJob() throws Exception {

        JobParameters jobParameters = this.jobLauncherTestUtils.getUniqueJobParameters();

        JobExecution jobExecution = this.jobLauncherTestUtils.launchJob(jobParameters);

        assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
    }
}