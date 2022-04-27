package ir.caspco.versatile.batch.card.wallet.hampa.core.clearing.purchase;

import ir.caspco.versatile.batch.card.wallet.hampa.core.clearing.ClearingItemProcessor;
import ir.caspco.versatile.batch.card.wallet.hampa.core.clearing.model.ClearingModel;
import ir.caspco.versatile.batch.card.wallet.hampa.core.clearing.model.ClearingWithMerchantModel;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

//TODO from davood akbari: Test the Purchase request when performing the job.
@Configuration
@EntityScan("ir.caspco.versatile.application.card.wallet.hampa.context.domains")
public class PurchaseClearingBatchConfiguration {

    public static final String JOB_NAME = "PURCHASE_CLEARING_BATCH_JOB";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final JobLauncher jobLauncher;

    private final PurchaseClearingItemReader purchaseClearingItemReader;
    private final ClearingItemProcessor clearingItemProcessor;
    private final PurchaseClearingItemWriter purchaseClearingItemWriter;


    public PurchaseClearingBatchConfiguration(JobBuilderFactory jobBuilderFactory,
                                              StepBuilderFactory stepBuilderFactory,
                                              JobLauncher jobLauncher,

                                              PurchaseClearingItemReader purchaseClearingItemReader,
                                              ClearingItemProcessor clearingItemProcessor,
                                              PurchaseClearingItemWriter purchaseClearingItemWriter) {

        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.jobLauncher = jobLauncher;

        this.purchaseClearingItemReader = purchaseClearingItemReader;
        this.clearingItemProcessor = clearingItemProcessor;
        this.purchaseClearingItemWriter = purchaseClearingItemWriter;
    }


    @Bean
    public Step purchaseClearingStep1() {
        return stepBuilderFactory.get("purchaseClearingStep1")
                .<ClearingModel, ClearingWithMerchantModel>chunk(0)
                .reader(purchaseClearingItemReader)
                .processor(clearingItemProcessor)
                .writer(purchaseClearingItemWriter)
                .build();
    }

    @Bean(name = PurchaseClearingBatchConfiguration.JOB_NAME)
    public Job clearingBatchJob() {
        return jobBuilderFactory.get(PurchaseClearingBatchConfiguration.JOB_NAME).start(purchaseClearingStep1()).build();
    }

    @Scheduled(cron = "${batch.scheduled.cron.clearing.purchase}")
    public void launchJob() throws Exception {
        jobLauncher.run(
                clearingBatchJob(),
                new JobParametersBuilder().addLong("uniqueness", System.nanoTime()).toJobParameters()
        );
    }
}

