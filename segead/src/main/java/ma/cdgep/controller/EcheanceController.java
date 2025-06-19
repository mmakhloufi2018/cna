package ma.cdgep.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ma.cdgep.paiement.dto.EcheanceDto;
import ma.cdgep.paiement.dto.LotPaiementDto;
import ma.cdgep.paiement.dto.SyntheseDto;
import ma.cdgep.service.EcheanceSerivce;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/echeances")
@Api("Service pour la consultation des écheances.")
@ApiIgnore
public class EcheanceController {

	@Autowired
	EcheanceSerivce echeanceSerivce;

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Opération terminée avec succes"),
			@ApiResponse(code = 500, message = "Opération echouée") })
	@ApiOperation(value = "Récuperer une écheance par année , moit et type deprestation ", nickname = "getEcheance")
	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody EcheanceDto getEcheance(@RequestParam(name = "annee", required = true) String annee,
			@RequestParam(name = "mois", required = true) String mois,
			@RequestParam(name = "type", required = true) String type) {
		return echeanceSerivce.getEcheance(annee, mois, type);
	}

	@PostMapping(value = "/alimenter-doss-clot", produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "Alimenter Les nouvelles dossiers cloturés.", nickname = "alimenterNvDossierCloture")
	public void alimenterNvDossierCloture(@RequestParam("moisAnnee") String moisAnnee) {
		new Thread(() -> {
			try {
				String message = echeanceSerivce.alimenterNvDossieCloture(moisAnnee);
				if (!"V".equals(message))
					System.out.println("message :" + message);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
	}

	@PostMapping(value = "/alimenter-doss-ouv", produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "Alimenter Les nouvelles dossiers cloturés.", nickname = "alimenterNvDossierOuvert")
	@ResponseStatus(HttpStatus.OK)
	public void alimenterNvDossierOuvert(@RequestParam("moisAnnee") String moisAnnee) {

		new Thread(() -> {
			try {
				String message = echeanceSerivce.alimenterNvDossierOuvert(moisAnnee);
				if (!"V".equals(message))
					System.out.println("message :" + message);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();

	}

	@PostMapping(value = "/alimenter-candidat", produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "Alimenter Les nouvelles dossiers cloturés.", nickname = "alimenterCandidat")
	@ResponseStatus(HttpStatus.OK)
	public void alimenterCandidat(@RequestParam("moisAnnee") String moisAnnee) {

		new Thread(() -> {
			try {
				String message = echeanceSerivce.alimenterCandidat(moisAnnee);
				if (!"V".equals(message))
					System.out.println("message :" + message);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();

	}

//	@ApiResponses(value = { @ApiResponse(code = 200, message = "Opération terminée avec succes"),
//			@ApiResponse(code = 500, message = "Opération echouée") })
//	@ApiOperation(value = "Récuperer des paiments des bénéficiare par référence écheance et numero lot ", nickname = "getLitPaiement")
//	@GetMapping(value = "/paiement/{referenceEcheance}/{idLot}", produces = { MediaType.APPLICATION_JSON_VALUE })
//	@ResponseStatus(HttpStatus.OK)
//	public @ResponseBody List<PaiementDto> getLitPaiement(@ApiParam(name = "referenceEcheance", value = "la reference de l'echeance")  @PathVariable("referenceEcheance") String referenceEcheance,
//			@ApiParam(name = "idLot", value = "le numero du lot") @PathVariable("idLot") String idLot) {
//		return echeanceSerivce.getListPaiement(referenceEcheance, idLot);
//	}

	@GetMapping(value = "send", produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody void send(@RequestParam("echeance") String echeance) {

		new Thread(() -> {
			try {
				echeanceSerivce.sendAll(echeance);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}).start();
	}

//	@GetMapping(value = "/synthese-echeance", produces = { MediaType.APPLICATION_JSON_VALUE })
//	@ResponseStatus(HttpStatus.OK)
//	public void getSyntheseEcheance(@RequestParam("refeeche") String refeeche,
//			@RequestParam("prestation") String prestation) {
//
//		echeanceSerivce.initSyntheseEcheance(refeeche, prestation);
//		SyntheseDto s = echeanceSerivce.getSyntheseEcheance(refeeche, prestation);
		
//		if (s.getSyntheseDto() != null) {
//			System.out.println("synthese : " + s.getSyntheseDto().getNombreLignes());
//			echeanceSerivce.saveSyntheseEcheance(SyntheseEcheanceEntity.to(refeeche, prestation, s, echeanceSerivce.getSyntheseEncheanceEnvoyee(refeeche, prestation)));
//		}
//		else if (s.getErreurDto() != null)
//			System.out.println("Erreur : " + s.getErreurDto().getMessage());

		
//	}

	@PostMapping(value = "send", produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody void send(@RequestBody LotPaiementDto lotPaiementDto) {

		echeanceSerivce.sendLot(lotPaiementDto.getReferenceEcheance(), lotPaiementDto.getNumeroLot());
	}
	
	@GetMapping(value = "get-one/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody LotPaiementDto get(@PathVariable(name = "id" ) String id) {

		return echeanceSerivce.getLot(Long.valueOf(id));
	}
	
	@GetMapping(value = "send-one/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody String sendOne(@PathVariable(name = "id" ) String id) {

		return echeanceSerivce.sendLot(Long.valueOf(id));
	}
	
	@GetMapping(value = "synthese",  produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody SyntheseDto synthese() {
		
		return echeanceSerivce.getSyntheseEcheance("2023-11-01", "F");
	}
}
