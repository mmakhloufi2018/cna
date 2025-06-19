package ma.cdgep.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ma.cdgep.demande.dto.DemandeMajDto;
import ma.cdgep.demande.dto.LotAjoutMembreDto;
import ma.cdgep.demande.dto.LotAnnulationRecoursDto;
import ma.cdgep.demande.dto.LotDemandeAnnuleDto;
import ma.cdgep.demande.dto.LotDemandeDto;
import ma.cdgep.demande.dto.LotRecoursDto;
import ma.cdgep.demande.dto.ReponseDemandeMajDto;
import ma.cdgep.demande.dto.ReponseLotDemandeDto;
import ma.cdgep.paiement.entity.EcheanceEntity;
import ma.cdgep.service.DemandeMajSerivce;
import ma.cdgep.service.DemandeSerivce;
import ma.cdgep.service.EcheanceSerivce;
import ma.cdgep.service.LotSerivce;

@RestController
@RequestMapping("/demandes")
//@ApiIgnore
public class DemandeController {

	@Autowired
	LotSerivce lotSerivce;
	
	@Autowired
	DemandeSerivce demandeSerivce;
	
	@Autowired
	EcheanceSerivce echeanceSerivce;
	
	@Autowired
	DemandeMajSerivce demandeMajSerivce;
	
//	@GetMapping(value = "hash",  produces = { MediaType.APPLICATION_JSON_VALUE })
//	@ResponseStatus(HttpStatus.OK)
//	public @ResponseBody void getOne(@RequestParam(name = "id", required = true) String id)
//			 {
//		demandeSerivce.creerBloc("23/11/2023");
//	}
	
	

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Opération terminée avec succes"),
			@ApiResponse(code = 500, message = "Opération echouée") })
	@PostMapping
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Créer des demandes ", nickname = "creerLot")
	public @ResponseBody ReponseLotDemandeDto creerLot(@RequestBody LotDemandeDto lot) {
		EcheanceEntity echeance = echeanceSerivce.getEcheanceEncoursAndReceptionAutorise();
		if(echeance != null)
			return lotSerivce.saveLot(lot, echeance);
		throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "À l'heure actuelle, la réception des lots n'est pas autorisée.");
	}

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Opération terminée avec succes"),
			@ApiResponse(code = 500, message = "Opération echouée") })
	@PostMapping(value = "/annulation", produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Annuler des demandes ", nickname = "annulerLot")
	public @ResponseBody ReponseLotDemandeDto annulerLot(@RequestBody LotDemandeAnnuleDto lot) {
		EcheanceEntity echeance = echeanceSerivce.getEcheanceEncoursAndReceptionAutorise();
		if(echeance != null)
			return lotSerivce.annulerLot(lot, echeance);
		throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "À l'heure actuelle, la réception des lots n'est pas autorisée.");
		
	}
	
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Opération terminée avec succes"),
			@ApiResponse(code = 500, message = "Opération echouée") })
	@PostMapping(value = "/recours/annulation", produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Annuler Demandes Recours ", nickname = "annulerRecours")
	public @ResponseBody ReponseLotDemandeDto annulerLotRecours(@RequestBody LotAnnulationRecoursDto lot) {
		EcheanceEntity echeance = echeanceSerivce.getEcheanceEncoursAndReceptionAutorise();
		if(echeance != null)
			return lotSerivce.annulerRecoursLot(lot, echeance);
		throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "À l'heure actuelle, la réception des lots n'est pas autorisée.");
		
	}
	
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Opération terminée avec succes"),
			@ApiResponse(code = 500, message = "Opération echouée") })
	@PostMapping(value = "/recours", produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Recours", nickname = "Recours")
	public @ResponseBody ReponseLotDemandeDto recours(@RequestBody LotRecoursDto lot) {
		EcheanceEntity echeance = echeanceSerivce.getEcheanceEncours();
		if(echeance != null)
			return lotSerivce.recours(lot, echeance);
		throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "À l'heure actuelle, la réception des lots n'est pas autorisée.");
		
	}
	
	
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Opération terminée avec succes"),
			@ApiResponse(code = 500, message = "Opération echouée") })
	@PostMapping(value = "/ajoutmembre", produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "ajout membre", nickname = "ajoutmembre")
	public @ResponseBody ReponseLotDemandeDto ajoutmembre(@RequestBody LotAjoutMembreDto lot) {
		EcheanceEntity echeance = echeanceSerivce.getEcheanceEncoursAndReceptionAutorise();
		if(echeance != null)
			return lotSerivce.ajoutMembre(lot, echeance);
		throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "À l'heure actuelle, la réception des lots n'est pas autorisée.");
		
	}
	
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Opération terminée avec succes"),
			@ApiResponse(code = 500, message = "Opération echouée") })
	@PostMapping(value = "/miseajour", produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "MAJ", nickname = "MAJ")
	public @ResponseBody ReponseDemandeMajDto maj(@RequestBody DemandeMajDto maj) {
		if(maj == null)
			return new ReponseDemandeMajDto();
		EcheanceEntity echeance =  echeanceSerivce.getEcheanceEncours();
		if(echeance != null) {
			demandeMajSerivce.saveDemandeMaj(maj, echeance);
			return new ReponseDemandeMajDto(maj.getReferenceCnss(), null, "ACCEPTE");
		}
		throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "À l'heure actuelle, la réception des lots n'est pas autorisée.");
		
	}
	
//	@ApiResponses(value = { @ApiResponse(code = 200, message = "Opération terminée avec succes"),
//			@ApiResponse(code = 500, message = "Opération echouée") })
//	@PostMapping(value = "/traiter-demande", produces = { MediaType.APPLICATION_JSON_VALUE })
//	@ResponseStatus(HttpStatus.OK)
//	@ApiOperation(value = "Traiter demandes ", nickname = "creerLot")
//	public void traiterDemande() {
//		demandeSerivce.traiterDemandes();
//	}

//	@ApiResponses(value = { @ApiResponse(code = 200, message = "Opération terminée avec succes"),
//			@ApiResponse(code = 500, message = "Opération echouée") })
//	@PostMapping(value = "/test", produces = { MediaType.APPLICATION_JSON_VALUE })
//	@ResponseStatus(HttpStatus.OK)
//	@ApiOperation(value = "Créer/Modifier des demandes de test ", nickname = "creerLot")
//	public void testerLot(int nbrLot, int nbrDemande) {
//		long i = 0;
//		while (i < nbrLot) {
//			Date d0 = new Date();
//			creerLot(genererLot(i++, nbrDemande));
//			Date d1 = new Date();
//			System.out.println("lot " + i + " : " + (d1.getTime() - d0.getTime()));
//		}
//	}

//	private LotDemandeDto genererLot(long i, int nbrDemande) {
//		return new LotDemandeDto("ref" + i, "2023-10-31", "ENFANT", genererDemande(i, nbrDemande));
//	}

/*	private List<DemandeDto> genererDemande(long i, int nbrDemande) {
		// TODO Auto-generated method stub
		List<DemandeDto> list = new ArrayList<DemandeDto>();
		long j = i * 1000;
		while (j <= i * 1000 + nbrDemande -1)
			list.add(new DemandeDto("2023-10-31", "refCnss" + j, "immatriculation" + j, genrerDemandeur(j++)));

		return list;

	}

	private DemandeurDto genrerDemandeur(long l) {
		// TODO Auto-generated method stub
		return new DemandeurDto("idcs" + l, "cin" + l, "codeMenage" + l, "NomFr" + l, "PrenomFr" + l, "NomAr" + l,
				"PrenomAr" + l, "M", "" + getScore(), "M", "1970-04-22", "Adresse" + l, "1", "1", genererConjoint(l),
				genererEnfant(l), genererMembre(l));
	}

	private List<AutreMembreDto> genererMembre(long l) {
		List<AutreMembreDto> membres = new ArrayList<AutreMembreDto>();
		membres.add(new AutreMembreDto("CinM" + l, "idcsM" + l, "2018-02-12", "ADOPTIF", "CodeMenage" + l, "NomAr" + l,
				"NomFr" + l, "PrenomAr" + l, "PrenomFr" + l, "" + getScore(), "1"));
		return membres;
	}

	private List<EnfantDto> genererEnfant(long l) {
		List<EnfantDto> enfants = new ArrayList<EnfantDto>();
		enfants.add(new EnfantDto("CinEnf1-" + l, "idcsEnf1-" + l, "CinConj" + l, "idcsConj" + l, "cineEnf1-" + l,
				"idcs" + l, "2007-12-21", "0", "1" + l, "ENFANT", "CodeMenage" + l, "NomArEnf1-" + l,
				"NomFrEnf1-" + l, "PrenomArEnf1-" + l, "PrenomFrEnf1-" + l, "1", "" + getScore(), "1",
				"TypeScolarite", "0", "1"));
		enfants.add(new EnfantDto("CinEnf2-" + l, "idcsEnf2-" + l, "CinConj" + l, "idcsConj" + l, "cinEnf2-" + l,
				"idcs" + l, "2019-12-30", "0", "1" + l, "ENFANT", "CodeMenage" + l, "NomArEnf1-" + l,
				"NomFrEnf1-" + l, "PrenomArEnf1-" + l, "PrenomFrEnf2-" + l, "1", "" + getScore(), "1",
				"TypeScolarite", "0", "1"));
		enfants.add(new EnfantDto("CinEnf3-" + l, "idcsEnf3-" + l, "CinConj" + l, "idcsConj" + l, "cinEnf3-" + l,
				"idcs" + l, "2014-03-12", "0", "1" + l, "ENFANT", "CodeMenage" + l, "NomArEnf1-" + l,
				"NomFrEnf1-" + l, "PrenomArEnf1-" + l, "PrenomFrEnf3-" + l, "1", "" + getScore(), "1",
				"TypeScolarite", "0", "1"));
		return enfants;
	}

	private List<ConjointDto> genererConjoint(long l) {
		List<ConjointDto> list = new ArrayList<ConjointDto>();
		list.add(new ConjointDto("idcsConj" + l, "CinConj" + l, "CodeMenage" + l, "NomConj" + l, "PrenomConj" + l,
				"NomConjAr" + l, "PrenomArConj" + l, "" + getScore(), "1998-01-23", "1"));
		return list;
	}

	private Double getScore() {
		return ThreadLocalRandom.current().nextDouble(0, 10);

	}
*/
}
