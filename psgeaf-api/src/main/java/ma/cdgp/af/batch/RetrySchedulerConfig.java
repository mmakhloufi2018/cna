//package ma.cdgp.af.batch;
//
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.JobParameters;
//import org.springframework.batch.core.JobParametersBuilder;
//import org.springframework.batch.core.launch.JobLauncher;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//
//@Configuration
//@EnableScheduling
//public class RetrySchedulerConfig {
//	@Autowired
//	JobLauncher jobLauncher;
//
//	@Autowired
//	Job job;
//
//	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
//
//	@Scheduled(cron = "0 0/15 * * * ?")
//	public void scheduleByFixedRate() throws Exception {
//		System.out.println("Batch Rtry job starting");
//		JobParameters jobParameters = new JobParametersBuilder()
//				.addLong("time", System.currentTimeMillis()).toJobParameters();
//		jobLauncher.run(job, jobParameters);
//		System.out.println("Batch Rtry job executed successfully\n");
//	}
//}
