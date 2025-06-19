package ma.rcar.rsu.service;



import ma.rcar.rsu.dto.mhai.FileRequestMhaiDto;
import ma.rcar.rsu.entity.mhai.FileRequestMhaiEntity;
import ma.rcar.rsu.generic.PartnerBatchService;
import ma.rcar.rsu.repository.mhai.FileRequestMhaiRepository;
import ma.rcar.rsu.service.FileReaderParserServiceMhai;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;



/**
 * @author BAKHALED Ibrahim.
 *
 */



@Service
public class MhaiPartnerBatchService implements PartnerBatchService<FileRequestMhaiDto> {

    @Autowired
    private FileReaderParserServiceMhai service;

    @Autowired
    private FileRequestMhaiRepository repo;

    @Override
    public String getPartnerKey() {
        return "MHAI";
    }

    @Override
    public List<FileRequestMhaiDto> checkInboundFiles() {
        return service.checkExistenceInbound();
    }

    @Override
    public void controlFile(FileRequestMhaiDto fileDto) {
        service.controleFileRequest(fileDto);
    }

    @Override
    public void extractAndArchive(FileRequestMhaiDto fileDto) {
        service.extractAndAddOutbound(fileDto);
    }

    @Override
    public void saveToDatabase(FileRequestMhaiDto fileDto) {
        FileRequestMhaiEntity entity = FileRequestMhaiEntity.from(fileDto);
        entity.beforeSave();
        repo.save(entity);
    }
}
