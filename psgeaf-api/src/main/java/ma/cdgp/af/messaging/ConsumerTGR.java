package ma.cdgp.af.messaging;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.sql.rowset.serial.SerialException;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JsonParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import ma.cdgp.af.client.NotificationTgrClient;
import ma.cdgp.af.dto.af.tgr.AcquittementBenefNotificationTgrDto;
import ma.cdgp.af.dto.af.tgr.BenefNotificationTgrDto;
import ma.cdgp.af.dto.af.tgr.LotNotifTgrDto;
import ma.cdgp.af.dto.af.tgr.LotNotificationTgrDto;
import ma.cdgp.af.entity.RetourTracabiliteNotification;
import ma.cdgp.af.entity.TraceEnvoiRetourNotification;
import ma.cdgp.af.entity.tgr.DetailsNotifTgr;
import ma.cdgp.af.entity.tgr.LotNotifTgr;
import ma.cdgp.af.entity.tgr.RetourBeneficiaresAquittementTgr;
import ma.cdgp.af.esgaf.EsgeafRepository;
import ma.cdgp.af.oauth2.JwtResponse;
import ma.cdgp.af.oauth2.JwtUtils;
import ma.cdgp.af.oauth2.LoginRequestTgr;
import ma.cdgp.af.repository.DetailsNotifTgrRepository;
import ma.cdgp.af.repository.LotNotifTgrRepo;
import ma.cdgp.af.repository.NotificationPartenaireRepository;
import ma.cdgp.af.repository.RetourBeneficiaresAquittementRepository;
import ma.cdgp.af.repository.RetourTracabiliteNotificationRepository;
import ma.cdgp.af.repository.TraceEnvoiRetourNotificationRepository;
import ma.cdgp.af.utils.Utils;

@Component
public class ConsumerTGR {

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


    @RabbitListener(queues = "${spring.rabbitmq.queue.notification.tgr}", concurrency = "2")
	public void receivedMsg(Message message)
			throws JsonParseException, JsonMappingException, IOException, SQLException {
		System.out.println("-------- Massage recieved : " + new Date());
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

		mapper.setSerializationInclusion(Include.NON_NULL);
		mapper.setSerializationInclusion(Include.NON_EMPTY);
		Map<String, Object> map = message.getMessageProperties().getHeaders();

		String type = (String) map.get("type");
		String msg = new String(message.getBody(), StandardCharsets.UTF_8);
		
		
		LotNotifTGRRabbitDto lotRabbitTGR = mapper.readValue(msg, new TypeReference<LotNotifTGRRabbitDto>() {
		});
		
    	System.out.println("=====start sending data to tgr =====");
        LoginRequestTgr loginRequestTgr = new LoginRequestTgr();
        String dateLot = Utils.dateToStringddMMyyyy(new Date());
        String idLot = String.valueOf(Calendar.getInstance().getTimeInMillis());
        lotRabbitTGR.getLot().setDateLot(dateLot);
        lotRabbitTGR.getLot().setIdLot(Long.valueOf(idLot));
       
        LotNotifTgr lotNotifTgr = LotNotifTgrDto.from(lotRabbitTGR.getLotNotifTgr());
   
        lotNotifTgr.updateFromDto(lotRabbitTGR.getLot());
        loginRequestTgr.setUserName(userAuth);
        loginRequestTgr.setPassword(clePass);
    	System.out.println("=====generate token =====");

        JwtResponse jwtauthOk = notificationTgrClient.authTgr(loginRequestTgr);
        if(jwtauthOk != null && jwtauthOk.getToken() != null && jwtauthOk.getToken().getAccessToken() != null) {
        System.out.println("=====connected : " + jwtauthOk.getToken().getAccessToken());
        Date dateCreationReq = Calendar.getInstance().getTime();
        lotNotifTgr.setters();
        lotNotifTgr.setEtatLot("SENT_TO_PARTENAIRE");

        LotNotifTgr saved = lotNotifTgrRepo.save(lotNotifTgr);
        saved.setDateCreation(new Date());
        System.out.println("====sending data to tgr"+new Date());
        ResponseEntity<AcquittementBenefNotificationTgrDto> acq = notificationTgrClient.sendLotBenefPartenaire(lotRabbitTGR.getLot(),"Bearer "+jwtauthOk.getToken().getAccessToken());
        System.out.println("====data recieved from tgr"+new Date());
        saveTraceEnvoiRetour(dateCreationReq, lotRabbitTGR.getLot(), acq.getBody(), saved.getIdLot(), "TGR");
        //System.out.println("====data traced "+new Date());
        if(acq.getBody() != null) {
            //printResponseEntity(acq);
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
            detailsNotifTgr.setLibeMotif((acq.getBody().getLibeMotif() != null && !acq.getBody().getLibeMotif().isEmpty()) ? acq.getBody().getLibeMotif() : "VIDE");
            detailsNotifTgr.setNombreEnregistrement(acq.getBody().getNombreEnregistrement());
            Set<RetourBeneficiaresAquittementTgr> beneficiaresAquittementTgrSet = new HashSet<>();
                RetourBeneficiaresAquittementTgr retourBeneficiaresAquittementTgr = new RetourBeneficiaresAquittementTgr();
          
            if(acq.getBody() != null && !CollectionUtils.isEmpty(acq.getBody().getBeneficiareNonEnregistrer())) {
                acq.getBody().getBeneficiareNonEnregistrer().stream().map(in -> {
                    retourBeneficiaresAquittementTgr.setCin(in.getCin());
                    retourBeneficiaresAquittementTgr.setLibeMotif(in.getLibeMotif());
                    retourBeneficiaresAquittementTgr.setCodeMotif(in.getCodeMotif());
                    retourBeneficiaresAquittementTgr.setDetailsNotifTgr(detailsNotifTgr);
                    beneficiaresAquittementTgrSet.add(retourBeneficiaresAquittementTgr);
                    return retourBeneficiaresAquittementTgr;
                }).collect(Collectors.toSet());
                	
            }
    
            detailsNotifTgr.setBeneficiareNonEnregistrer(beneficiaresAquittementTgrSet);
            detailsNotifTgrRepository.save(detailsNotifTgr);
            lotNotifTgrRepo.save(saved);
            addTracabiliteRetour(saved, acq.getBody().getLibeMotif(), acq.getBody().getCodeMotif());
        	
            
           List<Long> idNotifCollected = lotRabbitTGR.getLot().getListeBeneficiaires().stream().map(t -> Long.valueOf(((BenefNotificationTgrDto) t).getId())).collect(Collectors.toList());	
           repoEseag.flagNotifTGRCollected(idNotifCollected, 1);
           System.out.println("==== notif flagged "+new Date());
        }else {
            System.err.printf("NO RESPONSE");
            addTracabiliteRetour(saved, acq.getBody().getLibeMotif(), acq.getBody().getCodeMotif());
        }
        } else System.err.printf("AUTH KO");


	
		//System.out.println("--------fin consumer TGR : " + new Date());

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
	            System.out.println("----- RETOUR JSON "+ json);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
}
