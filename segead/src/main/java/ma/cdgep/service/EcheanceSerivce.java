package ma.cdgep.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import ma.cdgep.paiement.dto.EcheanceDto;
import ma.cdgep.paiement.dto.ErreurDto;
import ma.cdgep.paiement.dto.LotPaiementDto;
import ma.cdgep.paiement.dto.RetourCnssDto;
import ma.cdgep.paiement.dto.SyntheseDto;
import ma.cdgep.paiement.dto.SyntheseEncheanceEnvoyeeProj;
import ma.cdgep.paiement.dto.SyntheseLotDto;
import ma.cdgep.paiement.entity.EcheanceEntity;
import ma.cdgep.paiement.entity.ErreurLotPaiementEntity;
import ma.cdgep.paiement.entity.LotPaiementEntity;
import ma.cdgep.paiement.entity.SyntheseEcheanceEntity;
import ma.cdgep.repository.EcheanceRepository;
import ma.cdgep.repository.ErreurLotPaiementRepository;
import ma.cdgep.repository.LotPaiementRepository;
import ma.cdgep.repository.PaiementRepository;
import ma.cdgep.repository.SyntheseEcheanceRepository;

@Service
public class EcheanceSerivce {

	private static final String CNSS_URL = "http://10.22.103.37:8080/aid-payment/v1/";

	@Autowired
	EcheanceRepository echeanceRepository;

	@Autowired
	SyntheseEcheanceRepository syntheseEcheanceRepository;

	@Autowired
	PaiementRepository paiementRepository;

	@Autowired
	LotPaiementRepository lotPaiementRepository;

	@Autowired
	ErreurLotPaiementRepository erreurLotPaiementRepository;
	
	public EcheanceEntity getEcheanceEncoursAndReceptionAutorise() {
		List<EcheanceEntity> listEcheance = echeanceRepository.findByStatut("ENCOURS");
		if(listEcheance != null && listEcheance.size() > 0 && "1".equals(listEcheance.get(0).getReceptionLotAutorise()))
			return listEcheance.get(0);
		return null;
	}
	public EcheanceEntity getEcheanceEncours() {
		List<EcheanceEntity> listEcheance = echeanceRepository.findByStatut("ENCOURS");
		if(listEcheance != null && listEcheance.size() > 0)
			return listEcheance.get(0);
		return null;
	}

	public EcheanceDto getEcheance(String annee, String mois, String type) {
		List<EcheanceEntity> list = echeanceRepository.findByAnneeAndMoisAndType(annee, mois, type);
		if (list != null && list.size() > 0)
			return EcheanceDto.from(list.get(0));
		return null;
	}

	public String alimenterNvDossieCloture(String moisAnnee) {
		String msg = paiementRepository.alimenterNvDossieCloture(moisAnnee);
		return msg;
	}

	public String alimenterNvDossierOuvert(String moisAnnee) {
		String msg = paiementRepository.alimenterNvDossierOuvert(moisAnnee);

		return msg;
	}

	public String alimenterCandidat(String moisAnnee) {
		String msg = paiementRepository.alimenterCandidat(moisAnnee);

		return msg;
	}

	public void sendLot(String refeeche, Integer numlot) {

		RestTemplate restTemplate = new RestTemplate();

		LotPaiementEntity lotEntity = lotPaiementRepository.getByReferenceEcheanceAndNumeroLot(refeeche, numlot);

		LotPaiementDto lotDto = LotPaiementDto.from(lotEntity);

		ResponseEntity<RetourCnssDto> result = restTemplate.postForEntity(CNSS_URL + "as-lot-ech", lotDto,
				RetourCnssDto.class);

		if (result.getStatusCode() == HttpStatus.OK) {

			RetourCnssDto retour = result.getBody();

			if ("S".equalsIgnoreCase(retour.getMessageType())) {

				lotPaiementRepository.updateLot(retour.getRefecnss(), retour.getMessageType(), lotEntity.getId());
			} else {
				lotPaiementRepository.updateLotEr(retour.getMessage(), retour.getMessageType(), lotEntity.getId());

			}
		}
	}

	public void initSyntheseEcheance(String refeeche, String prestation) {
		syntheseEcheanceRepository.initSyntheseEcheance(refeeche, prestation);
	}

	public SyntheseEncheanceEnvoyeeProj getSyntheseEncheanceEnvoyee(String refeeche, String prestation) {
		return syntheseEcheanceRepository.getSyntheseEncheanceEnvoyee(refeeche, prestation);
	}

	public SyntheseDto getSyntheseEcheance(String refeeche, String prestation) {

		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<SyntheseDto> result = restTemplate
				.getForEntity(CNSS_URL + "as-syn-ech/" + refeeche + "/" + prestation, SyntheseDto.class);

		if (result.getStatusCode() == HttpStatus.OK) {

			return result.getBody();

//			Object res = result.getBody();
//
//			ObjectMapper objectMapper = new ObjectMapper();
//			LinkedHashMap<String, Object> resultMap = objectMapper.convertValue(res,
//					new TypeReference<LinkedHashMap<String, Object>>() {
//					});
//
//			if (resultMap.containsKey("nomblots") && resultMap.containsKey("nomblign")
//					&& resultMap.containsKey("monttota")) {
//				return new ReponseSyntheseDto(new SyntheseDto(resultMap.get("nomblots").toString(),
//						resultMap.get("nomblign").toString(), resultMap.get("monttota").toString()), null);
//			}
//			if (resultMap.containsKey("messageType") && resultMap.containsKey("status")
//					&& resultMap.containsKey("message")) {
//				return new ReponseSyntheseDto(null, new ErreurDto((String) resultMap.get("messageType"),
//						(String) resultMap.get("status"), (String) resultMap.get("message")));
//			}

		}
		return null;
	}

	public SyntheseLotDto getSyntheseLot(String refeeche, String prestation) {

		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<Object> result = restTemplate
				.getForEntity(CNSS_URL + "as-syn-lots-ech/" + refeeche + "/" + prestation, Object.class);

		if (result.getStatusCode() == HttpStatus.OK) {

			Object res = result.getBody();

			if (res instanceof SyntheseLotDto) {
				return (SyntheseLotDto) res;
			}
			if (res instanceof ErreurDto) {

			}
		}
		return null;
	}

	public void sendAll(String referenceEcheance) throws InterruptedException {

		System.out.println("Start sending ");
		List<Long> ids = lotPaiementRepository.getall(referenceEcheance);
		for (long i : ids) {

			System.out.println("sending lot :" + i);
			this.sendLotId(i);

//			TimeUnit.SECONDS.sleep(10);
		}

		System.out.println("end sending ");

	}

	public void saveSyntheseEcheance(SyntheseEcheanceEntity syntheseEcheanceEntity) {
		syntheseEcheanceRepository.save(syntheseEcheanceEntity);

	}

	public LotPaiementDto getLot(Long id) {

		return LotPaiementDto.from(lotPaiementRepository.getById(id));

	}

	public String sendLot(Long id) {
		RestTemplate restTemplate = new RestTemplate();

		LotPaiementEntity lotEntity = lotPaiementRepository.getById(id);

		LotPaiementDto lotDto = LotPaiementDto.from(lotEntity);

		ResponseEntity<String> result = restTemplate.postForEntity(CNSS_URL + "as-lot-ech", lotDto, String.class);

		return result.getBody();

	}

	public void sendLotId(Long id) {

		RestTemplate restTemplate = new RestTemplate();

		LotPaiementEntity lotEntity = lotPaiementRepository.getById(id);

		LotPaiementDto lotDto = LotPaiementDto.from(lotEntity);

		try {
			ResponseEntity<RetourCnssDto> result = restTemplate.postForEntity(CNSS_URL + "as-lot-ech", lotDto,
					RetourCnssDto.class);

			if (result.getStatusCode() == HttpStatus.OK) {

				RetourCnssDto retour = result.getBody();

				if ("S".equalsIgnoreCase(retour.getMessageType())) {

					lotPaiementRepository.updateLot(retour.getRefecnss(), retour.getMessageType(), lotEntity.getId());
				} else {
					lotPaiementRepository.updateLotEr(retour.getMessage(), retour.getMessageType(), lotEntity.getId());

				}
			}

		} catch (HttpClientErrorException e) {

			ErreurLotPaiementEntity r = new ErreurLotPaiementEntity();
			r.setIdLot(id);
			r.setReference(String.valueOf(lotEntity.getNumeroLot()));
			r.setMessage(e.getResponseBodyAsString());
			r.setStatut("404");
			erreurLotPaiementRepository.save(r);
		} catch (HttpServerErrorException e) {
			ErreurLotPaiementEntity r = new ErreurLotPaiementEntity();
			r.setIdLot(id);
			r.setReference(String.valueOf(lotEntity.getNumeroLot()));
			r.setMessage(e.getResponseBodyAsString());
			r.setStatut("500");
			erreurLotPaiementRepository.save(r);
		} catch (Exception e) {
			ErreurLotPaiementEntity r = new ErreurLotPaiementEntity();
			r.setIdLot(id);
			r.setReference(String.valueOf(lotEntity.getNumeroLot()));
			r.setMessage(e.getMessage());
			r.setStatut("E");
			erreurLotPaiementRepository.save(r);
			e.printStackTrace();
		}
	}
}
