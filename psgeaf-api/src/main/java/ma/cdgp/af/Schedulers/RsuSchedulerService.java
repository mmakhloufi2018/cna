package ma.cdgp.af.Schedulers;

import ma.cdgp.af.entity.ParametrageCollection;

import ma.cdgp.af.repository.CandidatCheckEventRepo;
import ma.cdgp.af.repository.CandidatsRepository;
import ma.cdgp.af.repository.LotSituationTgrRepo;
import ma.cdgp.af.repository.ParametrageRepo;
import ma.cdgp.af.service.RequestBuilderExecutor;
import ma.cdgp.af.service.RequestTgrExecutor;
import ma.cdgp.af.service.ServiceRsuSender;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class RsuSchedulerService {
	
	private ScheduledExecutorService situationCheckExec;
	private boolean isRunningSituationCheck;
	
	private ScheduledExecutorService situationRetryExec;
	private boolean isRunningSituationRetry;
	
	private Date dateLancementCheck;
	private Date dateLancementRetry;
	
	private Long rateCheck;
	private Long rateRetry;
	@Autowired
	CandidatsRepository candidatRepo;

	@Autowired
	RequestTgrExecutor executorTgr;

	@Autowired
	RequestBuilderExecutor requestBuilderExecutor;

	@Autowired
	ParametrageRepo parametrageRepo;

	@Autowired
	LotSituationTgrRepo tgrRepo;

	@Autowired
	CandidatCheckEventRepo candidatCheckEventRepo;
	@Autowired
	ServiceRsuSender rsuSender;
	
	RsuSchedulerService() {
		situationCheckExec = Executors.newScheduledThreadPool(1);
		situationRetryExec = Executors.newScheduledThreadPool(1);
	}
	public void startSent() {
		situationCheckExec = Executors.newScheduledThreadPool(1);
		ParametrageCollection paramCnss = parametrageRepo.findByPartenaire("RSU");
		if (paramCnss == null || BooleanUtils.isNotTrue(paramCnss.getActiveCollect())) {
			System.out.println("Collecting RSU désactivée ");
			return ;
		}
		Integer rateCollect = paramCnss.getRateCollect() != null ? paramCnss.getRateCollect().intValue() : 5;
		situationCheckExec.scheduleAtFixedRate(rsuSender.runTask(), 0, rateCollect,
				TimeUnit.SECONDS);
		isRunningSituationCheck = true;
		dateLancementCheck = new Date();
		rateCheck = rateCollect.longValue();
	}
	 

	public boolean isServiceRunningCheck() {
		return isRunningSituationCheck;
	}
	public boolean isServiceRunningRetry() {
		return isRunningSituationRetry;
	}

	public void stopServiceCheck() {
		situationCheckExec.shutdown();
		isRunningSituationCheck = false;
	}
	
	public void stopServiceRetry() {
		situationRetryExec.shutdown();
		isRunningSituationRetry = false;
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
	
	
}
