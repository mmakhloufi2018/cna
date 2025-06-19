package ma.cdgp.af.service;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.transaction.Transactional;

import ma.cdgp.af.entity.NotificationCollected;
import ma.cdgp.af.entity.massar2025.CandidatCheckEventMassar2025;
import ma.cdgp.af.entity.massar2025.DemandeSituationMassar2025;
import ma.cdgp.af.entity.massar2025.LotSituationMassar2025;
import ma.cdgp.af.repository.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;

import com.google.common.collect.Lists;

import ma.cdgp.af.Schedulers.NotificationTgrSchedulerService;
import ma.cdgp.af.dto.af.CandidatInfos;
import ma.cdgp.af.dto.af.CriteresTransactionDto;
import ma.cdgp.af.dto.af.rsu.LotDemandeRsuDto;
import ma.cdgp.af.dto.af.tgr.BenefNotificationTgrDto;
import ma.cdgp.af.dto.af.tgr.LotNotifTgrDto;
import ma.cdgp.af.dto.af.tgr.LotNotificationTgrDto;
import ma.cdgp.af.entity.CandidatCheckEvent;
import ma.cdgp.af.entity.CandidatCollected;
import ma.cdgp.af.entity.ParametrageCollection;
import ma.cdgp.af.entity.cmr.DemandeSituationCmr;
import ma.cdgp.af.entity.cmr.LotSituationCmr;
import ma.cdgp.af.entity.cnss.DemandeSituationCnss;
import ma.cdgp.af.entity.cnss.LotSituationCnss;
import ma.cdgp.af.entity.massar.DemandeSituationMassar;
import ma.cdgp.af.entity.massar.LotSituationMassar;
import ma.cdgp.af.entity.mi.DemandeSituationMi;
import ma.cdgp.af.entity.mi.LotSituationMi;
import ma.cdgp.af.entity.rsu.CollectedDemandesRsu;
import ma.cdgp.af.entity.rsu.LotDemandeRsu;
import ma.cdgp.af.entity.sante.DemandeSituationSante;
import ma.cdgp.af.entity.sante.LotSituationSante;
import ma.cdgp.af.entity.tgr.DemandeSituationTgr;
import ma.cdgp.af.entity.tgr.LotNotifTgr;
import ma.cdgp.af.entity.tgr.LotSituationTgr;
import ma.cdgp.af.esgaf.EsgeafRepository;
import ma.cdgp.af.esgaf.RsuDemandesEsgeafRepository;
import ma.cdgp.af.messaging.LotCollRabbitDto;
import ma.cdgp.af.messaging.LotNotifTGRRabbitDto;
import ma.cdgp.af.messaging.LotRabbitDto;
import ma.cdgp.af.messaging.LotRabitDemandesRsuDto;
import ma.cdgp.af.messaging.LotSenderService;
import ma.cdgp.af.messaging.SenderTGR;
import ma.cdgp.af.utils.Utils;

@Service
public class RequestBuilderExecutor {

	@Autowired
	CandidatsRepository candidatRepo;

	@Autowired
	LotSituationCmrRepo cmrRepo;

	@Autowired
	LotSituationMiRepo miRepo;

	@Autowired
	RequestCmrExecutor cmrExec;

	@Autowired
	RequestMiExecutor miExec;

	@Autowired
	LotSituationTgrRepo tgrRepo;

	@Autowired
	RequestTgrExecutor tgrExec;

	@Autowired
	CandidatCheckEventRepo candidatCheckEventRepo;

	@Autowired
	CandidatCheckEventMassar2025Repo candidatCheckEventMassar2025Repo;

	@Autowired
	CandidatCollectedRepo candidatCollectedRepo;

	@Autowired
	RequestSanteExecutor santeExec;

	@Autowired
	LotSituationSanteRepo santeRepo;

	@Autowired
	RequestCnssExecutor cnssExec;

	@Autowired
	LotSituationCnssRepo cnssRepo;

	@Autowired
	RequestMassarExecutor massarExec;

	@Autowired
	RequestMassar2025Executor massar2025Executor;


	@Autowired
	LotSenderService lotSender;

	@Autowired
	LotSituationMassarRepo massarRepo;

	@Autowired
	LotSituationMassar2025Repo massar2025Repo;

	@Autowired
	ParametrageRepo parametrageRepo;

	@Autowired
	ServiceTgrSender serviceTgrSender;
	
	@Autowired
	SenderTGR senderTGR;
	
	@Autowired
	EsgeafRepository repoEseag;

	@Autowired
	RsuDemandesEsgeafRepository rsuDemandesEsgeafRepository;

	@Autowired
	NotificationCollectedRepository notificationCollectedRepository;

	@Autowired
	LotDemandeRsuRepository lotDemandeRsuRepository;


	@Autowired
	DemandeRsuRepository demandeRsuRepository;

	@Autowired
	ServiceDemandeRsuSender serviceDemandeRsuSender;

	@Autowired
	CollectedDemandesRsuRepository collectedDemandesRsuRepository;

	@Autowired
	LotNotifTgrRepo lotNotifTgrRepo;


	@Autowired
	NotificationTgrSchedulerService notificationTgrSchedulerService;

	private int currentPage = 0;
	private final AtomicInteger lotsSentCount = new AtomicInteger(0);
	private final AtomicInteger totalDemandesSentCount = new AtomicInteger(0);
	private final AtomicLong startTime = new AtomicLong(0);

	private List<CollectedDemandesRsu> currentBatch = new ArrayList<>();
	private int currentBatchIndex = 0;


	private static final Logger loggerDemandeSituationMassar = LoggerFactory.getLogger(DemandeSituationMassar.class);

	private static final Logger loggerDemandeSituationMassar2025 = LoggerFactory.getLogger(DemandeSituationMassar2025.class);


	public Runnable collectCandidats() {
		return () -> {
			System.out.println("📥 Starting Collecting Candidats at: " + Utils.dateToStringTime(new Date()));
			try {
				CriteresTransactionDto rt = new CriteresTransactionDto();
				rt.setPageSize(10000);
				rt.setPageNumber(0);

				List<CandidatInfos> fetchedList = candidatRepo.getAllCandidats(rt);

				if (CollectionUtils.isNotEmpty(fetchedList)) {
					String reference = String.valueOf(System.currentTimeMillis());
					List<List<CandidatInfos>> partitions = Lists.partition(fetchedList, 100);

					System.out.println("📦 Total candidates to collect: " + fetchedList.size());

					for (List<CandidatInfos> batch : partitions) {
						List<Long> idsToFlag = batch.stream()
								.map(CandidatInfos::getId)
								.filter(Objects::nonNull)
								.map(Long::valueOf)
								.collect(Collectors.toList());

						candidatRepo.flagCandidatCollected(idsToFlag);
						lotSender.sendLotCollect(new LotCollRabbitDto(batch, reference), "COLLECT-ASD");

						System.out.println("✅ Sent batch of size: " + batch.size());
					}
				} else {
					System.out.println("ℹ️ No Candidats to collect.");
				}

			} catch (Exception e) {
				System.err.println("❌ Error during candidat collection:");
				e.printStackTrace();
			}
		};
	}


	public Runnable runCnssCheck() {
		return () -> {
			System.out.println("▶️ Starting CNSS CHECK Task at: " + Utils.dateToStringTime(new Date()));
			try {
				while (!Thread.currentThread().isInterrupted()) {
					ParametrageCollection param = parametrageRepo.findByPartenaire("CNSS");
					if (param == null || !BooleanUtils.isTrue(param.getActiveCollect())) {
						System.out.println("❌ CNSS collection is disabled.");
						break;
					}

					Integer maxSize = param.getMaxSize() != null ? param.getMaxSize().intValue() : 100;
					Pageable pageable = PageRequest.of(0, maxSize * 30);
					Page<CandidatCollected> page = candidatCollectedRepo.findMissedCnssCandidat(pageable);

					if (page != null && CollectionUtils.isNotEmpty(page.getContent())) {
						List<CandidatCollected> valid = page.getContent().stream()
								.filter(c -> c.getCin() != null && !c.getCin().contains(" "))
								.collect(Collectors.toList());
						if (valid.isEmpty()) {
							System.out.println("ℹ️ No CNSS candidates to collect.");
						}

						List<List<CandidatCollected>> parts = Lists.partition(valid, maxSize);
						for (List<CandidatCollected> batch : parts) {
							System.out.println("🚀 Processing CNSS batch of size: " + batch.size());
							buildAndSendCnssLot(batch, BooleanUtils.isTrue(param.getUseRabbit()));
						}
					} else {
						System.out.println("ℹ️ No CNSS candidates to collect.");
					}

					Thread.sleep(5000); // 5 seconds delay
				}
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
				System.err.println("🛑 Interrupted: Exiting CNSS check loop.");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				System.out.println("✅ Finished CNSS CHECK Task.");
			}
		};
	}


	public Runnable runCmrCheck() {
		return () -> {
			System.out.println("▶️ Starting CMR CHECK Task at: " + Utils.dateToStringTime(new Date()));
			try {
				while (!Thread.currentThread().isInterrupted()) {
					ParametrageCollection param = parametrageRepo.findByPartenaire("CMR");
					if (param == null || !BooleanUtils.isTrue(param.getActiveCollect())) {
						System.out.println("❌ CMR collection is disabled.");
						break;
					}

					Integer maxSize = param.getMaxSize() != null ? param.getMaxSize().intValue() : 100;
					Pageable pageable = PageRequest.of(0, maxSize * 30);
					Page<CandidatCollected> page = candidatCollectedRepo.findMissedCmrCandidat(pageable);

					if (page != null && CollectionUtils.isNotEmpty(page.getContent())) {
						List<CandidatCollected> valid = page.getContent().stream()
								.filter(c -> c.getCin() != null && !c.getCin().contains(" "))
								.collect(Collectors.toList());
						if (valid.isEmpty()) {
							System.out.println("ℹ️ No CMR candidates to collect");
						}
						List<List<CandidatCollected>> parts = Lists.partition(valid, maxSize);
						for (List<CandidatCollected> batch : parts) {
							buildCmrLot(batch, BooleanUtils.isTrue(param.getUseRabbit()));
						}
					} else {
						System.out.println("ℹ️ No CMR candidates to collect.");
					}

					try {
						Thread.sleep(5000); // Sleep 5 seconds before next iteration
					} catch (InterruptedException ex) {
						Thread.currentThread().interrupt();
						System.err.println("🛑 Interrupted: Exiting CMR check loop.");
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				System.out.println("✅ Finished CMR CHECK Task.");
			}
		};
	}


	public void runMiCheck() {
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		Runnable task = () -> {
			try {
				System.out.println("Collecting Candidats MI .... ");
				ParametrageCollection paramCnss = parametrageRepo.findByPartenaire("MI");
				if (paramCnss == null || BooleanUtils.isNotTrue(paramCnss.getActiveCollect())) {
					System.out.println("Collecting Candidats MI désactivée ");
					return;
				}
				System.out.println("Collecting Candidats MI .... " + paramCnss.getActiveCollect());
				Integer maxSize = paramCnss.getMaxSize() != null ? paramCnss.getMaxSize().intValue() : 100;
				System.out.println("Collecting MI Candidats executed : at: " + Utils.dateToStringTime(new Date()));
				Pageable pageable = PageRequest.of(0, maxSize * 10 );
				Page<CandidatCollected> repsCnss = null;
				repsCnss = candidatCollectedRepo.findMissedMiCandidat(pageable);
				if (repsCnss != null && CollectionUtils.isNotEmpty(repsCnss.getContent())) {
					try {
						List<List<CandidatCollected>> parts = Lists.partition(repsCnss.getContent(), maxSize);
						for (List<CandidatCollected> subList : parts) {
							buildMiLot(subList, BooleanUtils.isTrue(paramCnss.getUseRabbit()));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					System.err.println("Collecting Missed MI : no data to collect");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		};
		scheduler.scheduleAtFixedRate(task, 0, 1, TimeUnit.SECONDS);
	}

//	public Runnable runTgrCheck() {
//		return () -> {
//			System.out.println("▶️ Starting TGR CHECK Task at: " + Utils.dateToStringTime(new Date()));
//			try {
//				while (!Thread.currentThread().isInterrupted()) {
//					ParametrageCollection param = parametrageRepo.findByPartenaire("TGR");
//					if (param == null || !BooleanUtils.isTrue(param.getActiveCollect())) {
//						System.out.println("❌ TGR collection is disabled.");
//						break;
//					}
//
//					Integer maxSize = param.getMaxSize() != null ? param.getMaxSize().intValue() : 100;
//					Pageable pageable = PageRequest.of(0, maxSize * 30);
//					Page<CandidatCollected> page = candidatCollectedRepo.findMissedTgrCandidat(pageable);
//
//					if (page != null && CollectionUtils.isNotEmpty(page.getContent())) {
//						List<CandidatCollected> valid = page.getContent().stream()
//								.filter(c -> c.getCin() != null && !c.getCin().contains(" "))
//								.collect(Collectors.toList());
//						List<List<CandidatCollected>> parts = Lists.partition(valid, maxSize);
//						for (List<CandidatCollected> batch : parts) {
//							buildTgrLot(batch, BooleanUtils.isTrue(param.getUseRabbit()));
//						}
//					} else {
//						System.out.println("ℹ️ No TGR candidates to collect.");
//					}
//
//					try {
//						Thread.sleep(5000); // Sleep 5 seconds before next iteration
//					} catch (InterruptedException ex) {
//						Thread.currentThread().interrupt();
//						System.err.println("🛑 Interrupted: Exiting TGR check loop.");
//						break;
//					}
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			} finally {
//				System.out.println("✅ Finished TGR CHECK Task.");
//			}
//		};
//	}

	public Runnable runTgrCheck() {
		return () -> {
			System.out.println("▶️ Starting TGR CHECK Task at: " + Utils.dateToStringTime(new Date()));
			try {
				while (!Thread.currentThread().isInterrupted()) {
					ParametrageCollection param = parametrageRepo.findByPartenaire("TGR");
					if (param == null || !BooleanUtils.isTrue(param.getActiveCollect())) {
						System.out.println("❌ TGR collection is disabled.");
						break;
					}

					Integer maxSize = param.getMaxSize() != null ? param.getMaxSize().intValue() : 100;
					Pageable pageable = PageRequest.of(0, maxSize * 30);

					Page<CandidatCollected> page = candidatCollectedRepo.findMissedTgrCandidat(pageable);


					if (page != null && CollectionUtils.isNotEmpty(page.getContent())) {
						List<CandidatCollected> valid = page.getContent().stream()
								.filter(c -> c.getCin() != null && !c.getCin().contains(" "))
								.collect(Collectors.toList());
						if (valid.isEmpty()) {
							System.out.println("ℹ️ No TGR candidates to collect");
						}
						List<List<CandidatCollected>> parts = Lists.partition(valid, maxSize);
						for (List<CandidatCollected> batch : parts) {
							buildTgrLot(batch, BooleanUtils.isTrue(param.getUseRabbit()));
						}
					} else {
						System.out.println("ℹ️ No TGR candidates to collect.");
					}

					Thread.sleep(5000);
				}
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
				System.err.println("🛑 Interrupted: Exiting TGR check loop.");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				System.out.println("✅ Finished TGR CHECK Task.");
			}
		};
	}


	public Runnable runMassarCheck() {
		return () -> {
			System.out.println("▶️ Starting MASSAR CHECK Task at: " + Utils.dateToStringTime(new Date()));
			try {
				while (!Thread.currentThread().isInterrupted()) {
//					System.out.println("INSIDE !Thread.currentThread().isInterrupted()");
					ParametrageCollection param = parametrageRepo.findByPartenaire("MASSAR");
					if (param == null || !BooleanUtils.isTrue(param.getActiveCollect())) {
						System.out.println("❌ MASSAR collection is disabled.");
						break;
					}

					Integer maxSize = param.getMaxSize() != null ? param.getMaxSize().intValue() : 100;
					Pageable pageable = PageRequest.of(0, maxSize * 30);
					Page<CandidatCollected> page = candidatCollectedRepo.findMissedMassarCandidat(pageable);
//					System.out.println("SIZE MASSSAR >>>>> " + page.stream().count());
					if (page != null && CollectionUtils.isNotEmpty(page.getContent())) {
						List<List<CandidatCollected>> parts = Lists.partition(page.getContent(), maxSize);
						for (List<CandidatCollected> batch : parts) {
							System.out.println("inside batchs : parts" + batch.size());
//							if (Thread.currentThread().isInterrupted()) {
//								System.err.println("🛑 Interrupted during MASSAR batch processing.");
//								return;
//							}
							buildMassarLot(batch, BooleanUtils.isTrue(param.getUseRabbit()));
						}
					} else {
						System.out.println("ℹ️ No MASSAR candidates to collect.");
					}

					try {
						Thread.sleep(5000);
					} catch (InterruptedException ex) {
						Thread.currentThread().interrupt();
						System.err.println("🛑 Interrupted: Exiting MASSAR check loop.");
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				System.out.println("✅ Finished MASSAR CHECK Task.");
			}
		};
	}



	public void runMassar2025Check() {
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		Runnable task = () -> {

			try {
				ParametrageCollection paramCnss = parametrageRepo.findByPartenaire("MASSAR");
				if (paramCnss == null || BooleanUtils.isNotTrue(paramCnss.getActiveCollect())) {
					System.out.println("Collecting Candidats MASSAR désactivée ");
					return;
				}
				Integer maxSize = paramCnss.getMaxSize() != null ? paramCnss.getMaxSize().intValue() : 100;
				System.out.println("Collecting MASSAR Candidats executed : at: " + Utils.dateToStringTime(new Date()));
				Pageable pageable = PageRequest.of(0, maxSize * 300);
				Page<CandidatCollected> repsCnss = null;
				repsCnss = candidatCollectedRepo.findMissedMassar2025Candidat(pageable);
				if (repsCnss != null && CollectionUtils.isNotEmpty(repsCnss.getContent())) {
					try {
						List<List<CandidatCollected>> parts = Lists.partition(repsCnss.getContent(), maxSize);
						for (List<CandidatCollected> subList : parts) {
							buildMassar2025Lot(subList, paramCnss.getUseRabbit());
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					System.err.println("Collecting Missed MASSAR : no data to collect");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		};
		scheduler.scheduleAtFixedRate(task, 0, 5, TimeUnit.SECONDS);
	}

	public Runnable runSanteCheck() {
		return () -> {
			System.out.println("▶️ SANTE CHECK started at: " + Utils.dateToStringTime(new Date()));
			try {
				while (!Thread.currentThread().isInterrupted()) {

					ParametrageCollection paramCnss = parametrageRepo.findByPartenaire("SANTE");
					if (paramCnss == null || BooleanUtils.isNotTrue(paramCnss.getActiveCollect())) {
						System.out.println("❌ Collecting Candidats SANTE désactivée");
						break;
					}

					Integer maxSize = paramCnss.getMaxSize() != null ? paramCnss.getMaxSize().intValue() : 100;
					System.out.println("📥 Collecting SANTE Candidats at: " + Utils.dateToStringTime(new Date()));
					Pageable pageable = PageRequest.of(0, maxSize);
					Page<CandidatCollected> repsCnss = candidatCollectedRepo.findMissedSanteCandidat(pageable);

					if (repsCnss != null && CollectionUtils.isNotEmpty(repsCnss.getContent())) {
						try {
							buildSanteLot(repsCnss.getContent());
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						System.out.println("ℹ️ No SANTE candidates to collect.");
					}

					try {
						Thread.sleep(5000); // wait 5 seconds
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
						System.out.println("🛑 SANTE CHECK was interrupted. Exiting...");
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				System.out.println("✅ SANTE CHECK task finished.");
			}
		};
	}






	private void buildCmrLot(List<CandidatCollected> candidats, Boolean useRabbit) throws Exception {
		LotSituationCmr requestEntity = new LotSituationCmr();
		requestEntity.setPartenaire("CMR");
		requestEntity.setDateCreation(new Date());
		requestEntity.setEtatLot("CREATED");
		requestEntity.setDateLot(Utils.dateToStringddMMyyyy(new Date()));
		requestEntity.setLotId(Calendar.getInstance().getTimeInMillis() + "");
		requestEntity.setDemandes(candidats.stream().map(d -> {
			CandidatCheckEvent event = CandidatCheckEvent.fromCollected(d, "CMR");
			CandidatCheckEvent savedEvent = candidatCheckEventRepo.save(event);
			DemandeSituationCmr dp = new DemandeSituationCmr();
			dp.setCin(d.getCin());
			dp.setCandidatEvent(savedEvent);
//           dp.setDateNaissance(d.getDateNaissance());
			return dp;
		}).collect(Collectors.toSet()));
		requestEntity.setters();
		LotSituationCmr savedRR = cmrRepo.save(requestEntity);
		if (BooleanUtils.isTrue(useRabbit)) {
			lotSender.sendLotCmr(new LotRabbitDto(savedRR.getId().toString(), "CMR"), "CMR-ASD");
		} else {
			cmrExec.traiterDemandes(savedRR.getId());
		}

	}

	private void buildTgrLot(List<CandidatCollected> candidats, Boolean useRabbit) throws Exception {
		LotSituationTgr requestEntity = new LotSituationTgr();
		requestEntity.setPartenaire("TGR");
		requestEntity.setDateCreation(new Date());
		requestEntity.setDateLot(Utils.dateToStringddMMyyyy(new Date()));
		requestEntity.setEtatLot("CREATED");
		requestEntity.setLotId(Calendar.getInstance().getTimeInMillis() + "");
		requestEntity.setDemandes(candidats.stream().map(d -> {
			CandidatCheckEvent event = CandidatCheckEvent.fromCollected(d, "TGR");
			CandidatCheckEvent savedEvent = candidatCheckEventRepo.save(event);
			DemandeSituationTgr dp = new DemandeSituationTgr();
			dp.setCin(d.getCin());
			dp.setCandidatEvent(savedEvent);
			try {
				Date dateNaiss = Utils.stringToDateTime(d.getDateNaissance());
				dp.setDateNaissance(Utils.dateToStringddMMyyyy(dateNaiss));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return dp;
		}).collect(Collectors.toSet()));
		requestEntity.setters();
		LotSituationTgr savedRR = tgrRepo.save(requestEntity);

		if (BooleanUtils.isTrue(useRabbit)) {
			lotSender.sendLotTgr(new LotRabbitDto(savedRR.getId().toString(), "TGR"), "TGR-ASD");
		} else {
			tgrExec.traiterDemandes(savedRR.getId());
		}
	}

	private void buildSanteLot(List<CandidatCollected> candidats) throws Exception {
		LotSituationSante requestEntity = new LotSituationSante();
		requestEntity.setPartenaire("SANTE");
		requestEntity.setDateCreation(new Date());
		requestEntity.setDateLot(Utils.dateToStringYYYY_MM_dd(new Date()));
		requestEntity.setEtatLot("CREATED");
		requestEntity.setLotId(Calendar.getInstance().getTimeInMillis() + "");
		requestEntity.setDemandes(candidats.stream().map(d -> {
			CandidatCheckEvent event = CandidatCheckEvent.fromCollected(d, "SANTE");
			CandidatCheckEvent savedEvent = candidatCheckEventRepo.save(event);
			DemandeSituationSante dp = new DemandeSituationSante();
			dp.setCandidatEvent(savedEvent);
			dp.setIdcs(d.getIdcs());
			return dp;
		}).collect(Collectors.toSet()));
		requestEntity.setters();
		LotSituationSante savedRR = santeRepo.save(requestEntity);
		santeExec.traiterDemandes(savedRR.getId());
	}

	private void buildAndSendCnssLot(List<CandidatCollected> candidats, boolean useRabbit) throws Exception {
		LotSituationCnss lot = new LotSituationCnss();
		lot.setPartenaire("CNSS");
		lot.setDateCreation(new Date());
		lot.setDateLot(Utils.dateToStringYYYYMMdd(new Date()));
		lot.setEtatLot("CREATED");
		lot.setLotId(String.valueOf(System.currentTimeMillis()));

		Set<DemandeSituationCnss> demandes = candidats.stream()
				.filter(c -> c.getCin() != null)
				.map(c -> {
					CandidatCheckEvent event = CandidatCheckEvent.fromCollected(c, "CNSS");
					CandidatCheckEvent saved = candidatCheckEventRepo.save(event);

					DemandeSituationCnss d = new DemandeSituationCnss();
					d.setCin(c.getCin());
					d.setIdcs(c.getIdcs());
					d.setCandidatEvent(saved);
					try {
						Date dateNaiss = Utils.stringToDateTime(c.getDateNaissance());
						d.setDateNaissance(Utils.dateToStringYYYYMMdd(dateNaiss));
					} catch (Exception e) {
						e.printStackTrace();
					}
					return d;
				})
				.collect(Collectors.toSet());

		lot.setDemandes(demandes);
		lot.setters();

		LotSituationCnss savedLot = cnssRepo.save(lot);

		if (useRabbit) {
			lotSender.sendLotCnss(new LotRabbitDto(savedLot.getId().toString(), "CNSS"), "CNSS-ASD");
		} else {
			cnssExec.traiterDemandes(savedLot.getId());
		}
	}

	private void buildMassarLot(List<CandidatCollected> candidats, Boolean useRabbit) throws Exception {

		System.out.println("INSIDE BUILDMASSAR");
		LotSituationMassar requestEntity = new LotSituationMassar();
		requestEntity.setPartenaire("MASSAR");
		requestEntity.setDateCreation(new Date());
		requestEntity.setDateLot(Utils.dateToStringYYYYMMdd(new Date()));
		requestEntity.setEtatLot("CREATED");
		requestEntity.setLotId(Calendar.getInstance().getTimeInMillis() + "");
		requestEntity.setDemandes(candidats.stream().filter(t -> t.getMassar() != null).map(d -> {
			CandidatCheckEvent event = CandidatCheckEvent.fromCollected(d, "MASSAR");
			CandidatCheckEvent savedEvent = candidatCheckEventRepo.save(event);
			DemandeSituationMassar dp = new DemandeSituationMassar();
			dp.setNumMassar(d.getMassar());
			dp.setAnneeScolaire(null);
			dp.setCandidatEvent(savedEvent);
			return dp;
		}).collect(Collectors.toSet()));
		requestEntity.setters();
		LotSituationMassar savedRR = massarRepo.save(requestEntity);
		if (BooleanUtils.isTrue(useRabbit)) {
			lotSender.sendLot(new LotRabbitDto(savedRR.getId().toString(), "MASSAR"), "MASSAR-ASD");
		} else {
			massarExec.traiterDemandes(savedRR.getId());
		}

	}

	private void buildMassar2025Lot (List<CandidatCollected> candidats, Boolean useRabbit) throws Exception {
		LotSituationMassar2025 requestEntity = new LotSituationMassar2025();
		requestEntity.setPartenaire("MASSAR");
		requestEntity.setDateCreation(new Date());
		requestEntity.setDateLot(Utils.dateToStringYYYYMMdd(new Date()));
		requestEntity.setEtatLot("CREATED");
		requestEntity.setLotId(Calendar.getInstance().getTimeInMillis() + "");
		requestEntity.setDemandes(candidats.stream().filter(t -> t.getMassar() != null).map(d -> {
			CandidatCheckEventMassar2025 event = CandidatCheckEventMassar2025.fromCollected(d, "MASSAR");
			CandidatCheckEventMassar2025 savedEvent = candidatCheckEventMassar2025Repo.save(event);
			DemandeSituationMassar2025 dp = new DemandeSituationMassar2025();
			dp.setNumMassar(d.getMassar());
			dp.setAnneeScolaire(Utils.getNextYear());
			dp.setCandidatEvent(savedEvent);
//			loggerDemandeSituationMassar2025.info("DemandeSituationMassar2025: {}", dp);
			return dp;
		}).collect(Collectors.toSet()));
		requestEntity.setters();
		LotSituationMassar2025 savedRR = massar2025Repo.save(requestEntity);
		if (BooleanUtils.isTrue(useRabbit)) {
			lotSender.sendLotMassar2025(new LotRabbitDto(savedRR.getId().toString(), "MASSAR2025"), "MASSAR2025-ASD");
		} else {
			massar2025Executor.traiterDemandes2025(savedRR.getId());
		}

	}

	private void buildMiLot(List<CandidatCollected> candidats, Boolean isRabbit) throws Exception {
		LotSituationMi requestEntity = new LotSituationMi();
		requestEntity.setPartenaire("MI");
		requestEntity.setDateCreation(new Date());
		requestEntity.setEtatLot("CREATED");
		requestEntity.setDateLot(Utils.dateToStringddMMyyyy(new Date()));
		requestEntity.setLotId(Calendar.getInstance().getTimeInMillis() + "");
		requestEntity.setDemandes(candidats.stream().map(d -> {
			CandidatCheckEvent event = CandidatCheckEvent.fromCollected(d, "MI");
			CandidatCheckEvent savedEvent = candidatCheckEventRepo.save(event);
			DemandeSituationMi dp = new DemandeSituationMi();
			dp.setCin(d.getCin());
			dp.setCandidatEvent(savedEvent);
			return dp;
		}).collect(Collectors.toSet()));
		requestEntity.setters();
		LotSituationMi savedRR = miRepo.save(requestEntity);
		if (BooleanUtils.isTrue(isRabbit)) {
			lotSender.sendLotMi(new LotRabbitDto(savedRR.getId().toString(), "MI"), "MI-ASD");
		} else {
			miExec.traiterDemandes(savedRR.getId());
		}
	}
	public Runnable runCnssRetryCheck() {
		return () -> {
			System.out.println("▶️ Starting CNSS RETRY Task at: " + Utils.dateToStringTime(new Date()));
			try {
				while (!Thread.currentThread().isInterrupted()) {
					ParametrageCollection param = parametrageRepo.findByPartenaire("CNSS");

					if (param == null || !BooleanUtils.isTrue(param.getActiveRetryKo())) {
						System.out.println("❌ CNSS retry disabled.");
						break;
					}

					System.out.println("🔄 Checking CNSS Integration KO...");
					checkCnssIntegKO(); // already handles its own internal logic

					System.out.println("🔄 Checking CNSS Response KO...");
					checkCnssRepKO(); // already handles its own internal logic

					Thread.sleep(5000); // sleep between iterations
				}
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
				System.err.println("🛑 Interrupted: Exiting CNSS retry loop.");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				System.out.println("✅ Finished CNSS RETRY Task.");
			}
		};
	}

	public void checkCnssRepKO() {
		try {
			ParametrageCollection param = parametrageRepo.findByPartenaire("CNSS");
			if (param == null || !BooleanUtils.isTrue(param.getActiveRetryKo())) {
				System.out.println("CNSS response retry disabled.");
				return;
			}

			Pageable pageable = PageRequest.of(0, 100);
			Page<LotSituationCnss> repKoLots = cnssRepo.findReponseKO(pageable);

			if (repKoLots == null || CollectionUtils.isEmpty(repKoLots.getContent())) {
				System.out.println("No CNSS lots with KO_REP or WAITING_RESPONSE.");
				return;
			}

			for (LotSituationCnss lot : repKoLots.getContent()) {
				if (CollectionUtils.isEmpty(lot.getPersonnes())) {
					System.out.println("Requesting CNSS response for lot ID: " + lot.getId());
					cnssExec.lancerRecupReponses(lot.getId());
				} else {
					System.out.println("Responses already exist for lot ID: " + lot.getId());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void checkCnssIntegKO() {
		try {
			ParametrageCollection param = parametrageRepo.findByPartenaire("CNSS");
			if (param == null || !BooleanUtils.isTrue(param.getActiveRetryKo())) {
				System.out.println("CNSS retry disabled.");
				return;
			}

			Pageable pageable = PageRequest.of(0, 500);
			Page<LotSituationCnss> koLots = cnssRepo.findIntegrationKO(pageable);

			if (koLots == null || CollectionUtils.isEmpty(koLots.getContent())) {
				System.out.println("No CNSS lots with KO_SEND state.");
				return;
			}

			for (LotSituationCnss lot : koLots.getContent()) {
				System.out.println("Re-integrating CNSS lot ID: " + lot.getId());
				cnssExec.traiterDemandes(lot.getId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Runnable checkCmrIntegKO() {
		return () -> {
			System.out.println("▶️ Starting CMR RETRY-INTEGRATION Task at: " + Utils.dateToStringTime(new Date()));
			try {
				ParametrageCollection param = parametrageRepo.findByPartenaire("CMR");
				if (param == null || !BooleanUtils.isTrue(param.getActiveRetryKo())) {
					System.out.println("❌ Retry for CMR is disabled.");
					return;
				}

				Pageable pageable = PageRequest.of(0, 500);
				Page<LotSituationCmr> failedLots = cmrRepo.findIntegrationKO(pageable);
				if (failedLots != null && CollectionUtils.isNotEmpty(failedLots.getContent())) {
					for (LotSituationCmr lot : failedLots.getContent()) {
						System.err.println("🔁 Retrying CMR Integration for lot ID: " + lot.getId());
						cmrExec.traiterDemandes(lot.getId());
					}
				} else {
					System.out.println("ℹ️ No CMR lots in KO state to retry.");
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				System.out.println("✅ Finished CMR RETRY-INTEGRATION Task.");
			}
		};
	}


	public Runnable checkTgrIntegKO() {
		return () -> {
			try {
				ParametrageCollection param = parametrageRepo.findByPartenaire("TGR");
				if (param == null || !BooleanUtils.isTrue(param.getActiveRetryKo())) {
					System.out.println("❌ Retry TGR désactivée");
					return;
				}
				System.out.println("🔁 Checking TGR rep KO at: " + Utils.dateToStringTime(new Date()));
				Pageable pageable = PageRequest.of(0, 500);
				Page<LotSituationTgr> koRep = tgrRepo.findIntegrationKO(pageable);

				if (koRep != null && CollectionUtils.isNotEmpty(koRep.getContent())) {
					for (LotSituationTgr lot : koRep.getContent()) {
						System.err.println("🔄 ReIntegration TGR de " + lot.getId());
						tgrExec.traiterDemandes(lot.getId());
					}
				} else {
					System.out.println("ℹ️ No TGR KO lots to re-integrate.");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	}

	public void checkMiIntegKO() {
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		Runnable task = () -> {

			try {
				ParametrageCollection paramCnss = parametrageRepo.findByPartenaire("MI");
				if (paramCnss == null || BooleanUtils.isNotTrue(paramCnss.getActiveRetryKo())) {
					System.out.println("Retry  MI désactivée ");
					return;
				}
				System.out.println("checking MI rep ko   executed : at: " + Utils.dateToStringTime(new Date()));
				Pageable pageable = PageRequest.of(0, 500);
				Page<LotSituationMi> koRep = miRepo.findIntegrationKO(pageable);
				if (koRep != null && CollectionUtils.isNotEmpty(koRep.getContent())) {
					for (LotSituationMi lot : koRep.getContent()) {
						System.err.println("ReIntegration MI de " + lot.getId());
						miExec.traiterDemandes(lot.getId());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
		scheduler.scheduleAtFixedRate(task, 0, 1, TimeUnit.MINUTES);
	}

	public void checkSanteIntegKo() {}

	public Runnable checkMassarIntegKO() {
		return () -> {
			System.out.println("▶️ Starting MASSAR RETRY Task at: " + Utils.dateToStringTime(new Date()));
			try {
				while (!Thread.currentThread().isInterrupted()) {
					ParametrageCollection param = parametrageRepo.findByPartenaire("MASSAR");
					if (param == null || !BooleanUtils.isTrue(param.getActiveRetryKo())) {
						System.out.println("❌ MASSAR retry is disabled.");
						break;
					}

					Pageable pageable = PageRequest.of(0, 500);
					Page<LotSituationMassar> koRep = massarRepo.findIntegrationKO(pageable);
					if (koRep != null && CollectionUtils.isNotEmpty(koRep.getContent())) {
						for (LotSituationMassar lot : koRep.getContent()) {
							if (Thread.currentThread().isInterrupted()) {
								System.err.println("🛑 Interrupted during retry. Exiting MASSAR retry.");
								return;
							}
							System.out.println("🔁 Re-integrating MASSAR lot ID: " + lot.getId());
							massarExec.traiterDemandesOneByOne(lot.getId());
						}
					}

					try {
						Thread.sleep(1000);
					} catch (InterruptedException ex) {
						Thread.currentThread().interrupt();
						System.err.println("🛑 Interrupted: Exiting MASSAR retry loop.");
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				System.out.println("✅ Finished MASSAR RETRY Task.");
			}
		};
	}




	public void checkMassar2025IntegKO() {
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		Runnable task = () -> {

			try {
				ParametrageCollection paramCnss = parametrageRepo.findByPartenaire("MASSAR");
				if (paramCnss == null || BooleanUtils.isNotTrue(paramCnss.getActiveRetryKo())) {
					System.out.println("Retry  MASSAR désactivée ");
					return;
				}
				System.out.println("checking massar rep ko   executed : at: " + Utils.dateToStringTime(new Date()));
				Pageable pageable = PageRequest.of(0, 500);
				Page<LotSituationMassar2025> koRep = massar2025Repo.findIntegrationKO(pageable);
				if (koRep != null && CollectionUtils.isNotEmpty(koRep.getContent())) {
					for (LotSituationMassar2025 lot : koRep.getContent()) {
						System.err.println("ReIntegration MASSAR de " + lot.getId());
						massar2025Executor.traiterDemandes2025OneByOne(lot.getId());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
		scheduler.scheduleAtFixedRate(task, 0, 1, TimeUnit.MINUTES);
	}


	@org.springframework.transaction.annotation.Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public Set<BenefNotificationTgrDto> getAllCinAndStatut(int currentPage) throws SQLException {

		Set<BenefNotificationTgrDto> data = repoEseag.getAllCinAndStatut(currentPage,1000);
		return data;
	}



	
	@Transactional
	public Runnable runNotifTgr() {
		return () -> {


//	LotNotificationTgrDto lot = new LotNotificationTgrDto();
//	LotNotifTgr lotNotif = new LotNotifTgr();


//	Set<BenefNotificationTgrDto> data = null;
//		try {
//			data = getAllCinAndStatut(currentPage);
//		
//			if (CollectionUtils.isEmpty(data)) {
//				System.err.println("Liste empty");
//				currentPage = 0;
//				notificationTgrSchedulerService.stopServiceCheck();
//				return;
//			} else {
//				System.err.println("================data not empty=======");
//				lotNotif.setDateCreation(new Date());
//				lotNotif.setEtatLot("CREATED");
//				LotNotifTgr lotNotifTgr = lotNotifTgrRepo.save(lotNotif);
//				currentPage++;
//				lot.setListeBeneficiaires(data);
//				LotNotifTGRRabbitDto lotRabbit = new LotNotifTGRRabbitDto(lot, LotNotifTgrDto.to(lotNotifTgr));
//
//				//send data to Rabbit
//				sendDataToRabbitAndUpdateFlag(lotRabbit );
//
//
//			}
//			
//		} catch (SQLException e) {
//			throw new RuntimeException(e);
//		}
	
	Set<BenefNotificationTgrDto> data = null;
	try {
	    data = getAllCinAndStatut(currentPage);

	    if (CollectionUtils.isEmpty(data)) {
	        System.err.println("Liste empty");
	        currentPage = 0;
	        notificationTgrSchedulerService.stopServiceCheck();
	        return;
	    } else {
	        System.err.println("================data not empty=======");
	        
	        currentPage++;

	        // Diviser les données en lots de 100
	        List<BenefNotificationTgrDto> dataList = new ArrayList<>(data);
	        int batchSize = 100;
	        int totalSize = dataList.size();
	        for (int i = 0; i < totalSize; i += batchSize) {
	        	LotNotificationTgrDto lot = new LotNotificationTgrDto();
	        	LotNotifTgr lotNotif = new LotNotifTgr();
	        	lotNotif.setDateCreation(new Date());
		        lotNotif.setEtatLot("CREATED");
		        LotNotifTgr lotNotifTgr = lotNotifTgrRepo.save(lotNotif);
	        	
	            List<BenefNotificationTgrDto> batchList = dataList.subList(i, Math.min(totalSize, i + batchSize));
	            Set<BenefNotificationTgrDto> batchSet = new HashSet<>(batchList);

	            // Créer et envoyer le lot à Rabbit
	            lot.setListeBeneficiaires(batchSet);
	            LotNotifTGRRabbitDto lotRabbit = new LotNotifTGRRabbitDto(lot, LotNotifTgrDto.to(lotNotifTgr));
	            sendDataToRabbitAndUpdateFlag(lotRabbit);
	        }
	    }
	} catch (SQLException e) {
	    throw new RuntimeException(e);
	}

	
	
	};
}
	
public void sendDataToRabbitAndUpdateFlag(LotNotifTGRRabbitDto lot) throws SQLException {
	senderTGR.send(lot, "TGR");
//update flag
	List<Long> idNotifCollected = lot.getLot().getListeBeneficiaires().stream().map(t -> Long.valueOf(((BenefNotificationTgrDto) t).getId())).collect(Collectors.toList());	
	repoEseag.flagNotifTGRCollected(idNotifCollected, 0);
}

	public Runnable runDemandesRsu() {
		return () -> {
			try {
				if (currentBatch.isEmpty() || currentBatchIndex >= currentBatch.size()) {
					fetchUnflaggedCandidat();
				}

				if (!currentBatch.isEmpty()) {
					LotDemandeRsuDto tempLot = updateLotDemandeRsuDto();
					if (tempLot != null && !tempLot.getDemandes().isEmpty()) {
						try {
							LotDemandeRsu lot = lotDemandeRsuRepository.save(new LotDemandeRsu());

							Set<Long> idcs = tempLot.getDemandes().stream()
									.map(d -> Long.valueOf(d.getIdcs()))
									.collect(Collectors.toSet());
							Set<CollectedDemandesRsu> existingDemandes = collectedDemandesRsuRepository.findAllByIdcsIn(idcs);
							Set<Long> ids = existingDemandes.stream().map(CollectedDemandesRsu::getId).collect(Collectors.toSet());
							if (!existingDemandes.isEmpty()) {
								collectedDemandesRsuRepository.updateFlagForIdcs(ids, true);
							}

							lotSender.sendDemandesRsuToQueue(
									new LotRabitDemandesRsuDto(lot.getId().toString(), tempLot, "RSU"),
									"DEMANDES_RSU"
							);

							lotsSentCount.incrementAndGet();
							totalDemandesSentCount.addAndGet(tempLot.getDemandes().size());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	}

	public Runnable reportRsuStatus() {
		return () -> {
			int lotsSentInPeriod = lotsSentCount.getAndSet(0);
			int demandesSentInPeriod = totalDemandesSentCount.getAndSet(0);
			System.out.println("\u001B[32m" +
					"Lot sent in 15 seconds: " + lotsSentInPeriod +
					", Total demandes sent: " + demandesSentInPeriod +
					"\u001B[0m - " + Utils.dateToStringTime(new Date()));
		};
	}

	public LotDemandeRsuDto updateLotDemandeRsuDto() {
		final int CHUNK_SIZE = 60;
		int end = Math.min(currentBatchIndex + CHUNK_SIZE, currentBatch.size());
		List<CollectedDemandesRsu> chunk = currentBatch.subList(currentBatchIndex, end);
		currentBatchIndex += CHUNK_SIZE;
		return LotDemandeRsuDto.convertToDto(chunk);
	}


	public void fetchUnflaggedCandidat() {
		Pageable pageable = PageRequest.of(0, 1800);
		Page<CollectedDemandesRsu> page = collectedDemandesRsuRepository.findDemandesRsuEsgeafByInstanceId(pageable);
		currentBatch = page.getContent();
		currentBatchIndex = 0;
	}
}
