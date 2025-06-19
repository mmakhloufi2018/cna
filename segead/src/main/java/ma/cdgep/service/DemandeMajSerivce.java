package ma.cdgep.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ma.cdgep.demande.dto.DemandeMajDto;
import ma.cdgep.demande.entity.DemandeMajEntity;
import ma.cdgep.paiement.entity.EcheanceEntity;
import ma.cdgep.repository.DemandeMajRepository;

@Service
public class DemandeMajSerivce {

	@Autowired
	DemandeMajRepository demandeMajRepository;

	public Integer saveDemandeMaj(DemandeMajDto demande, EcheanceEntity echeance) {
		DemandeMajEntity d = DemandeMajDto.to(demande);
		d.setDateDemande(new Date());
		d.setEcheance(echeance);
		demandeMajRepository.save(d);
		return 1;
	}

}
