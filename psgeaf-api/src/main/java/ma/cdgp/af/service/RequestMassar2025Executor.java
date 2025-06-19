package ma.cdgp.af.service;


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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.sql.rowset.serial.SerialException;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.concurrent.CompletableFuture;

@Service("MASSAR2025")
public class RequestMassar2025Executor extends ExecutorSituationPersonne{

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





    public CompletableFuture<ResponseEntity<?>> traiterDemandes2025(Long idRequeteRSU) throws Exception {
        Assert.notNull(idRequeteRSU);
        LotSituationMassar2025 savedRR = lotSituationMassar2025Repo.findById(idRequeteRSU).orElse(null);
        Assert.notNull(savedRR, "Lot not found");
        savedRR.setLocked(true);
        lotSituationMassar2025Repo.save(savedRR);

        try {
            Date dateCreationReq = Calendar.getInstance().getTime();
            LotSituationPrsMassar2025Dto reqIn = LotSituationPrsMassar2025Dto.fromEntity(savedRR);
            String token = jwtUtils.tokenAcess();
            String uid = jwtUtils.generateUniqueId();
            LotSituationPrsMassar2025Dto response = null;

            try {
                response = massarClient.askMassar2025(reqIn, clientId, uid, requestApplicationName, "Bearer " + token);
            } catch (FeignException e) {
                if (e.status() == 404) {
                    System.err.println("404 Not Found error for Massar2025 service: " + e.getMessage());

                    response = new LotSituationPrsMassar2025Dto();
                } else {
                    throw e;
                }
            }

            saveTraceEnvoiRetour2025(dateCreationReq, reqIn, response, idRequeteRSU);

            if (response == null || CollectionUtils.isEmpty(response.getResult())) {
                System.err.println("Response from Massar2025 service is empty or null.");
                savedRR.setCodeRetour("500");
                savedRR.setEtatLot("KO");
                savedRR.setMessageRetour("EMPTY_PERSONNES");
            } else {
                LotSituationMassar2025 retour = LotSituationPrsMassar2025Dto.fromRetourDto(response, savedRR);
                retour.setCodeRetour("200");
                retour.setDateReponse(new Date());
                retour.setEtatLot("OK");
                retour.setMessageRetour("OK");
                retour.setters();
                retour.setLocked(false);
                LotSituationMassar2025 savedRetour = lotSituationMassar2025Repo.save(retour);
                System.err.println("Retour OK ID : " + savedRetour.getId());
                addTracabiliteRetour2025(savedRetour, retour.getEtatLot(), retour.getMessageRetour());
                return CompletableFuture.completedFuture(ResponseEntity.ok("Success"));
            }

            savedRR.setLocked(false);
            lotSituationMassar2025Repo.save(savedRR);
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.NO_CONTENT).body("No valid data to process"));
        } catch (Exception e) {
            e.printStackTrace();
            savedRR.setCodeRetour("500");
            savedRR.setEtatLot("KO");
            savedRR.setMessageRetour(e.getMessage());
            savedRR.setDateReponse(null);
            savedRR.setters();
            savedRR.setLocked(false);
            lotSituationMassar2025Repo.save(savedRR);
            String msg = e.getMessage().length() > 1000 ? e.getMessage().substring(0, 1000) : e.getMessage();
            addTracabiliteRetour2025(savedRR, "KO", msg);
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(""));
        }
    }





    public CompletableFuture<ResponseEntity<?>> traiterDemandes2025OneByOne(Long idRequeteRSU) throws Exception {
        Assert.notNull(idRequeteRSU);
        LotSituationMassar2025 savedRR = lotSituationMassar2025Repo.findById(idRequeteRSU).orElse(null);
        Assert.notNull(savedRR, "Lot not found");
        savedRR.setLocked(true);
        lotSituationMassar2025Repo.save(savedRR);

        boolean allFailed = true;

        try {
            Date dateCreationReq = Calendar.getInstance().getTime();
            LotSituationPrsMassar2025Dto reqIn = LotSituationPrsMassar2025Dto.fromEntity(savedRR);
            String token = jwtUtils.tokenAcess();
            System.err.println(token);
            String uid = jwtUtils.generateUniqueId();
            LotSituationPrsMassar2025Dto response = new LotSituationPrsMassar2025Dto();
            response.setResult(new HashSet<>());
            ResponseMassarDto dataOne = null;

            for (String code : reqIn.getCodeEleves()) {
                System.err.println("Checking code : " + code);
                try {
                    dataOne = massarClient.getEleveDonnees(code, null, "2025", clientId, uid, requestApplicationName, "Bearer " + token);
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
                    DetailsLotSituationPrsMassar2025Dto det = new DetailsLotSituationPrsMassar2025Dto();
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

            saveTraceEnvoiRetour2025(dateCreationReq, reqIn, response, idRequeteRSU);
//            Assert.notNull(response, "Reponse Massar Vide");
//            Assert.notEmpty(response.getResult(), "OUTPUT :Liste réponse vide");
            LotSituationMassar2025 retour = LotSituationPrsMassar2025Dto.fromRetourDto(response, savedRR);

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
            LotSituationMassar2025 savedRetour = lotSituationMassar2025Repo.save(retour);
            System.err.println("Retour OK ID : " + savedRetour.getId());
            addTracabiliteRetour2025(savedRetour, retour.getEtatLot(), retour.getMessageRetour());


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
            lotSituationMassar2025Repo.save(savedRR);
            String msg = e.getMessage().length() > 1000 ? e.getMessage().substring(0, 1000) : e.getMessage();
            addTracabiliteRetour2025(savedRR, "KO", msg);
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(""));
        }
    }







    public void addTracabiliteRetour2025(LotSituationMassar2025 r, String status, String msg) {
        retourHistoRepo.save(new RetourTracabilite(null, r, status, msg, new Date()));

    }






    private void saveTraceEnvoiRetour2025(Date dateCreation, LotSituationPrsMassar2025Dto req, LotSituationPrsMassar2025Dto rep,
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
        if (type == null || !type.equalsIgnoreCase("MASSAR2025")) {
            return;
        }
        try {
            traiterDemandes2025(idLot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
