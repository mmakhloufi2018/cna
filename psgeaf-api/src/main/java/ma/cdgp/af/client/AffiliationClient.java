package ma.cdgp.af.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import ma.cdgp.af.FeignClientInterceptor;
import ma.cdgp.af.dto.af.CandidatInfos;

@FeignClient(name = "${affiliation-service-name}", url = "${affiliation.url}", configuration = FeignClientInterceptor.class)
public interface AffiliationClient {

	@GetMapping(value = "/persons/cmr-infos/{identifiant}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public CandidatInfos getPersonInfoCmr(@PathVariable(name = "identifiant") String identifiant);

}
