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
public class CnssSchedulerService extends SchedulerData {

    private ScheduledExecutorService situationCheckExec = Executors.newSingleThreadScheduledExecutor();
    private ScheduledExecutorService situationRetryExec = Executors.newSingleThreadScheduledExecutor();

    private boolean isRunningSituationCheck = false;
    private boolean isRunningSituationRetry = false;

    private Date dateLancementCheck;
    private Date dateLancementRetry;
    private Long rateCheck;
    private Long rateRetry;

    @Autowired private ParametrageRepo parametrageRepo;
    @Autowired private RequestBuilderExecutor requestBuilderExecutor;
    @Autowired private CandidatCheckEventRepo candidatCheckEventRepo;
    @Autowired private CandidatCollectedRepo candidatCollectedRepo;

    public void startCnssCheck() {
        stopServiceCheck();
        System.out.println("🟢 startCnssCheck triggered");
        situationCheckExec = Executors.newSingleThreadScheduledExecutor();
        situationCheckExec.execute(requestBuilderExecutor.runCnssCheck());
        isRunningSituationCheck = true;
        dateLancementCheck = new Date();
        rateCheck = 5L;
        setMotif("");
    }

    public void startCnssCheckRetry() {
        stopServiceRetry();
        System.out.println("🟢 startCnssCheckRetry triggered");
        situationRetryExec = Executors.newSingleThreadScheduledExecutor();
        situationRetryExec.execute(requestBuilderExecutor.runCnssRetryCheck());
        isRunningSituationRetry = true;
        dateLancementRetry = new Date();
        rateRetry = 5L;
        setMotif("");
    }

    public void stopServiceCheck() {
        isRunningSituationCheck = false;
        if (situationCheckExec != null && !situationCheckExec.isShutdown()) {
            situationCheckExec.shutdownNow();
            System.err.println("🛑 CNSS Check service stopped.");
        }
    }

    public void stopServiceRetry() {
        if (situationRetryExec != null && !situationRetryExec.isShutdown()) {
            situationRetryExec.shutdownNow();
            isRunningSituationRetry = false;
            System.err.println("🛑 CNSS Retry service stopped.");
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

        if (event.getPartenaire().equalsIgnoreCase(PartenaireEnum.CNSS.name())) {
            switch (event.getEvent()) {
                case "RUN":
                    startCnssCheck();
                    break;
                case "RUNRETRY":
                    startCnssCheckRetry();
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
