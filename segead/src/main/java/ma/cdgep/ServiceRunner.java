package ma.cdgep;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;

import ma.cdgep.demande.dto.DemandeRejeteeDto;
import ma.cdgep.demande.entity.ConjointEntity;
import ma.cdgep.demande.entity.DemandeEntity;
import ma.cdgep.demande.entity.EnfantEntity;
import ma.cdgep.repository.DemandeRepository;
import ma.cdgep.service.LotSerivce;

//@Service
public class ServiceRunner {

	@Autowired
	LotSerivce lotSerivce;

	@Autowired
	DemandeRepository demandeRepository;

	@Async("taskExecutor")
	public CompletableFuture<Void> run(int i) {
		System.out.println(i);
		this.traiter(i);
		return CompletableFuture.completedFuture(null);
	}

	private void traiter(int i) {
		
		Pageable pageable = PageRequest.of(i, 500);
		org.springframework.data.domain.Page<DemandeEntity> p = demandeRepository.getDemandesByStatut2("RELANCER", pageable);

		System.out.println(i +" =======> :" +p.getContent().size());
		for (DemandeEntity d : p.getContent()) {

			

			List<EnfantEntity> enfs = demandeRepository.getEnfs(d.getDemandeur());
			List<ConjointEntity> conjs = demandeRepository.getConjs(d.getDemandeur());
			List<DemandeRejeteeDto> dmdsrej = lotSerivce.controlerDemande(d.getTypePrestation(), d, enfs, conjs);

			if (!dmdsrej.isEmpty()) {
				String motif = dmdsrej.stream().map(DemandeRejeteeDto::getMotifDemande)
						.collect(Collectors.joining(";"));

				demandeRepository.updateSatutMotifDemande(motif, d.getId());

			} else
				demandeRepository.updateSatutMotifDemande(d.getId());

		}
		
	}

}
