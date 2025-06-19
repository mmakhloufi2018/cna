package ma.cdgp.af.service;

import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import javax.sql.rowset.serial.SerialException;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.lang.Assert;
import ma.cdgp.af.dto.af.DecesInfos;
import ma.cdgp.af.dto.af.cmr.LotSituationPrsCmrDto;
import ma.cdgp.af.entity.RetourTracabilite;
import ma.cdgp.af.entity.TraceEnvoiRetour;
import ma.cdgp.af.entity.cmr.LotSituationCmr;
import ma.cdgp.af.entity.mi.DetailsLotSituationMi;
import ma.cdgp.af.entity.mi.LotSituationMi;
import ma.cdgp.af.oauth2.JwtResponse;
import ma.cdgp.af.oauth2.LoginRequest;
import ma.cdgp.af.repository.CandidatsRepository;
import ma.cdgp.af.repository.LotSituationMiRepo;
import ma.cdgp.af.repository.RetourTracabiliteRepository;
import ma.cdgp.af.repository.TraceEnvoiRetourRepository;

@Service("MI")
public class RequestMiExecutor extends ExecutorSituationPersonne {

	@Autowired
	LotSituationMiRepo lotSituationPersonneRepo;

	@Autowired
	RetourTracabiliteRepository retourHistoRepo;

	@Autowired
	ApplicationContext ctx;
	@Autowired
	TraceEnvoiRetourRepository traceEnvoiRetourRepository;

	@Autowired
	CandidatsRepository repoDeces;

	public CompletableFuture<ResponseEntity<?>> traiterDemandes(Long idRequeteRSU) throws Exception {
		Assert.notNull(idRequeteRSU);
		LotSituationMi savedRR = lotSituationPersonneRepo.findById(idRequeteRSU).orElse(null);
		Assert.notNull(savedRR, "Lot not found");
		savedRR.setLocked(true);
		lotSituationPersonneRepo.save(savedRR);

		try {
			savedRR.setPersonnes(new HashSet<>());
			Date dateCreationReq = Calendar.getInstance().getTime();
			if (CollectionUtils.isNotEmpty(savedRR.getDemandes())) {
				List<String> cines = savedRR.getDemandes().stream().filter(t -> t.getCin() != null).map(c -> c.getCin())
						.collect(Collectors.toList());
				List<DecesInfos> decesFounds = repoDeces.fetchDecesMi(cines);
				if(decesFounds == null) {
					decesFounds = new ArrayList<>();
				}
				if (CollectionUtils.isNotEmpty(decesFounds)) {
					Set<DetailsLotSituationMi> founds = decesFounds.stream().map(t -> {
						DetailsLotSituationMi det = new DetailsLotSituationMi();
						det.setCin(t.getCnie());
						det.setDateDeces(t.getDateDeces());
						det.setDateNaissance(t.getDateNaissance());
						det.setEtatCivil(t.getEtatCivil());
						det.setNom(t.getNom());
						det.setPrenom(t.getPrenom());
						det.setMessageRetour("OK");
						det.setCodeRetour("200");
						det.setSituation("1");
						return det;
					}).collect(Collectors.toSet());
					savedRR.getPersonnes().addAll(founds);
				}
				// complete not found cines;
				for (String cinDmd : cines) {
					if (decesFounds.stream().filter(t -> t.getCnie().equalsIgnoreCase(cinDmd)).count() == 0) {
						DetailsLotSituationMi det = new DetailsLotSituationMi();
						det.setCin(cinDmd);
						det.setMessageRetour("OK");
						det.setCodeRetour("200");
						det.setSituation("0");
						savedRR.getPersonnes().add(det);
					}
				}
			}

			savedRR.setCodeRetour("200");
			savedRR.setDateReponse(new Date());
			savedRR.setEtatLot("OK");
			savedRR.setMessageRetour("OK");
			savedRR.setters();
			LotSituationMi savedRetour = lotSituationPersonneRepo.save(savedRR);
			System.err.println("Retour OK ID : " + savedRetour.getId());
			addTracabiliteRetour(savedRetour, savedRR.getEtatLot(), savedRR.getMessageRetour());
//			savedRR.setLocked(false);
//			lotSituationPersonneRepo.save(savedRR);
			return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(""));
		} catch (Exception e) {
			e.printStackTrace();
			savedRR.setCodeRetour("500");
			savedRR.setEtatLot("KO");
			savedRR.setMessageRetour(e.getMessage());
			savedRR.setDateReponse(null);
			savedRR.setters();
			savedRR.setLocked(false);
			lotSituationPersonneRepo.save(savedRR);
			String msg = e.getMessage().length() > 1000 ? e.getMessage().substring(0, 1000) : e.getMessage();
			addTracabiliteRetour(savedRR, "KO", msg);
			return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(""));
		}
	}

	public void addTracabiliteRetour(LotSituationMi r, String status, String msg) {
		retourHistoRepo.save(new RetourTracabilite(null, r, status, msg, new Date()));

	}

	private void saveTraceEnvoiRetour(Date dateCreation, LotSituationPrsCmrDto req, LotSituationPrsCmrDto rep,
			Long idLot) {
		TraceEnvoiRetour traceEnvoi = new TraceEnvoiRetour(null, getclob(req.toString()), null, dateCreation, null);
		traceEnvoi.setIdRetour(idLot);
		traceEnvoi.setDateRsu(Calendar.getInstance().getTime());
		traceEnvoi.setRetourRsu(rep != null ? getclob(rep.toString()) : null);
		traceEnvoiRetourRepository.save(traceEnvoi);
	}

	private Clob getclob(String val) {
		try {
			return new javax.sql.rowset.serial.SerialClob(val.toCharArray());
		} catch (SerialException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void executeWork(Long idLot, String type) {
		if (type == null || !type.equalsIgnoreCase("MI")) {
			return;
		}
		try {
			traiterDemandes(idLot);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
