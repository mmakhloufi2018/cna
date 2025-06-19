package ma.cdgp.af.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import feign.FeignException;
import io.jsonwebtoken.lang.Assert;
import ma.cdgp.af.client.AffiliationClient;
import ma.cdgp.af.client.MassarClient;
import ma.cdgp.af.dto.af.ResponseMassarDto;
import ma.cdgp.af.dto.af.massar.DetailsLotSituationPrsMassarDto;
import ma.cdgp.af.dto.af.massar.LotSituationPrsMassarDto;
import ma.cdgp.af.dto.af.massar2025.DetailsLotSituationPrsMassar2025Dto;
import ma.cdgp.af.dto.af.massar2025.LotSituationPrsMassar2025Dto;
import ma.cdgp.af.entity.RetourTracabilite;
import ma.cdgp.af.entity.TraceEnvoiRetour;
import ma.cdgp.af.entity.massar.DemandeSituationMassar;
import ma.cdgp.af.entity.massar.LotSituationMassar;
import ma.cdgp.af.entity.massar2025.LotSituationMassar2025;
import ma.cdgp.af.oauth2.JwtUtils;
import ma.cdgp.af.repository.LotSituationMassar2025Repo;
import ma.cdgp.af.repository.LotSituationMassarRepo;
import ma.cdgp.af.repository.RetourTracabiliteRepository;
import ma.cdgp.af.repository.TraceEnvoiRetourRepository;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.sql.rowset.serial.SerialException;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service("MASSAR")
public class RequestMassarExecutor extends ExecutorSituationPersonne {

	@Autowired
	LotSituationMassarRepo lotSituationPersonneRepo;

	@Autowired
	LotSituationMassar2025Repo lotSituationMassar2025Repo;

	@Autowired
	RetourTracabiliteRepository retourHistoRepo;

	@Autowired
	ApplicationContext ctx;
	@Autowired
	TraceEnvoiRetourRepository traceEnvoiRetourRepository;

	@Autowired
	AffiliationClient service;

	@Value("${massar.sec.xClientId}")
	private String clientId;

	@Value("${massar.sec.xRequestAppName}")
	private String requestApplicationName;

	@Value("${massar.connection}")
	private String connection;

	@Value("${massar.acceptEncoding}")
	private String acceptEncoding;

	@Value("${massar.accept}")
	private String accept;

	@Autowired
	private JwtUtils jwtUtils;
	@Autowired
	MassarClient massarClient;

	public CompletableFuture<ResponseEntity<?>> traiterDemandes(Long idRequeteRSU) throws Exception {
		Assert.notNull(idRequeteRSU);
		LotSituationMassar savedRR = lotSituationPersonneRepo.findById(idRequeteRSU).orElse(null);
		Assert.notNull(savedRR, "Lot not found");
		savedRR.setLocked(true);
		lotSituationPersonneRepo.save(savedRR);

		try {
			Date dateCreationReq = Calendar.getInstance().getTime();
			LotSituationPrsMassarDto reqIn = LotSituationPrsMassarDto.fromEntity(savedRR);
			String token = jwtUtils.tokenAcess();
			String uid = jwtUtils.generateUniqueId();
			LotSituationPrsMassarDto response = null;

			try {
				response = massarClient.askMassar(reqIn, clientId, uid, requestApplicationName, "Bearer " + token);
			} catch (FeignException e) {
				if (e.status() == 404) {
					System.err.println("404 Not Found error for Massar service: " + e.getMessage());

					response = new LotSituationPrsMassarDto();
				} else {
					throw e;
				}
			}

			saveTraceEnvoiRetour(dateCreationReq, reqIn, response, idRequeteRSU);

			if (response == null || CollectionUtils.isEmpty(response.getResult())) {
				System.err.println("Response from Massar service is empty or null.");
				savedRR.setCodeRetour("500");
				savedRR.setEtatLot("KO");
				savedRR.setMessageRetour("EMPTY_PERSONNES");
			} else {
				LotSituationMassar retour = LotSituationPrsMassarDto.fromRetourDto(response, savedRR);
				retour.setCodeRetour("200");
				retour.setDateReponse(new Date());
				retour.setEtatLot("OK");
				retour.setMessageRetour("OK");
				retour.setters();
				retour.setLocked(false);
				LotSituationMassar savedRetour = lotSituationPersonneRepo.save(retour);
				System.err.println("Retour OK ID : " + savedRetour.getId());
				addTracabiliteRetour(savedRetour, retour.getEtatLot(), retour.getMessageRetour());
				return CompletableFuture.completedFuture(ResponseEntity.ok("Success"));
			}

			savedRR.setLocked(false);
			lotSituationPersonneRepo.save(savedRR);
			return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.NO_CONTENT).body("No valid data to process"));
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



	public CompletableFuture<ResponseEntity<?>> traiterDemandesOneByOne(Long idRequeteRSU) throws Exception {
		Assert.notNull(idRequeteRSU);
		LotSituationMassar savedRR = lotSituationPersonneRepo.findById(idRequeteRSU).orElse(null);
		Assert.notNull(savedRR, "Lot not found");
		savedRR.setLocked(true);
		lotSituationPersonneRepo.save(savedRR);

		boolean allFailed = true;

		try {
			Date dateCreationReq = Calendar.getInstance().getTime();
			LotSituationPrsMassarDto reqIn = LotSituationPrsMassarDto.fromEntity(savedRR);
			String token = jwtUtils.tokenAcess();
			System.err.println(token);
			String uid = jwtUtils.generateUniqueId();
			LotSituationPrsMassarDto response = new LotSituationPrsMassarDto();
			response.setResult(new HashSet<>());
			ResponseMassarDto dataOne = null;

			for (String code : reqIn.getCodeEleves()) {
				System.err.println("Checking code : " + code);
				try {
					dataOne = massarClient.getEleveDonnees(code, null, null, clientId, uid, requestApplicationName, "Bearer " + token);
				} catch (FeignException e) {
					if (e.status() == 404) {
						System.err.println("Student not found for code: " + code);
						continue;
					} else {
						throw e;
					}
				}

				if (dataOne != null && dataOne.getResult() != null) {
					allFailed = false;
					DetailsLotSituationPrsMassarDto det = new DetailsLotSituationPrsMassarDto();
					det.setCodeEleve(code);
					det.setCinMere(dataOne.getResult().getEleveData().getCinMere());
					det.setCinPere(dataOne.getResult().getEleveData().getCinPere());
					det.setCinTuteur(dataOne.getResult().getEleveData().getCinTuteur());
					det.setCodeEleve(dataOne.getResult().getEleveData().getCodeEleve());
					det.setCodeNiveau(dataOne.getResult().getEleveData().getCodeNiveau());
					det.setDateNaisEleve(dataOne.getResult().getEleveData().getDateNaisEleve());
					det.setNomEleveAr(dataOne.getResult().getEleveData().getNomEleveAr());
					det.setNomEleveFr(dataOne.getResult().getEleveData().getNomEleveFr());
					det.setPrenomEleveAr(dataOne.getResult().getEleveData().getPrenomEleveAr());
					det.setPrenomEleveFr(dataOne.getResult().getEleveData().getPrenomEleveFr());
					det.setSituationScolarisation(dataOne.getResult().getEleveData().getSituationScolarisation());
					det.setCinEleve(dataOne.getResult().getEleveData().getCinEleve());
					det.setGenre(dataOne.getResult().getEleveData().getGenre());
					det.setCodeNationalite(dataOne.getResult().getEleveData().getCodeNationalite());
					det.setEmail(dataOne.getResult().getEleveData().getEmail());
					det.setTypeCandidat(dataOne.getResult().getEleveData().getTypeCandidat());
					det.setCodeEtablissement(dataOne.getResult().getEleveData().getCodeEtablissement());
					det.setCodeProvince(dataOne.getResult().getEleveData().getCodeProvince());
					det.setLieuNaissance(dataOne.getResult().getEleveData().getLieuNaissance());
					det.setLastSituationScolarisation(dataOne.getResult().getEleveData().getLastSituationScolarisation());
					det.setMoyenneGenerale(String.valueOf(dataOne.getResult().getEleveData().getMoyenneGenerale()));
					det.setResultatFinAnnee(dataOne.getResult().getEleveData().getResultatFinAnnee());
					response.getResult().add(det);
				}
			}
			saveTraceEnvoiRetour(dateCreationReq, reqIn, response, idRequeteRSU);
//            Assert.notNull(response, "Reponse Massar Vide");
//            Assert.notEmpty(response.getResult(), "OUTPUT :Liste réponse vide");
			LotSituationMassar retour = LotSituationPrsMassarDto.fromRetourDto(response, savedRR);

			if (CollectionUtils.isEmpty(retour.getCodeEleves())) {
				retour.setCodeRetour("500");
				retour.setDateReponse(new Date());
				retour.setEtatLot("OK");
				retour.setMessageRetour("EMPTY_PERSONNES");
			} else {
				retour.setCodeRetour("200");
				retour.setDateReponse(new Date());
				retour.setEtatLot("OK");
				retour.setMessageRetour("OK");
			}

			retour.setters();
			retour.setLocked(false);
			LotSituationMassar savedRetour = lotSituationPersonneRepo.save(retour);
			System.err.println("Retour OK ID : " + savedRetour.getId());
			addTracabiliteRetour(savedRetour, retour.getEtatLot(), retour.getMessageRetour());


			HttpStatus status = allFailed ? HttpStatus.BAD_REQUEST : HttpStatus.OK;
			return CompletableFuture.completedFuture(ResponseEntity.status(status).body(""));
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







	public void addTracabiliteRetour(LotSituationMassar r, String status, String msg) {
		retourHistoRepo.save(new RetourTracabilite(null, r, status, msg, new Date()));

	}



	private void saveTraceEnvoiRetour(Date dateCreation, LotSituationPrsMassarDto req, LotSituationPrsMassarDto rep,
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
		if (type == null || !type.equalsIgnoreCase("MASSAR")) {
			return;
		}
		try {
			traiterDemandes(idLot);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
