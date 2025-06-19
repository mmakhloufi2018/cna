package ma.cdgp.af.Schedulers;


import ma.cdgp.af.dto.af.PartenaireEnum;
import ma.cdgp.af.entity.ParametrageCollection;
import ma.cdgp.af.repository.CandidatCheckEventRepo;
import ma.cdgp.af.repository.CandidatCollectedRepo;
import ma.cdgp.af.repository.ParametrageRepo;
import ma.cdgp.af.service.RequestBuilderExecutor;
import ma.cdgp.af.utils.AlEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class TgrSchedulerService extends SchedulerData {

    private ScheduledExecutorService situationCheckExec = Executors.newSingleThreadScheduledExecutor();
    private ScheduledExecutorService situationRetryExec = Executors.newSingleThreadScheduledExecutor();

    private volatile boolean isRunningSituationCheck = false;
    private volatile boolean isRunningSituationRetry = false;

    private Date dateLancementRetry;
    private Date dateLancementCheck;
    private Long rateCheck;
    private Long rateRetry;

    @Autowired private ParametrageRepo parametrageRepo;
    @Autowired private RequestBuilderExecutor requestBuilderExecutor;
    @Autowired private CandidatCheckEventRepo candidatCheckEventRepo;
    @Autowired private CandidatCollectedRepo candidatCollectedRepo;

    public void startTgrCheck() {
        stopServiceCheck();
        System.out.println("🟢 startTgrCheck triggered");
        situationCheckExec = Executors.newSingleThreadScheduledExecutor();
        situationCheckExec.execute(requestBuilderExecutor.runTgrCheck());
        isRunningSituationCheck = true;
        dateLancementCheck = new Date();
        rateCheck = 5L; // Default rate in seconds
        setMotif("");
    }

    public void startTgrCheckRetry() {
        stopServiceRetry();
        System.out.println("🟢 startTgrCheckRetry triggered");
        situationRetryExec = Executors.newSingleThreadScheduledExecutor();
        situationRetryExec.execute(requestBuilderExecutor.checkTgrIntegKO());
        isRunningSituationRetry = true;
        dateLancementRetry = new Date();
        rateRetry = 5L; // Default retry rate
        setMotif("");
    }

    public void stopServiceCheck() {
        if (situationCheckExec != null && !situationCheckExec.isShutdown()) {
            situationCheckExec.shutdownNow();
            isRunningSituationCheck = false;
            System.err.println("🛑 TGR Check service stopped.");
        }
    }

    public void stopServiceRetry() {
        if (situationRetryExec != null && !situationRetryExec.isShutdown()) {
            situationRetryExec.shutdownNow();
            isRunningSituationRetry = false;
            System.err.println("🛑 TGR Retry service stopped.");
        }
    }

    public boolean isServiceRunningCheck() {
        return isRunningSituationCheck;
    }

    public boolean isServiceRunningRetry() {
        return isRunningSituationRetry;
    }

    public Date getDateLancementCheck() {
        return dateLancementCheck;
    }

    public Date getDateLancementRetry() {
        return dateLancementRetry;
    }

    public Long getRateCheck() {
        return rateCheck;
    }

    public Long getRateRetry() {
        return rateRetry;
    }

    @EventListener
    public void handleEvent(AlEvent event) {
        if (event == null || event.getPartenaire() == null || event.getEvent() == null) return;

        if (event.getPartenaire().equalsIgnoreCase(PartenaireEnum.TGR.name())) {
            switch (event.getEvent()) {
                case "RUN":
                    System.out.println("▶ Starting TGR Check...");
                    startTgrCheck();
                    break;
                case "RUNRETRY":
                    System.out.println("▶ Starting TGR Retry...");
                    startTgrCheckRetry();
                    break;
                case "STOP":
                    stopServiceCheck();
                    break;
                case "STOPRETRY":
                    stopServiceRetry();
                    break;
                default:
                    break;
            }
            System.out.println("🔔 Event received: " + event.getPartenaire());
        }
    }
}