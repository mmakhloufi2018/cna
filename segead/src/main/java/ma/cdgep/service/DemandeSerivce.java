package ma.cdgep.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ma.cdgep.demande.dto.DemandeDto;
import ma.cdgep.demande.entity.BlocEntity;
import ma.cdgep.demande.entity.DemandeEntity;
import ma.cdgep.dossier.entity.DossierAsdEntity;
import ma.cdgep.repository.BlocRepository;
import ma.cdgep.repository.DemandeRepository;
import ma.cdgep.repository.DossierAsdRepository;

@Service
public class DemandeSerivce {

	@Autowired
	DemandeRepository demandeRepository;
	
	@Autowired
	DossierAsdRepository dossierAsdRepository;
	
	@Autowired
	BlocRepository blocRepository;

	public Integer saveDemande(DemandeDto demande) {
		demandeRepository.save(DemandeDto.to(demande));
		return 1;
	}

	public void traiterDemandes() {

		boolean finTraitement = false;
		while (!finTraitement) {
			final List<DemandeEntity>  demandeNonTraitees = demandeRepository.getDemandesByStatut(DemandeDto.STATUT_RECEPTIONNE);
			if (demandeNonTraitees == null || demandeNonTraitees.size() == 0)
				finTraitement = true;
			else {
				for (DemandeEntity demande : demandeNonTraitees) {
					switch (demande.getTypeDemande()) {
					case DemandeDto.TYPE_NOUVELLE:
						AjouterDemande(demande);
//						demandeRepository.updateSatutDemande(DemandeDto.STATUT_TRAITE, demande.getReferenceDemande());
						break;
					case DemandeDto.TYPE_ANNULATION:
						AnnulerDemande(demande);
						break;
					default:
						break;
					}
					demande.setStatut(DemandeDto.STATUT_TRAITE);
				}

			}

		}

	}

	private void AnnulerDemande(DemandeEntity demande) {
		// TODO Auto-generated method stub

	}

	private void AjouterDemande(DemandeEntity demande) {
		DossierAsdEntity dossier = DemandeEntity.to(demande);
		dossierAsdRepository.save(dossier);
		
	}
	
	public void creerBloc(String dateTransaction) {
		String[] hashs = blocRepository.getHashTransactions(dateTransaction);
		String b = null;
		int i = 0;
		if(hashs != null && hashs.length > 0)
			for (String hashTr : hashs) {
				b+=hashTr;
				i++;
			}
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] hash = digest.digest(b.getBytes(StandardCharsets.UTF_8));
		blocRepository.save(new BlocEntity(""+hashs.length,dateTransaction,Base64.getEncoder().encodeToString(hash)));
	}
}
