package ma.cdgep.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import ma.cdgep.eligibilite.dto.EligibiliteLotDemandeDto;
import ma.cdgep.eligibilite.dto.EligibiliteReponseDto;
import ma.cdgep.eligibilite.entity.EligibiliteLotDemandeEntity;
import ma.cdgep.eligibilite.entity.RetourEligibiliteLotEntity;
import ma.cdgep.repository.EligibiliteRepository;
import ma.cdgep.repository.RetourEligibiliteRepository;

@Service
public class EligibiliteSerivce {

	private static final String CNSS_URL = "http://10.22.103.37:8080/aid-eligibility-batch/update";

	@Autowired
	EligibiliteRepository eligibiliteRepository;

	@Autowired
	RetourEligibiliteRepository retourEligibiliteRepository;

	public void sendLot(Long id) throws Exception {

		RestTemplate restTemplate = new RestTemplate();

		EligibiliteLotDemandeEntity lotEntity = eligibiliteRepository.getById(id);

		EligibiliteLotDemandeDto lotDto = EligibiliteLotDemandeDto.from(lotEntity);
		HttpEntity<EligibiliteLotDemandeDto> entity = new HttpEntity<EligibiliteLotDemandeDto>(lotDto);
		ResponseEntity<String> result = null;
		try {
			result = restTemplate.exchange(CNSS_URL, HttpMethod.PUT, entity, String.class);

			if (result.getStatusCode() == HttpStatus.OK) {
				ObjectMapper mapper = new ObjectMapper();

				EligibiliteReponseDto retour = mapper.readValue(result.getBody(), EligibiliteReponseDto.class);

				eligibiliteRepository.updateStatut(retour.getStatus(), retour.getError(), id);
			}

		} catch (HttpClientErrorException e) {

			
			RetourEligibiliteLotEntity r = new RetourEligibiliteLotEntity();
			r.setIdLot(id);
			r.setReference(lotEntity.getReference());
			r.setMessage(e.getResponseBodyAsString());
			r.setStatut("404");
			retourEligibiliteRepository.save(r);
		} catch (HttpServerErrorException e) {
			RetourEligibiliteLotEntity r = new RetourEligibiliteLotEntity();
			r.setIdLot(id);
			r.setReference(lotEntity.getReference());
			r.setMessage(e.getResponseBodyAsString());
			r.setStatut("500");
			retourEligibiliteRepository.save(r);
			throw e;
		} catch (Exception e) {
			RetourEligibiliteLotEntity r = new RetourEligibiliteLotEntity();
			r.setIdLot(id);
			r.setReference(lotEntity.getReference());
			r.setMessage(e.getMessage());
			r.setStatut("E");
			retourEligibiliteRepository.save(r);
			e.printStackTrace();
			throw e;
		}
	}

	public void sendAll(String echeance) throws InterruptedException {

		System.out.println("Start sending ");

		List<Long> ids = eligibiliteRepository.getallBy(echeance);
		for (long i : ids) {

			System.out.println("sending lot :" + i);
			try {
				this.sendLot(i);
			} catch (Exception e) {
				e.printStackTrace();

				eligibiliteRepository.updateStatutError("E3", e.getMessage(), i);

			}

//			TimeUnit.SECONDS.sleep(10);
		}

		System.out.println("end sending ");

	}

	public EligibiliteLotDemandeDto getOne(Long id) {
		return EligibiliteLotDemandeDto.from(eligibiliteRepository.getById(id));
	}

}
