package ma.rcar.rsu.generic;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;




/**
 * @author BAKHALED Ibrahim.
 *
 */



@Component
@StepScope
public class GenericPartnerReader implements ItemReader<Object> {

    @Value("#{jobParameters['partner']}")
    private String partner;

    @Autowired
    private List<PartnerBatchService<?>> partnerServices;

    private Queue<Object> fileQueue = new LinkedList<>();

    @Override
    public Object read() {
        if (fileQueue.isEmpty()) {
            PartnerBatchService<?> service = getPartnerService();
            List<?> files = service.checkInboundFiles();
            fileQueue.addAll(files);
        }
        return fileQueue.poll();
    }

    private PartnerBatchService<?> getPartnerService() {
        return partnerServices.stream()
                .filter(s -> s.getPartnerKey().equalsIgnoreCase(partner))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Partner not found"));
    }
}
