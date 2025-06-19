package ma.cdgp.af.Schedulers;

import ma.cdgp.af.dto.af.PartenaireEnum;
import ma.cdgp.af.entity.ParametrageCollection;
import ma.cdgp.af.repository.*;
import ma.cdgp.af.service.RequestBuilderExecutor;
import ma.cdgp.af.service.RequestTgrExecutor;
import ma.cdgp.af.utils.AlEvent;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class CollectSchedulerService extends SchedulerData {

	private ScheduledExecutorService collectExec = Executors.newSingleThreadScheduledExecutor();
	private boolean isRunningCollect = false;

	private Date dateLancementCollect;
	private Long rateCheck;

	@Autowired private CandidatsRepository candidatRepo;
	@Autowired private RequestTgrExecutor executorTgr;
	@Autowired private RequestBuilderExecutor requestBuilderExecutor;
	@Autowired private ParametrageRepo parametrageRepo;
	@Autowired private LotSituationTgrRepo tgrRepo;
	@Autowired private CandidatCheckEventRepo candidatCheckEventRepo;
	@Autowired private CandidatCollectedRepo candidatCollectedRepo;

	public void startCollect() {
		stopServiceCollect();

		System.out.println("🟢 startCollect triggered");
		collectExec = Executors.newSingleThreadScheduledExecutor();

		// For now, using static rate = 5 seconds
		Integer rateCollect = 5;

		// Schedule the collection task at fixed rate
		collectExec.scheduleAtFixedRate(requestBuilderExecutor.collectCandidats(), 0, rateCollect, TimeUnit.SECONDS);

		isRunningCollect = true;
		dateLancementCollect = new Date();
		rateCheck = rateCollect.longValue();
		setMotif("");
	}

	public void stopServiceCollect() {
		if (collectExec != null && !collectExec.isShutdown()) {
			collectExec.shutdownNow();
			isRunningCollect = false;
			System.err.println("🛑 Collect service stopped.");
		}
	}

	public boolean isRunningCollect() {
		return isRunningCollect;
	}

	public Date getDateLancement() {
		return dateLancementCollect;
	}

	public Long getRateCheck() {
		return rateCheck;
	}

	@EventListener
	public void handleEvent(AlEvent event) {
		if (event == null || event.getPartenaire() == null || event.getEvent() == null) return;

		if (event.getPartenaire().equalsIgnoreCase(PartenaireEnum.COLLECT.name())) {
			switch (event.getEvent()) {
				case "RUN":
					startCollect();
					break;
				case "STOP":
					stopServiceCollect();
					break;
				default:
					break;
			}
		}

		System.out.println("🔔 Event received: " + event.getPartenaire());
	}
}
