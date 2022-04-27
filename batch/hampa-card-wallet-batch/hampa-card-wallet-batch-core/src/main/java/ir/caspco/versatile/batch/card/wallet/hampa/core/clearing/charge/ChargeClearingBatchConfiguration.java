package ir.caspco.versatile.batch.card.wallet.hampa.core.clearing.charge;

import ir.caspco.versatile.batch.card.wallet.hampa.core.clearing.ClearingItemProcessor;
import ir.caspco.versatile.batch.card.wallet.hampa.core.clearing.model.ClearingModel;
import ir.caspco.versatile.batch.card.wallet.hampa.core.clearing.model.ClearingWithMerchantModel;
import ir.caspco.versatile.batch.listener.ExceptionListener;
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

//TODO from davood akbari: Test the charge request when performing the job.
@Configuration
@EntityScan("ir.caspco.versatile.application.card.wallet.hampa.context.domains")
public class ChargeClearingBatchConfiguration {

    public static final String JOB_NAME = "CHARGE_CLEARING_BATCH_JOB";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final JobLauncher jobLauncher;

    private final ChargeClearingItemReader chargeClearingItemReader;
    private final ClearingItemProcessor clearingItemProcessor;
    private final ChargeClearingItemWriter chargeClearingItemWriter;

    private final ExceptionListener exceptionListener;


    public ChargeClearingBatchConfiguration(JobBuilderFactory jobBuilderFactory,
                                            StepBuilderFactory stepBuilderFactory,
                                            JobLauncher jobLauncher,

                                            ChargeClearingItemReader chargeClearingItemReader,
                                            ClearingItemProcessor clearingItemProcessor,
                                            ChargeClearingItemWriter chargeClearingItemWriter,

                                            ExceptionListener exceptionListener) {

        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.jobLauncher = jobLauncher;

        this.chargeClearingItemReader = chargeClearingItemReader;
        this.clearingItemProcessor = clearingItemProcessor;
        this.chargeClearingItemWriter = chargeClearingItemWriter;

        this.exceptionListener = exceptionListener;
    }


    @Bean
    public Step chargeClearingStep1() {

        return stepBuilderFactory.get("chargeClearingStep1")
                .<ClearingModel, ClearingWithMerchantModel>chunk(0)
                .reader(chargeClearingItemReader)
                .processor(clearingItemProcessor)
                .writer(chargeClearingItemWriter)
                .faultTolerant()
                .noRollback(Throwable.class)
                .processorNonTransactional()
                .skip(Exception.class)
                .skipLimit(0)
                .listener(exceptionListener)
                .build();
    }

    @Bean(name = ChargeClearingBatchConfiguration.JOB_NAME)
    public Job clearingBatchJob() {

        return jobBuilderFactory.get(ChargeClearingBatchConfiguration.JOB_NAME).start(chargeClearingStep1()).build();
    }

    @Scheduled(cron = "${batch.scheduled.cron.clearing.charge}")
    public void launchJob() throws Exception {

        jobLauncher.run(
                clearingBatchJob(),
                new JobParametersBuilder().addLong("uniqueness", System.nanoTime()).toJobParameters()
        );
    }
}

