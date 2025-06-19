package ma.cdgp.af.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import ma.cdgp.af.dto.af.cmr.LotSituationPrsCmrDto;
import ma.cdgp.af.feign.FeignClientConfiguration;
import ma.cdgp.af.oauth2.JwtResponse;
import ma.cdgp.af.oauth2.LoginRequest;

@FeignClient(name = "${cmr.fa.service.name}", url = "${cmr.fa.url}", configuration = FeignClientConfiguration.class)
public interface CmrClient {

	@PostMapping(value = "/Session/Create", produces = { MediaType.APPLICATION_JSON_VALUE })
	public JwtResponse authCmr(@RequestBody LoginRequest loginRequest);
	
	
	@PostMapping(value = "/v1/poc/demandes/saveRequete", consumes = MediaType.APPLICATION_JSON_VALUE)
	LotSituationPrsCmrDto askCmr(@RequestBody LotSituationPrsCmrDto lot,  @RequestHeader("Authorization") String token);
	
	
}