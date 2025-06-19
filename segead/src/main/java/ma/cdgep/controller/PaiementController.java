package ma.cdgep.controller;

import java.util.List;

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

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ma.cdgep.paiement.dto.InfoPaiementDto;
import ma.cdgep.paiement.dto.LotImpayesDto;
import ma.cdgep.paiement.dto.PaiementCnssDto;
import ma.cdgep.paiement.dto.RepLotImapyesDto;
import ma.cdgep.service.PaiementSerivce;

@RestController
@RequestMapping("/paiement")
public class PaiementController {

	@Autowired
	PaiementSerivce paiementSerivce;

	@GetMapping(value = "chercher", produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody List<InfoPaiementDto> getOne(
			@RequestParam(name = "reference", required = true) String reference) {
		return paiementSerivce.findByReferencePaiement(reference);
	}

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Opération terminée avec succes"),
			@ApiResponse(code = 500, message = "Opération echouée") })
	@PostMapping(value = "/impayes", produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Enregistrer un lot des impayés ", nickname = "saveLotImpayes")
	public @ResponseBody RepLotImapyesDto saveLotImpayes(@RequestBody LotImpayesDto lot) {
		return paiementSerivce.saveLotImpayes(lot);
	}

	@GetMapping(value = "{referencePaiement}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public PaiementCnssDto getDetailPaiement(@PathVariable("referencePaiement") String referencePaiement) {
		return PaiementCnssDto.from(paiementSerivce.getDetailPaiement(referencePaiement));
	}

}
