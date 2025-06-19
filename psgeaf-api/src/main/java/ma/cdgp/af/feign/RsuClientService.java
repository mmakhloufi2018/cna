//package ma.cdgp.cmr.feign;
//
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestHeader;
//
//import ma.cdgp.cmr.dto.AcquittementRetourRsuRcarDto;
//import ma.cdgp.cmr.dto.ResponseCmrDto;
//import ma.cdgp.cmr.oauth2.JwtResponse;
//import ma.cdgp.cmr.oauth2.LoginRequest;
//
//@FeignClient(name = "rsuService", url = "${rsu.url}", configuration = FeignClientConfiguration.class)
//public interface RsuClientService {
//
//	@PostMapping(value = "/clients-services/v1/recevoir/rcar/consommations", consumes = MediaType.APPLICATION_JSON_VALUE)
//	AcquittementRetourRsuRcarDto postFromCDGP(@RequestBody ResponseCmrDto retourRcar 
//			, @RequestHeader("X-Client-ID") String clientId,
//			@RequestHeader("X-Request-ID") String requestID, @RequestHeader("X-Request-Application-Name") String appNam,
//			@RequestHeader("X-Token") String token,@RequestHeader("Authorization") String tokenBerear);
//
//	@PostMapping(value = "/clients-services/v1/Session", consumes = MediaType.APPLICATION_JSON_VALUE)
//	JwtResponse authenticateToRsu(@RequestBody LoginRequest loginRequest, @RequestHeader("X-Client-ID") String clientId,
//			@RequestHeader("X-Request-ID") String requestID, @RequestHeader("X-Request-Application-Name") String appNam,
//			@RequestHeader("Authorization") String token);
//}


