package ma.rcar.rsu.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.batch.core.repository.JobRepository;





/**
 * @author BAKHALED Ibrahim.
 *
 */



@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Bean
    public Job partnerBatchJob(JobRepository jobRepository, Step dynamicStep) {
        return new JobBuilder("partnerBatchJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(dynamicStep)
                .build();
    }

    @Bean
    public Step dynamicStep(JobRepository jobRepository,
                            PlatformTransactionManager transactionManager,
                            ItemReader<Object> reader,
                            ItemProcessor<Object, Object> processor,
                            ItemWriter<Object> writer) {

        return new StepBuilder("partnerStep", jobRepository)
                .<Object, Object>chunk(1, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}