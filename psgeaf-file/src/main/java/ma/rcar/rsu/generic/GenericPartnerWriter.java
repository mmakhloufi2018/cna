package ma.rcar.rsu.generic;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
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
public class GenericPartnerWriter implements ItemWriter<Object> {

    @Value("#{jobParameters['partner']}")
    private String partner;

    @Autowired
    private List<PartnerBatchService<?>> partnerServices;

    @Override
    public void write(Chunk<? extends Object> chunk) throws Exception {
        PartnerBatchService<Object> service = getPartnerService();
        for (Object item : chunk) {
            service.saveToDatabase(item);
        }
    }

    @SuppressWarnings("unchecked")
    private PartnerBatchService<Object> getPartnerService() {
        return (PartnerBatchService<Object>) partnerServices.stream()
                .filter(s -> s.getPartnerKey().equalsIgnoreCase(partner))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No matching partner service found for key: " + partner));
    }
}