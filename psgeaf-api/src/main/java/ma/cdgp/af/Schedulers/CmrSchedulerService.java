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

@Service
public class CmrSchedulerService extends SchedulerData {

    private ScheduledExecutorService situationCheckExec = Executors.newSingleThreadScheduledExecutor();
    private ScheduledExecutorService situationRetryExec = Executors.newSingleThreadScheduledExecutor();

    private boolean isRunningSituationCheck = false;
    private boolean isRunningSituationRetry = false;
    private Date dateLancementRetry;
    private Long rateCheck;
    private Long rateRetry;
    private Date dateLancementCheck;

    @Autowired private ParametrageRepo parametrageRepo;
    @Autowired private RequestBuilderExecutor requestBuilderExecutor;
    @Autowired private CandidatCheckEventRepo candidatCheckEventRepo;
    @Autowired private CandidatCollectedRepo candidatCollectedRepo;

    public void startCmrCheck() {
        stopServiceCheck();
        System.out.println("🟢 startCmrCheck triggered");
        situationCheckExec = Executors.newSingleThreadScheduledExecutor();
        situationCheckExec.execute(requestBuilderExecutor.runCmrCheck());
        isRunningSituationCheck = true;
        dateLancementCheck = new Date();
        rateCheck = 5L;
        setMotif("");
    }

    public void startCmrCheckRetry() {
        stopServiceRetry();
        System.out.println("🟢 startCmrCheckRetry triggered");
        situationRetryExec = Executors.newSingleThreadScheduledExecutor();
        situationRetryExec.execute(requestBuilderExecutor.checkCmrIntegKO());
        isRunningSituationRetry = true;
        dateLancementRetry = new Date();
        rateRetry = 1L;
        setMotif("");
    }

    public void stopServiceCheck() {
        if (situationCheckExec != null && !situationCheckExec.isShutdown()) {
            situationCheckExec.shutdownNow();
            isRunningSituationCheck = false;
            System.err.println("🛑 CMR Check service stopped.");
        }
    }

    public void stopServiceRetry() {
        if (situationRetryExec != null && !situationRetryExec.isShutdown()) {
            situationRetryExec.shutdownNow();
        }
        isRunningSituationRetry = false;
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
        System.out.println("🔔 [EventListener] Received event: " + event);

        if (event == null || event.getPartenaire() == null || event.getEvent() == null) return;

        if (event.getPartenaire().equalsIgnoreCase(PartenaireEnum.CMR.name())) {
            switch (event.getEvent()) {
                case "RUN":
                    System.out.println("▶ Starting CMR Check...");
                    startCmrCheck();
                    break;
                case "RUNRETRY":
                    System.out.println("▶ Starting CMR Retry...");
                    startCmrCheckRetry();
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
        }
    }

}
