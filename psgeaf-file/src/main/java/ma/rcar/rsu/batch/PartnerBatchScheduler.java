package ma.rcar.rsu.batch;

import ma.rcar.rsu.generic.PartnerBatchService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;




/**
 * @author BAKHALED Ibrahim.
 *
 */



@Component
@EnableScheduling
public class PartnerBatchScheduler {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job partnerBatchJob;

    @Autowired
    private List<PartnerBatchService<?>> partnerServices;

    @Scheduled(fixedDelay = 60000)
    public void runBatches() {
        for (PartnerBatchService<?> service : partnerServices) {
            if (!service.checkInboundFiles().isEmpty()) {
                try {
                    JobParameters params = new JobParametersBuilder()
                            .addString("partner", service.getPartnerKey())
                            .addLong("time", System.currentTimeMillis())
                            .toJobParameters();

                    jobLauncher.run(partnerBatchJob, params);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
