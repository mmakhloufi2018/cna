package ma.cdgp.af.controller;

import java.util.Date;
import java.util.stream.Collectors;

import javax.validation.Valid;

import ma.cdgp.af.service.RequestBuilderExecutor;
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
import ma.cdgp.af.dto.af.sante.LotSituationPrsSanteDto;
import ma.cdgp.af.entity.sante.DemandeSituationSante;
import ma.cdgp.af.entity.sante.LotSituationSante;
import ma.cdgp.af.repository.LotSituationSanteRepo;
import ma.cdgp.af.service.RequestSanteExecutor;

@RestController
@RequestMapping("/sante")
public class RequestSanteController {

	@Autowired
	LotSituationSanteRepo lotSituationPersonneRepo;
 
	@Autowired
	RequestSanteExecutor requestSanteExecutor;

	@Autowired
	RequestBuilderExecutor requestBuilderExecutor;

	@Autowired
	CmrClient cmrClient;

	@PostMapping("/situation-personnes")
	@Operation(summary = "Traitement demandes Sante")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Succès", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = LotSituationPrsSanteDto.class)) }),
			@ApiResponse(responseCode = "400", description = "La syntaxe de la requête est erronée", content = @Content),
			@ApiResponse(responseCode = "404", description = "La syntaxe de la requête est erronée", content = @Content),
			@ApiResponse(responseCode = "500", description = "Erreur interne", content = @Content) })
	public ResponseEntity<LotSituationPrsSanteDto> getSituationPersonnesSante(
			@Valid @RequestBody LotSituationPrsSanteDto lotSituation) throws Exception {
		LotSituationSante requestEntity = new LotSituationSante();
		requestEntity.setPartenaire("SANTE");
		requestEntity.setDateCreation(new Date());
		requestEntity.setEtatLot("CREATED");
		requestEntity.setLotId(String.valueOf(lotSituation.getIdLot()));
		requestEntity.setDemandes(lotSituation.getPersonnes().stream().map(d -> {
			DemandeSituationSante dp = new DemandeSituationSante();
			dp.setIdcs(d.getIdcs());
			return dp;
		}).collect(Collectors.toSet()));
		requestEntity.setters();
		LotSituationSante savedRR = lotSituationPersonneRepo.save(requestEntity);
		requestSanteExecutor.traiterDemandes(savedRR.getId());
		LotSituationSante foundLot = lotSituationPersonneRepo.findById(savedRR.getId()).orElse(null);
		return ResponseEntity.status(HttpStatus.OK).body(LotSituationPrsSanteDto.fromEntity(foundLot));
	}



	@PostMapping("/runSante")
	public void runSante() {
		requestBuilderExecutor.runSanteCheck();
	}
}
