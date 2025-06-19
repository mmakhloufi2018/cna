package ma.rcar.rsu.service;

import ma.rcar.rsu.dto.fef.FileRequestFefDto;
import ma.rcar.rsu.entity.fef.FileRequestFefEntity;
import ma.rcar.rsu.generic.PartnerBatchService;
import ma.rcar.rsu.repository.fef.FileRequestFefRepository;
import ma.rcar.rsu.service.FileReaderParserServiceFef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;




/**
 * @author BAKHALED Ibrahim.
 *
 */




@Service
public class FefPartnerBatchService implements PartnerBatchService<FileRequestFefDto> {

    @Autowired
    FileReaderParserServiceFef service;

    @Autowired
    FileRequestFefRepository repo;

    @Override
    public String getPartnerKey() {
        return "FEF";
    }

    @Override
    public List<FileRequestFefDto> checkInboundFiles() {
        return service.checkExistenceInbound();
    }

    @Override
    public void controlFile(FileRequestFefDto fileDto) {
        service.controleFileRequest(fileDto);
    }

    @Override
    public void extractAndArchive(FileRequestFefDto fileDto) {
        service.extractAndAddOutboundLocal(fileDto);
    }

    @Override
    public void saveToDatabase(FileRequestFefDto fileDto) {
        FileRequestFefEntity entity = FileRequestFefEntity.from(fileDto);
        entity.beforeSave();
        repo.save(entity);
    }
}

