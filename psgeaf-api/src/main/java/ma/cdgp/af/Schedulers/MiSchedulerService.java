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
public class MiSchedulerService extends SchedulerData{

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

    MiSchedulerService(){
        situationCheckExec = Executors.newScheduledThreadPool(1);
        situationRetryExec = Executors.newScheduledThreadPool(1);
    }

    public void startMiCheck() {
        situationCheckExec = Executors.newScheduledThreadPool(1);
//        ParametrageCollection paramCnss = parametrageRepo.findByPartenaire(PartenaireEnum.MI.name());
//        Integer rateCollectTgr = paramCnss != null && paramCnss.getRateCollect() != null
//                ? paramCnss.getRateCollect().intValue()
//                : 5;
        Integer rateCollectTgr = 5;
        situationCheckExec.execute(requestBuilderExecutor::runMiCheck);
        isRunningSituationCheck = true;
        dateLancementCheck = new Date();
        rateCheck = rateCollectTgr.longValue();
        setMotif("");
    }

    public void startMiCheckRetry(){

        situationRetryExec = Executors.newScheduledThreadPool(1);
//        ParametrageCollection paramTgr = parametrageRepo.findByPartenaire(PartenaireEnum.MI.name());
//        Integer rateCollectTgr = paramTgr != null && paramTgr.getRateRetry() != null
//                ? paramTgr.getRateRetry().intValue()
//                : 5;
        Integer rateCollectTgr = 5;
        situationRetryExec.execute(requestBuilderExecutor::checkMiIntegKO);
//        situationRetryExec.execute(requestBuilderExecutor::checkMiIntegKO);
        isRunningSituationRetry = true;
        dateLancementRetry = new Date();
        rateRetry = rateCollectTgr.longValue();
        setMotif("");
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
        situationCheckExec.shutdown();
        isRunningSituationCheck = false;
    }


    public void stopServiceRetry() {
        situationRetryExec.shutdown();
        isRunningSituationRetry = false;
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
        if(event.getPartenaire().equalsIgnoreCase(PartenaireEnum.MI.name())) {
            switch (event.getEvent()) {
                case "STOP":
                    break;
                case "RUN":
                    startMiCheck();
                    break;
                case "STOPRETRY":
                    break;
                case "RUNRETRY":
                    startMiCheckRetry();
                    break;
                default:
                    break;
            }
        }
        System.out.println("Event received: " + event.getPartenaire());
    }
}
