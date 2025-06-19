package ma.rcar.rsu.generic;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;





/**
 * @author BAKHALED Ibrahim.
 *
 */



@Component
@StepScope
public class GenericPartnerProcessor implements ItemProcessor<Object, Object> {

    @Value("#{jobParameters['partner']}")  private String partner;
    @Autowired  private List<PartnerBatchService<?>> partnerServices;

    @Override
    public Object process(Object item) throws Exception {
        PartnerBatchService<Object> service = getPartnerService();
        service.controlFile(item);
        service.extractAndArchive(item);
        return item;
    }

    @SuppressWarnings("unchecked")
    private PartnerBatchService<Object> getPartnerService() {
        return (PartnerBatchService<Object>) partnerServices.stream()
                .filter(s -> s.getPartnerKey().equalsIgnoreCase(partner))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Partner not found"));
    }
}
