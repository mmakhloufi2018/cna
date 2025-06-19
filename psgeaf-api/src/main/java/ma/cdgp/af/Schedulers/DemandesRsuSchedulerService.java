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
public class DemandesRsuSchedulerService extends SchedulerData {
    private ScheduledExecutorService situationCheckExec;
    private ScheduledExecutorService situationRetryExec;

    private boolean isRunningSituationCheck = false;
    private boolean isRunningSituationRetry = false;

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

    public void startDemadesRsuCheck() {
        if (!isRunningSituationCheck) {
            Integer rateCollectTgr = 5;

            situationCheckExec.scheduleAtFixedRate(
                    requestBuilderExecutor.runDemandesRsu(),
                    0, rateCollectTgr, TimeUnit.SECONDS
            );

            situationCheckExec.scheduleAtFixedRate(
                    requestBuilderExecutor.reportRsuStatus(),
                    15, 15, TimeUnit.SECONDS
            );

            isRunningSituationCheck = true;
            dateLancementCheck = new Date();
            rateCheck = rateCollectTgr.longValue();
            setMotif("");
        } else {
            System.out.println("RSU Check is already running.");
        }
    }


    public void startDemandesRsuCheckRetry() {
        stopServiceRetry();
        System.out.println("\uD83D\uDFE2 startDemandesRsuCheckRetry triggered");

        situationRetryExec = Executors.newScheduledThreadPool(1);
        situationRetryExec.scheduleAtFixedRate(
                requestBuilderExecutor.runDemandesRsu(),
                0,
                100,
                TimeUnit.MILLISECONDS
        );

        isRunningSituationRetry = true;
        dateLancementRetry = new Date();
        rateRetry = 100L;
        setMotif("");
    }

    public void stopServiceCheck() {
        if (situationCheckExec != null && !situationCheckExec.isShutdown()) {
            situationCheckExec.shutdown();
            try {
                if (!situationCheckExec.awaitTermination(60, TimeUnit.SECONDS)) {
                    System.err.println("RSU Check Executor did not terminate in the specified time.");
                }
            } catch (InterruptedException e) {
                System.err.println("RSU Check Termination interrupted.");
            } finally {
                isRunningSituationCheck = false;
                System.err.println("RSU Check service stopped.");
            }
        }
    }

    public void stopServiceRetry() {
        if (situationRetryExec != null && !situationRetryExec.isShutdown()) {
            situationRetryExec.shutdown();
            try {
                if (!situationRetryExec.awaitTermination(60, TimeUnit.SECONDS)) {
                    System.err.println("RSU Retry Executor did not terminate in the specified time.");
                }
            } catch (InterruptedException e) {
                System.err.println("RSU Retry Termination interrupted.");
            } finally {
                isRunningSituationRetry = false;
                System.err.println("RSU Retry service stopped.");
            }
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
        System.out.println("\uD83D\uDD14 [EventListener] Received event: " + event);

        if (event == null || event.getPartenaire() == null || event.getEvent() == null) return;

        if (event.getPartenaire().equalsIgnoreCase(PartenaireEnum.RSU.name())) {
            switch (event.getEvent()) {
                case "RUN":
                    startDemadesRsuCheck();
                    break;
                case "RUNRETRY":
                    startDemandesRsuCheckRetry();
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
