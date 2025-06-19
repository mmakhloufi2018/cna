package ma.cdgp.af.controller;

import java.util.Date;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import ma.cdgp.af.client.CnssClient;
import ma.cdgp.af.dto.af.cmr.LotSituationPrsCmrDto;
import ma.cdgp.af.dto.af.cnss.LotSituationPrsCnssDto;
import ma.cdgp.af.entity.cnss.DemandeSituationCnss;
import ma.cdgp.af.entity.cnss.LotSituationCnss;
import ma.cdgp.af.repository.LotSituationCnssRepo;
import ma.cdgp.af.service.RequestCnssExecutor;

@RestController
@RequestMapping("/cnss")
public class RequestCnssController {

	@Autowired
	LotSituationCnssRepo lotSituationPersonneRepo;
 
	@Autowired
	RequestCnssExecutor requestCnssExecutor;

	@Autowired
	CnssClient cmrClient;

	@PostMapping("/situation-personnes")
	@Operation(summary = "Traitement demandes CNSS")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Succès", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = LotSituationPrsCmrDto.class)) }),
			@ApiResponse(responseCode = "400", description = "La syntaxe de la requête est erronée", content = @Content),
			@ApiResponse(responseCode = "404", description = "La syntaxe de la requête est erronée", content = @Content),
			@ApiResponse(responseCode = "500", description = "Erreur interne", content = @Content) })
	public ResponseEntity<LotSituationPrsCnssDto> getSituationPersonnesCnss(
			@Valid @RequestBody LotSituationPrsCnssDto lotSituation) throws Exception {
		LotSituationCnss requestEntity = new LotSituationCnss();
		requestEntity.setPartenaire("CNSS");
		requestEntity.setDateCreation(new Date());
		requestEntity.setEtatLot("CREATED");
		requestEntity.setDateLot(lotSituation.getDateLot());
		requestEntity.setLotId(String.valueOf(lotSituation.getReferenceLot()));
		requestEntity.setDemandes(lotSituation.getPersonnes().stream().map(d -> {
			DemandeSituationCnss dp = new DemandeSituationCnss();
			dp.setCin(d.getCin());
			dp.setDateNaissance(d.getDateNaissance());
			return dp;
		}).collect(Collectors.toSet()));
		requestEntity.setters();
		LotSituationCnss savedRR = lotSituationPersonneRepo.save(requestEntity);
		requestCnssExecutor.traiterDemandes(savedRR.getId());
		LotSituationCnss foundLot = lotSituationPersonneRepo.findById(savedRR.getId()).orElse(null);
		return ResponseEntity.status(HttpStatus.OK).body(LotSituationPrsCnssDto.fromEntity(foundLot));
	}
	
	@PostMapping("/situation-personnes-reponse")
	@Operation(summary = "Traitement demandes CNSS")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Succès", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = LotSituationPrsCmrDto.class)) }),
			@ApiResponse(responseCode = "400", description = "La syntaxe de la requête est erronée", content = @Content),
			@ApiResponse(responseCode = "404", description = "La syntaxe de la requête est erronée", content = @Content),
			@ApiResponse(responseCode = "500", description = "Erreur interne", content = @Content) })
	public ResponseEntity<LotSituationPrsCnssDto> traitementReponseSituationPersonnesCnss(
			@Valid @RequestBody LotSituationPrsCnssDto lotSituation) throws Exception {
		requestCnssExecutor.traiterReponsesSituationCnss(lotSituation);
		return ResponseEntity.status(HttpStatus.OK).body(lotSituation);
	}
}
