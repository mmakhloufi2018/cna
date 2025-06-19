package ma.cdgp.af.service;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import ma.cdgp.af.client.NotificationTgrClient;
import ma.cdgp.af.dto.af.tgr.AcquittementBenefNotificationTgrDto;
import ma.cdgp.af.dto.af.tgr.LotNotificationTgrDto;
import ma.cdgp.af.entity.RetourTracabiliteNotification;
import ma.cdgp.af.entity.TraceEnvoiRetourNotification;
import ma.cdgp.af.entity.tgr.DetailsNotifTgr;
import ma.cdgp.af.entity.tgr.LotNotifTgr;
import ma.cdgp.af.entity.tgr.NotificationPartenaire;
import ma.cdgp.af.entity.tgr.RetourBeneficiaresAquittementTgr;
import ma.cdgp.af.esgaf.EsgeafRepository;
import ma.cdgp.af.oauth2.JwtResponse;
import ma.cdgp.af.oauth2.JwtUtils;
import ma.cdgp.af.oauth2.LoginRequestTgr;
import ma.cdgp.af.repository.*;
import ma.cdgp.af.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.sql.rowset.serial.SerialException;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ServiceTgrSender {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    NotificationTgrClient notificationTgrClient;

    @Autowired
    EsgeafRepository repoEseag;

    @Autowired
    LotNotifTgrRepo lotNotifTgrRepo;

    @Autowired
    NotificationPartenaireRepository notificationPartenaireRepo;

    @Autowired
    TraceEnvoiRetourNotificationRepository traceEnvoiRetourNotificationRepository;

    @Autowired
    RetourTracabiliteNotificationRepository retourTracabiliteNotificationRepository;

    @Value("${tgr.fa.cle.pass}")
    private String clePass;

    @Value("${tgr.fa.user.pass}")
    private String userAuth;

    @Autowired
    DetailsNotifTgrRepository detailsNotifTgrRepository;

    @Autowired
    RetourBeneficiaresAquittementRepository retourBeneficiaresAquittementRepository;

    public void sendNotifToPartenaire(LotNotificationTgrDto lot,LotNotifTgr lotNotifTgr) throws SQLException {

    	System.out.println("=====start sending data to tgr =====");

        System.out.println("sending to tgr");

        LoginRequestTgr loginRequestTgr = new LoginRequestTgr();
        String dateLot = Utils.dateToStringddMMyyyy(new Date());
        String idLot = String.valueOf(Calendar.getInstance().getTimeInMillis());
        lot.setDateLot(dateLot);
        lot.setIdLot(Long.valueOf(idLot));
        lotNotifTgr.updateFromDto(lot);
        loginRequestTgr.setUserName(userAuth);
        loginRequestTgr.setPassword(clePass);

    	System.out.println("=====generate token =====");


        System.out.println("auth tgr");

        JwtResponse jwtauthOk = notificationTgrClient.authTgr(loginRequestTgr);
        if(jwtauthOk != null && jwtauthOk.getToken() != null && jwtauthOk.getToken().getAccessToken() != null) {
            System.out.println("=====connected : " + jwtauthOk.getToken().getAccessToken());
        Date dateCreationReq = Calendar.getInstance().getTime();
        lotNotifTgr.setters();
        lotNotifTgr.setEtatLot("SENT_TO_PARTENAIRE");
        LotNotifTgr saved = lotNotifTgrRepo.save(lotNotifTgr);
        saved.setDateCreation(new Date());

         System.out.println("====sending data to tgr");

        System.out.println("sending to tgr");

        ResponseEntity<AcquittementBenefNotificationTgrDto> acq = notificationTgrClient.sendLotBenefPartenaire(lot,"Bearer "+jwtauthOk.getToken().getAccessToken());
        saveTraceEnvoiRetour(dateCreationReq, lot, acq.getBody(), saved.getIdLot(), "TGR");
        if(acq.getBody() != null) {
            System.out.println("acq from tgr");
            printResponseEntity(acq);
            System.out.println("data returned");
            saved.setPartenaire("TGR");
            saved.setDateReponse(new Date());
            saved.setEtatLot("REP_OK");
            saved.setCodeRetour("200");
            saved.setMessageRetour("OK");
            DetailsNotifTgr detailsNotifTgr = new DetailsNotifTgr();
            detailsNotifTgr.setLot(saved);
            detailsNotifTgr.setDateLot(acq.getBody().getDateLot());
            detailsNotifTgr.setCodeMotif(acq.getBody().getCodeMotif());
            detailsNotifTgr.setLibeMotif(acq.getBody().getLibeMotif());
            detailsNotifTgr.setNombreEnregistrement(acq.getBody().getNombreEnregistrement());
            Set<RetourBeneficiaresAquittementTgr> beneficiaresAquittementTgrSet = new HashSet<>();
                RetourBeneficiaresAquittementTgr retourBeneficiaresAquittementTgr = new RetourBeneficiaresAquittementTgr();
            acq.getBody().getBeneficiareNonEnregistrer().stream().map(in -> {
                retourBeneficiaresAquittementTgr.setCin(in.getCin());
                retourBeneficiaresAquittementTgr.setLibeMotif(in.getLibeMotif());
                retourBeneficiaresAquittementTgr.setCodeMotif(in.getCodeMotif());
                retourBeneficiaresAquittementTgr.setDetailsNotifTgr(detailsNotifTgr);
                beneficiaresAquittementTgrSet.add(retourBeneficiaresAquittementTgr);
                return retourBeneficiaresAquittementTgr;
            }).collect(Collectors.toSet());
            detailsNotifTgr.setBeneficiareNonEnregistrer(beneficiaresAquittementTgrSet);
            DetailsNotifTgr detailsaved =  detailsNotifTgrRepository.save(detailsNotifTgr);
            
            lotNotifTgrRepo.save(saved);
            addTracabiliteRetour(saved, acq.getBody().getLibeMotif(), acq.getBody().getCodeMotif());
        }else {
            System.err.printf("NO RESPONSE");
            addTracabiliteRetour(saved, acq.getBody().getLibeMotif(), acq.getBody().getCodeMotif());
        }
        } else System.err.printf("AUTH KO");
    }




    public void addTracabiliteRetour(LotNotifTgr r, String status, String msg) {
        retourTracabiliteNotificationRepository.save(new RetourTracabiliteNotification(null, r, status, msg, new Date()));

    }

    private void saveTraceEnvoiRetour(Date dateCreation, LotNotificationTgrDto req, AcquittementBenefNotificationTgrDto rep, Long idLot, String partenaire) {
        TraceEnvoiRetourNotification traceEnvoi = new TraceEnvoiRetourNotification(null, getclob(req.toString()), null,
                dateCreation, null, partenaire);
        traceEnvoi.setIdRetour(idLot);
        traceEnvoi.setDatePartenaire(Calendar.getInstance().getTime());
        traceEnvoi.setRetourPartenaire(rep != null ? getclob(rep.toString()) : null);
        traceEnvoiRetourNotificationRepository.save(traceEnvoi);
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


    public void printResponseEntity(ResponseEntity<AcquittementBenefNotificationTgrDto> acq) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            String json = mapper.writeValueAsString(acq.getBody());
            System.out.println(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
