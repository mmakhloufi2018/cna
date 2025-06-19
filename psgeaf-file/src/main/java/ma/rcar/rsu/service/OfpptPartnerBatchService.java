package ma.rcar.rsu.service;


import ma.rcar.rsu.dto.ofppt.FileRequestOfpptDto;
import ma.rcar.rsu.entity.ofppt.FileRequestOfpptEntity;
import ma.rcar.rsu.generic.PartnerBatchService;
import ma.rcar.rsu.repository.ofppt.FileRequestOfpptRepository;
import ma.rcar.rsu.service.FileReaderParserServiceOfppt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;




/**
 * @author BAKHALED Ibrahim.
 *
 */



@Service
public class OfpptPartnerBatchService implements PartnerBatchService<FileRequestOfpptDto> {

    @Autowired
    private FileReaderParserServiceOfppt service;

    @Autowired
    private FileRequestOfpptRepository repo;

    @Override
    public String getPartnerKey() {
        return "OFPPT";
    }

    @Override
    public List<FileRequestOfpptDto> checkInboundFiles() {
        return service.checkExistenceInbound();
    }

    @Override
    public void controlFile(FileRequestOfpptDto fileDto) {
        service.controleFileRequest(fileDto);
    }

    @Override
    public void extractAndArchive(FileRequestOfpptDto fileDto) {
        service.extractAndAddOutbound(fileDto);
    }

    @Override
    public void saveToDatabase(FileRequestOfpptDto fileDto) {
        FileRequestOfpptEntity entity = FileRequestOfpptEntity.from(fileDto);
        entity.beforeSave();
        repo.save(entity);
    }
}
