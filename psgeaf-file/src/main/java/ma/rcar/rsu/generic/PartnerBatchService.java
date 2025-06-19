package ma.rcar.rsu.generic;

import java.util.List;




/**
 * @author BAKHALED Ibrahim.
 *
 */



public interface PartnerBatchService<T> {
    String getPartnerKey();
    List<T> checkInboundFiles();
    void controlFile(T fileDto);
    void extractAndArchive(T fileDto);
    void saveToDatabase(T fileDto);
}
