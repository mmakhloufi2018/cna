package ma.cdgep.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ma.cdgep.droit.dto.DroitDto;

@RestController
@RequestMapping("/droits")
//@ApiIgnore
public class DroitController {


	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody DroitDto getDetailDroit(@RequestParam(name = "idcs") String idcs, @RequestParam(name = "echeance") String echeance)
			 {
		return new DroitDto();
	}
	
}
