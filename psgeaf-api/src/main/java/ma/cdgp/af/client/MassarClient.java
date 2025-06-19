package ma.cdgp.af.client;


import ma.cdgp.af.dto.af.massar2025.LotSituationPrsMassar2025Dto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import ma.cdgp.af.dto.af.ResponseMassarDto;
import ma.cdgp.af.dto.af.massar.LotSituationPrsMassarDto;
import ma.cdgp.af.feign.FeignClientConfiguration;

@FeignClient(name = "${massar.fa.service.name}", url = "${massar.fa.url}", configuration = FeignClientConfiguration.class)
public interface MassarClient {


    @PostMapping(value = "/eleves/liste", consumes = MediaType.APPLICATION_JSON_VALUE)
    LotSituationPrsMassarDto askMassar(@RequestBody LotSituationPrsMassarDto lot,
                                       @RequestHeader("X-Client-ID") String clientId,
                                       @RequestHeader("X-Request-ID") String requestID,
                                       @RequestHeader("X-Request-Application-Name") String appNam,
                                       @RequestHeader("Authorization") String token );

	@GetMapping(value = "/eleves/donnees", consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseMassarDto getEleveDonnees(
			@RequestParam(name = "codeEleve") String codeEleven,
			@RequestParam(name = "niveau", required = false) String niveau,
			@RequestParam(name = "AnneeScolaire", required = false) String anneeScolaire,
			@RequestHeader("X-Client-ID") String clientId, @RequestHeader("X-Request-ID") String requestID,
			@RequestHeader("X-Request-Application-Name") String appNam,  
			@RequestHeader("Authorization") String tokenBerear);



	@PostMapping(value = "/eleves/liste", consumes = MediaType.APPLICATION_JSON_VALUE)
	LotSituationPrsMassar2025Dto askMassar2025(@RequestBody LotSituationPrsMassar2025Dto lot,
											   @RequestHeader("X-Client-ID") String clientId,
											   @RequestHeader("X-Request-ID") String requestID,
											   @RequestHeader("X-Request-Application-Name") String appNam,
											   @RequestHeader("Authorization") String token );




}
