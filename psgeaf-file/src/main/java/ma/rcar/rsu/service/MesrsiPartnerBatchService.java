package ma.rcar.rsu.service;



import ma.rcar.rsu.dto.mesrsi.FileRequestMesrsiDto;
import ma.rcar.rsu.entity.mesrsi.FileRequestMesrsiEntity;
import ma.rcar.rsu.generic.PartnerBatchService;
import ma.rcar.rsu.repository.mesrsi.FileRequestMesrsiRepository;
import ma.rcar.rsu.service.FileReaderParserServiceMesrsi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;



/**
 * @author BAKHALED Ibrahim.
 *
 */



@Service
public class MesrsiPartnerBatchService implements PartnerBatchService<FileRequestMesrsiDto> {

    @Autowired
    private FileReaderParserServiceMesrsi service;

    @Autowired
    private FileRequestMesrsiRepository repo;

    @Override
    public String getPartnerKey() {
        return "MESRSI";
    }

    @Override
    public List<FileRequestMesrsiDto> checkInboundFiles() {
        return service.checkExistenceInbound();
    }

    @Override
    public void controlFile(FileRequestMesrsiDto fileDto) {
        service.controleFileRequest(fileDto);
    }

    @Override
    public void extractAndArchive(FileRequestMesrsiDto fileDto) {
        service.extractAndAddOutboundLocal(fileDto);
    }

    @Override
    public void saveToDatabase(FileRequestMesrsiDto fileDto) {
        FileRequestMesrsiEntity entity = FileRequestMesrsiEntity.from(fileDto);
        entity.beforeSave();
        repo.save(entity);
    }
}
