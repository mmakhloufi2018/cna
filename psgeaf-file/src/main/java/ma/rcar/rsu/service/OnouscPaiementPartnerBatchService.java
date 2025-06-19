package ma.rcar.rsu.service;


import ma.rcar.rsu.dto.onousc.paiement.FileRequestPaiementDto;
import ma.rcar.rsu.entity.onousc.paiement.FileRequestOnouscPaiementEntity;
import ma.rcar.rsu.generic.PartnerBatchService;
import ma.rcar.rsu.repository.onousc.paiement.FileRequestPaiementRepository;
import ma.rcar.rsu.service.FileReaderParserServiceOnouscPaiement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;




/**
 * @author BAKHALED Ibrahim.
 *
 */




@Service
public class OnouscPaiementPartnerBatchService implements PartnerBatchService<FileRequestPaiementDto> {

    @Autowired
    private FileReaderParserServiceOnouscPaiement service;

    @Autowired
    private FileRequestPaiementRepository repo;

    @Override
    public String getPartnerKey() {
        return "ONOUSC_PAIEMENT";
    }

    @Override
    public List<FileRequestPaiementDto> checkInboundFiles() {
        return service.checkExistenceInbound();
    }

    @Override
    public void controlFile(FileRequestPaiementDto fileDto) {
        service.controleFileRequest(fileDto);
    }

    @Override
    public void extractAndArchive(FileRequestPaiementDto fileDto) {
        service.extractAndAddOutboundLocal(fileDto);
    }

    @Override
    public void saveToDatabase(FileRequestPaiementDto fileDto) {
        FileRequestOnouscPaiementEntity entity = FileRequestOnouscPaiementEntity.from(fileDto);
        entity.beforeSave();
        repo.save(entity);
    }
}

