package ma.rcar.rsu.service;




import ma.rcar.rsu.dto.onousc.inscription.FileRequestInscriptionDto;
import ma.rcar.rsu.entity.onousc.inscription.FileRequestOnouscInscriptionEntity;
import ma.rcar.rsu.generic.PartnerBatchService;
import ma.rcar.rsu.repository.onousc.inscription.FileRequestInscriptionRepository;
import ma.rcar.rsu.service.FileReaderParserServiceOnousc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;



/**
 * @author BAKHALED Ibrahim.
 *
 */




@Service
public class OnouscInscriptionPartnerBatchService implements PartnerBatchService<FileRequestInscriptionDto> {

    @Autowired
    private FileReaderParserServiceOnousc service;

    @Autowired
    private FileRequestInscriptionRepository repo;

    @Override
    public String getPartnerKey() {
        return "ONOUSC_INSCRIPTION";
    }

    @Override
    public List<FileRequestInscriptionDto> checkInboundFiles() {
        return service.checkExistenceInbound();
    }

    @Override
    public void controlFile(FileRequestInscriptionDto fileDto) {
        service.controleFileRequest(fileDto);
    }

    @Override
    public void extractAndArchive(FileRequestInscriptionDto fileDto) {
        service.extractAndAddOutboundLocal(fileDto);
    }

    @Override
    public void saveToDatabase(FileRequestInscriptionDto fileDto) {
        FileRequestOnouscInscriptionEntity entity = FileRequestOnouscInscriptionEntity.from(fileDto);
        entity.beforeSave();
        repo.save(entity);
    }
}
