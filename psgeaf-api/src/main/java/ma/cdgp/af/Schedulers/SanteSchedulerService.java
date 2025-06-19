package ma.cdgp.af.Schedulers;

import ma.cdgp.af.dto.af.PartenaireEnum;
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
public class SanteSchedulerService extends SchedulerData {

    private ScheduledExecutorService situationCheckExec = Executors.newScheduledThreadPool(1);
    private ScheduledExecutorService situationRetryExec = Executors.newScheduledThreadPool(1);

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

    public void startSanteCheck() {
        stopServiceCheck();
        System.out.println("🟢 startSanteCheck triggered");
        Integer rate = 5;
        situationCheckExec = Executors.newSingleThreadScheduledExecutor();
        situationCheckExec.execute(requestBuilderExecutor.runSanteCheck());
        isRunningSituationCheck = true;
        dateLancementCheck = new Date();
        rateCheck = rate.longValue();
        setMotif("");
    }

    public void startSanteCheckRetry() {
        stopServiceRetry();
        System.out.println("🟢 startSanteCheckRetry triggered");
        Integer rate = 5;
        situationRetryExec = Executors.newSingleThreadScheduledExecutor();
//        situationRetryExec.execute(requestBuilderExecutor.checkSanteIntegKo());
        isRunningSituationRetry = true;
        dateLancementRetry = new Date();
        rateRetry = rate.longValue();
        setMotif("");
    }

    public void stopServiceCheck() {
        if (situationCheckExec != null && !situationCheckExec.isShutdown()) {
            situationCheckExec.shutdown();
        }
        isRunningSituationCheck = false;
        System.out.println("🔴 Sante Check service stopped.");
    }

    public void stopServiceRetry() {
        if (situationRetryExec != null && !situationRetryExec.isShutdown()) {
            situationRetryExec.shutdown();
        }
        isRunningSituationRetry = false;
        System.out.println("🔴 Sante Retry service stopped.");
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

        if (event.getPartenaire().equalsIgnoreCase(PartenaireEnum.SANTE.name())) {
            switch (event.getEvent()) {
                case "RUN":
                    startSanteCheck();
                    break;
                case "RUNRETRY":
                    startSanteCheckRetry();
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
        System.out.println("🔔 Event received: " + event.getPartenaire());
    }
}
