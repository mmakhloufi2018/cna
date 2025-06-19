package ma.cdgep.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ma.cdgep.eligibilite.dto.EligibiliteLotDemandeDto;
import ma.cdgep.service.EligibiliteSerivce;

@RestController
@RequestMapping("/eligibilites")
//@ApiIgnore
public class EligibiliteController {

	@Autowired
	EligibiliteSerivce eligibiliteSerivce;

	@GetMapping(value = "test",  produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody EligibiliteLotDemandeDto getOne(@RequestParam(name = "id", required = true) String id)
			 {
		return eligibiliteSerivce.getOne(Long.valueOf(id));
	}
	
	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody void send(@RequestParam("echeance") String echeance) {
		new Thread(() -> {
			try {

				System.out.println("debut");
				
				eligibiliteSerivce.sendAll(echeance);
				
				System.out.println("fin");
				
			}catch (Exception e) {
				e.printStackTrace();
			}
			
		}).start();
	}

}
