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
import ma.cdgp.af.client.CmrClient;
import ma.cdgp.af.dto.af.cmr.LotSituationPrsCmrDto;
import ma.cdgp.af.dto.af.tgr.LotSituationPrsTgrDto;
import ma.cdgp.af.entity.tgr.DemandeSituationTgr;
import ma.cdgp.af.entity.tgr.LotSituationTgr;
import ma.cdgp.af.repository.LotSituationTgrRepo;
import ma.cdgp.af.service.RequestTgrExecutor;

@RestController
@RequestMapping("/tgr")
public class RequestTgrController {

	@Autowired
	LotSituationTgrRepo lotSituationPersonneRepo;
 
	@Autowired
	RequestTgrExecutor requestTgrExecutor;

	@Autowired
	CmrClient cmrClient;

	@PostMapping("/situation-personnes")
	@Operation(summary = "Traitement demandes TGR")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Succès", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = LotSituationPrsCmrDto.class)) }),
			@ApiResponse(responseCode = "400", description = "La syntaxe de la requête est erronée", content = @Content),
			@ApiResponse(responseCode = "404", description = "La syntaxe de la requête est erronée", content = @Content),
			@ApiResponse(responseCode = "500", description = "Erreur interne", content = @Content) })
	public ResponseEntity<LotSituationPrsTgrDto> getSituationPersonnesCmr(
			@Valid @RequestBody LotSituationPrsTgrDto lotSituation) throws Exception {
		LotSituationTgr requestEntity = new LotSituationTgr();
		requestEntity.setPartenaire("TGR");
		requestEntity.setDateCreation(new Date());
		requestEntity.setEtatLot("CREATED");
		requestEntity.setLotId(String.valueOf(lotSituation.getIdLot()));
		requestEntity.setDemandes(lotSituation.getPersonnes().stream().map(d -> {
			DemandeSituationTgr dp = new DemandeSituationTgr();
			dp.setCin(d.getCin());
			dp.setSituation(d.getSituation());
			dp.setCouvertureAf(d.getCouvertureAf());
			dp.setDateNaissance(d.getDateNaissance());
			return dp;
		}).collect(Collectors.toSet()));
		requestEntity.setters();
		LotSituationTgr savedRR = lotSituationPersonneRepo.save(requestEntity);
		requestTgrExecutor.traiterDemandes(savedRR.getId());
		LotSituationTgr foundLot = lotSituationPersonneRepo.findById(savedRR.getId()).orElse(null);
		return ResponseEntity.status(HttpStatus.OK).body(LotSituationPrsTgrDto.fromEntity(foundLot));
	}
}
