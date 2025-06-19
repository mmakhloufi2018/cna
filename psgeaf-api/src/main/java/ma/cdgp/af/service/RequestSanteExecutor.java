package ma.cdgp.af.service;

import java.sql.Clob;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

import javax.sql.rowset.serial.SerialException;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.lang.Assert;
import ma.cdgp.af.client.AffiliationClient;
import ma.cdgp.af.client.CmrClient;
import ma.cdgp.af.client.SanteClient;
import ma.cdgp.af.dto.af.cmr.LotSituationPrsCmrDto;
import ma.cdgp.af.dto.af.sante.LotSituationPrsSanteDto;
import ma.cdgp.af.entity.RetourTracabilite;
import ma.cdgp.af.entity.TraceEnvoiRetour;
import ma.cdgp.af.entity.sante.LotSituationSante;
import ma.cdgp.af.oauth2.JwtResponse;
import ma.cdgp.af.oauth2.JwtUtils;
import ma.cdgp.af.oauth2.LoginRequest;
import ma.cdgp.af.repository.LotSituationSanteRepo;
import ma.cdgp.af.repository.RetourTracabiliteRepository;
import ma.cdgp.af.repository.TraceEnvoiRetourRepository;

@Service("SANTE")
public class RequestSanteExecutor extends ExecutorSituationPersonne {

	@Autowired
	LotSituationSanteRepo lotSituationPersonneRepo;

	@Autowired
	RetourTracabiliteRepository retourHistoRepo;

	@Autowired
	ApplicationContext ctx;
	@Autowired
	TraceEnvoiRetourRepository traceEnvoiRetourRepository;

	@Autowired
	AffiliationClient service;

	@Value("${rsu.sec.clientId}")
	private String clientId;

	@Value("${sante.fa.cle.pass}")
	private String clePasswordSante;

	@Value("${sante.fa.user.pass}")
	private String userAuthSante;
	@Autowired
	private JwtUtils jwtUtils;
	@Autowired
	SanteClient santeClient;

	public CompletableFuture<ResponseEntity<?>> traiterDemandes(Long idRequeteRSU) throws Exception {
		Assert.notNull(idRequeteRSU);
		LotSituationSante savedRR = lotSituationPersonneRepo.findById(idRequeteRSU).orElse(null);
		Assert.notNull(savedRR, "Lot not found");
		savedRR.setLocked(true);
		lotSituationPersonneRepo.save(savedRR);
		try {
//			LoginRequest logingToRsu = new LoginRequest();
//			String password = jwtUtils.getPassword(clePasswordSante);
//			logingToRsu.setPassword(password);
//			logingToRsu.setUserName(userAuthSante);
//			JwtResponse authJok = santeClient.authSante(logingToRsu);
//			Assert.notNull(authJok, "Authentification CMR not OK");
			Date dateCreationReq = Calendar.getInstance().getTime();
			LotSituationPrsSanteDto reqIn = LotSituationPrsSanteDto.fromEntity(savedRR);
			reqIn.setKey(clePasswordSante);
//			Assert.notEmpty(reqIn.getPersonnes(), "INPUT: Liste personnes vide");
			LotSituationPrsSanteDto response = santeClient.askSante(reqIn);

			saveTraceEnvoiRetour(dateCreationReq, reqIn, response, idRequeteRSU);
//			Assert.notNull(response, "Reponse Sante Vide");
//			Assert.notEmpty(response.getPersonnes(), "OUTPUT: Liste personnes vide");
			LotSituationSante retour = LotSituationPrsSanteDto.fromRetourDto(response, savedRR);
//			if (CollectionUtils.isEmpty(retour.getPersonnes())) {
//				retour.setCodeRetour("500");
//				retour.setDateReponse(new Date());
//				retour.setEtatLot("KO");
//				retour.setMessageRetour("EMPTY_PERSONNES");
//			} else if (retour.getPersonnes().size() != savedRR.getDemandes().size()) {
//				retour.setCodeRetour("500");
//				retour.setDateReponse(new Date());
//				retour.setEtatLot("KO");
//				retour.setMessageRetour("SOME_PERSONNES_KO");
//			} else {
				retour.setCodeRetour("200");
				retour.setDateReponse(new Date());
				retour.setEtatLot("OK");
				retour.setMessageRetour("OK");
//			}
			retour.setters();
			retour.setLocked(false);
			LotSituationSante savedRetour = lotSituationPersonneRepo.save(retour);
			System.err.println("Retour OK ID : " + savedRetour.getId());
			addTracabiliteRetour(savedRetour, retour.getEtatLot(), retour.getMessageRetour());
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

	public void addTracabiliteRetour(LotSituationSante r, String status, String msg) {
		retourHistoRepo.save(new RetourTracabilite(null, r, status, msg, new Date()));

	}

	private void saveTraceEnvoiRetour(Date dateCreation, LotSituationPrsSanteDto req, LotSituationPrsSanteDto rep, Long idLot) {
		TraceEnvoiRetour traceEnvoi = new TraceEnvoiRetour(null, getclob(req.toString()), null,
				dateCreation, null);
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
		 if(type == null || !type.equalsIgnoreCase("SANTE")) {
			 return;
		 }
		try {
//			traiterDemandes(idLot);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
