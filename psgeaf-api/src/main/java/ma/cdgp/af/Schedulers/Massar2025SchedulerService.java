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
public class Massar2025SchedulerService extends SchedulerData{

    private ScheduledExecutorService situationCheckExec;
    private ScheduledExecutorService situationRetryExec;
    private boolean isRunningSituationCheck;
    private boolean isRunningSituationRetry;
    private Date dateLancementRetry;
    private Long rateCheck;
    private Long rateRetry;
    private Date dateLancementCheck;
    @Autowired
    ParametrageRepo parametrageRepo;
    @Autowired
    RequestBuilderExecutor requestBuilderExecutor;
    @Autowired
    CandidatCheckEventRepo candidatCheckEventRepo;
    @Autowired
    CandidatCollectedRepo candidatCollectedRepo;


    Massar2025SchedulerService(){
        situationCheckExec = Executors.newScheduledThreadPool(1);
        situationRetryExec = Executors.newScheduledThreadPool(1);
    }

    public void startMassar2025Check() {
        situationCheckExec = Executors.newScheduledThreadPool(1);
        Integer rateCollectTgr = 5;
        situationCheckExec.execute(requestBuilderExecutor::runMassar2025Check);
        isRunningSituationCheck = true;
        dateLancementCheck = new Date();
        rateCheck = rateCollectTgr.longValue();
        setMotif("");
    }

    public void startMassar2025CheckRetry(){
        situationRetryExec = Executors.newScheduledThreadPool(1);
        Integer rateCollectTgr = 5;
        situationRetryExec.execute(requestBuilderExecutor::checkMassar2025IntegKO);
        isRunningSituationRetry = true;
        dateLancementRetry = new Date();
        rateRetry = rateCollectTgr.longValue();
        setMotif("");

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

    public void stopServiceCheck() {
        if (situationCheckExec != null) {
            situationCheckExec.shutdownNow();
            isRunningSituationCheck = false;
            try {
                if (!situationCheckExec.awaitTermination(60, TimeUnit.SECONDS)) {
                    System.err.println("Executor did not terminate in the specified time.");
                }
            } catch (InterruptedException e) {
                System.err.println("Termination interrupted.");
            } finally {
                if (!situationCheckExec.isTerminated()) {
                    System.err.println("Cancelling non-finished tasks.");
                }
                situationCheckExec.shutdownNow(); // Ensure termination
                System.err.println("Shutdown finished.");
            }
        }
    }



    public void stopServiceRetry() {
        if (situationRetryExec != null) {
            situationRetryExec.shutdownNow();
            isRunningSituationRetry = false;
            try {
                if (!situationRetryExec.awaitTermination(60, TimeUnit.SECONDS)) {
                    System.err.println("Executor did not terminate in the specified time.");
                }
            } catch (InterruptedException e) {
                System.err.println("Termination interrupted.");
            } finally {
                if (!situationRetryExec.isTerminated()) {
                    System.err.println("Cancelling non-finished tasks.");
                }
                situationRetryExec.shutdownNow(); // Ensure termination
                System.err.println("Shutdown finished.");
            }
        }
    }

    public Long getRateCheck() {
        return rateCheck;
    }

    public Long getRateRetry() {
        return rateRetry;
    }





    @EventListener
    public void handleEvent(AlEvent event) {
        if(event == null || event.getPartenaire() == null || event.getEvent() == null) {
            return;
        }
        if(event.getPartenaire().equalsIgnoreCase(PartenaireEnum.MASSAR.name())) {
            switch (event.getEvent()) {
                case "STOP":
                    break;
                case "RUN":
                    startMassar2025Check();
                    break;
                case "STOPRETRY":
                    break;
                case "RUNRETRY":
                    startMassar2025CheckRetry();
                    break;
                default:
                    break;
            }
        }
        System.out.println("Event received: " + event.getPartenaire());
    }
}
