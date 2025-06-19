package ma.cdgep.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ma.cdgep.paiement.dto.LotInduDto;
import ma.cdgep.paiement.dto.LotInduRetourDto;
import ma.cdgep.repository.PaiementRepository;

@Service
public class InduSerivce {

	private static final String CNSS_URL = "http://10.22.103.37:8080/api_indu_asd/";

	@Autowired
	PaiementRepository paiementRepository;

	public void setBenifIndu() {

		RestTemplate restTemplate = new RestTemplate();
		List<Object[]> lots = paiementRepository.getBenifIndu();

		LotInduDto dto = new LotInduDto();

		for (Object[] obj : lots) {

			dto.setDateEcheance(String.valueOf(obj[1]));
			dto.setNumLot(String.valueOf(obj[2]));

			dto.setListBenif(paiementRepository.getbenefIndu((BigDecimal) obj[0]));

			ResponseEntity<LotInduRetourDto> result = restTemplate.postForEntity(CNSS_URL + "setBenifIndu", dto,
					LotInduRetourDto.class);

			if (result.getStatusCode() == HttpStatus.OK) {

				LotInduRetourDto retour = result.getBody();

				retour.getListBenif().forEach(b -> {

					paiementRepository.updateIndu(b.getIdcs(), b.getCodeResponse(), b.getLibResponse(),
							((BigDecimal) obj[0]));
				});

				paiementRepository.updateLotIndu("S", ((BigDecimal) obj[0]));
			} else if (result.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {

				paiementRepository.updateLotIndu("E",(BigDecimal) obj[0]);
			}

		}
	}

	public void setClotureBenifIndu() {

		RestTemplate restTemplate = new RestTemplate();
		List<Object[]> lots = paiementRepository.getClotureBenifIndu();

		LotInduDto dto = new LotInduDto();

		for (Object[] obj : lots) {

			dto.setNumLot(String.valueOf(obj[1]));

			dto.setListBenif(paiementRepository.getClotureIndu((BigDecimal) obj[0]));

			ResponseEntity<LotInduRetourDto> result = restTemplate.postForEntity(CNSS_URL + "clotureBenifIndu", dto,
					LotInduRetourDto.class);

			if (result.getStatusCode() == HttpStatus.OK) {

				LotInduRetourDto retour = result.getBody();

				retour.getListBenif().forEach(b -> {

					paiementRepository.updateclotureBenifIndu(b.getIdcs(), b.getCodeResponse(), b.getLibResponse(),
							(BigDecimal) obj[0]);
				});

				paiementRepository.updateLotclotureBenifIndu("S", (BigDecimal) obj[0]);
			} else if (result.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {

				paiementRepository.updateLotclotureBenifIndu("E", (BigDecimal) obj[0]);
			}

		}
	}

	public List<LotInduDto> getLot() {

		List<Object[]> lots = paiementRepository.getBenifIndu();

		List<LotInduDto> ls = new ArrayList<LotInduDto>();

		LotInduDto dto = new LotInduDto();

		for (Object[] obj : lots) {

			dto.setDateEcheance(String.valueOf(obj[1]));
			dto.setNumLot(String.valueOf(obj[2]));

			dto.setListBenif(paiementRepository.getbenefIndu((BigDecimal) obj[0]));

			ls.add(dto);

		}

		return ls;
	}
}
