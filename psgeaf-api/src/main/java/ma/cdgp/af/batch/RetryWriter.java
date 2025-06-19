package ma.cdgp.af.batch;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ma.cdgp.af.entity.LotSituationGen;
import ma.cdgp.af.repository.LotSituationGenRepo;

@Component
public class RetryWriter implements ItemWriter<LotSituationGen> {
	private static final Logger logger = LoggerFactory.getLogger(RetryWriter.class);

	@Autowired
	LotSituationGenRepo retourRepo;
	@Override
	public void write(List<? extends LotSituationGen> list) throws Exception {
	        if(list != null && !list.isEmpty()) {
	        	for (LotSituationGen retour : list) {
	        		logger.info("RetryWriter writing  retour : " + retour.getId()+" and type "+retour.getType());
				}
	        }
	}
}