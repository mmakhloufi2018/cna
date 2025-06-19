package ma.cdgep.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ma.cdgep.paiement.dto.ImpayeDto;
import ma.cdgep.paiement.dto.InfoPaiementDto;
import ma.cdgep.paiement.dto.LotImpayesDto;
import ma.cdgep.paiement.dto.PaiementCnssDto;
import ma.cdgep.paiement.dto.RepImpayeDto;
import ma.cdgep.paiement.dto.RepLotImapyesDto;
import ma.cdgep.paiement.entity.ImpayeEntity;
import ma.cdgep.paiement.entity.InfoPaiementEntity;
import ma.cdgep.paiement.entity.LotImpayesEntity;
import ma.cdgep.repository.InfoPaiementRepository;
import ma.cdgep.repository.LotImpayesRepository;
import ma.cdgep.repository.PaiementCnss;
import ma.cdgep.repository.PaiementRepository;
import ma.cdgep.utils.Utils;

@Service
public class PaiementSerivce {

	@Autowired
	InfoPaiementRepository infoPaiementRepository;
	
	@Autowired
	PaiementRepository paiementRepository;
	
	@Autowired
	LotImpayesRepository lotImpayesRepository ;

	public List<InfoPaiementDto> findByReferencePaiement(String referencePaiement) {
		if (referencePaiement == null)
			return null;

		List<InfoPaiementEntity> list = infoPaiementRepository.findByReferencePaiement(referencePaiement);
		return list.stream().map(InfoPaiementDto::from).collect(Collectors.toList());

	}
	
	
	
	public RepLotImapyesDto saveLotImpayes(LotImpayesDto lot) {
		
		Set<RepImpayeDto> rejets = new HashSet<RepImpayeDto>();
		Set<ImpayeEntity> acceptes = new HashSet<ImpayeEntity>();

		
		for(ImpayeDto im : lot.getImpayes()) {
			
			if(infoPaiementRepository.getDemandeur(im.getIdcs()) != null) {
				
				if(infoPaiementRepository.getPaiement(im.getIdcs(), im.getReferencePaiement()) != null) {
					
					acceptes.add(ImpayeDto.to(im));
				} else {
					RepImpayeDto rej = new RepImpayeDto();
					
					rej.setIdcs(im.getIdcs());
					rej.setReferencePaiement(im.getReferencePaiement());
					rej.setCodeResponse("3");
					rej.setLibResponse("IDCS communiqué est non identique avec la référence de paiement");
					rejets.add(rej);
				}
			} else {
				RepImpayeDto rej = new RepImpayeDto();
				
				rej.setIdcs(im.getIdcs());
				rej.setReferencePaiement(im.getReferencePaiement());
				rej.setCodeResponse("1");
				rej.setLibResponse("IDCS inexistant");
				rejets.add(rej);
			}
			
		}
		
		LotImpayesEntity lotImp = new LotImpayesEntity();
		lotImp.setNumeroLot(lot.getNumeroLot());
		
		lotImp.setDateLot(Utils.stringToDate(lot.getDateLot(), Utils.FOURMAT_DATE_STRING));

		if (acceptes != null) {
			lotImp.setImpayes(acceptes);

			lotImp.getImpayes().forEach(i -> i.setLotImpayes(lotImp));
		}
		
		lotImpayesRepository.save(lotImp);
		
		RepLotImapyesDto repLot = new RepLotImapyesDto();
		repLot.setNumeroLot(lot.getNumeroLot());
		repLot.setStatutLot(rejets.isEmpty() ? 0 : 1);
		repLot.setImpayesRejetes(rejets);
		
		return repLot;
	}
	
	public List<PaiementCnss> getDetailPaiement(String reference){
		List<PaiementCnss> result = paiementRepository.getDetailPaiement(reference);
		return result;
	}

}
