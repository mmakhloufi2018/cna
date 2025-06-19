package ma.cdgep.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ma.cdgep.paiement.dto.LotInduDto;
import ma.cdgep.service.InduSerivce;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/indus")
//@ApiIgnore
public class InduController {

	@Autowired
	InduSerivce induSerivce;

	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody List<LotInduDto> getLot() {
		return induSerivce.getLot();
	}


	@GetMapping(value = "send-indus", produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody void send() {

		new Thread(() -> {
			try {
				induSerivce.setBenifIndu();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}).start();
	}
	
	@GetMapping(value = "cloture-indus", produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody void cloturer() {

		new Thread(() -> {
			try {
				induSerivce.setClotureBenifIndu();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}).start();
	}

}
