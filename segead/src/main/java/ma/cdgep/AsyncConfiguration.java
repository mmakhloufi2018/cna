package ma.cdgep;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfiguration {

	@Bean(name = "taskExecutor")
	public Executor taskExecutor() {
		final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(500);
		executor.setMaxPoolSize(500);
		executor.setQueueCapacity(1500);
		executor.setThreadNamePrefix("ExecPaymentThread-");
		executor.initialize();
		return executor;
	}
	
	@Bean(name = "taskExecutorPensionne")
	public Executor taskExecutorPensionne() {
		final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(300);
		executor.setMaxPoolSize(300);
		executor.setQueueCapacity(1500);
		executor.setThreadNamePrefix("ExecPaymentThread-");
		executor.initialize();
		return executor;
	}
	
}