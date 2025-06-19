package ma.cdgp.af.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import ma.cdgp.af.dto.af.cnss.AcquittementSituationCnssDto;
import ma.cdgp.af.dto.af.cnss.LotReponseSituationCnssDto;
import ma.cdgp.af.dto.af.cnss.LotSituationPrsCnssDto;
import ma.cdgp.af.feign.FeignClientConfiguration;
import ma.cdgp.af.oauth2.JwtResponse;
import ma.cdgp.af.oauth2.LoginRequest;

@FeignClient(name = "${cnss.fa.service.name}", url = "${cnss.fa.url}", configuration = FeignClientConfiguration.class)
public interface CnssClient {

	@PostMapping(value = "/Session/Create", produces = { MediaType.APPLICATION_JSON_VALUE })
	public JwtResponse authCnss(@RequestBody LoginRequest loginRequest);
	
	
	@PostMapping(value = "/v1/as-lot-dem", consumes = MediaType.APPLICATION_JSON_VALUE)
	AcquittementSituationCnssDto askCnss(@RequestBody LotSituationPrsCnssDto reqIn,  @RequestHeader("Authorization") String token);
	
	@GetMapping(value = "/v1/as-lot-result/{identLot}", consumes = MediaType.APPLICATION_JSON_VALUE)
	LotReponseSituationCnssDto askCnssForResponse(@PathVariable("identLot") String identLot, @RequestHeader("Authorization") String token);
	
}