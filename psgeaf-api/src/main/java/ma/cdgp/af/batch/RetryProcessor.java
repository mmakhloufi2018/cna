package ma.cdgp.af.batch;

import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ma.cdgp.af.entity.LotSituationGen;
import ma.cdgp.af.repository.LotSituationGenRepo;
import ma.cdgp.af.service.ExecutorSituationPersonne;

@Component
public class RetryProcessor implements ItemProcessor<LotSituationGen, LotSituationGen> {
	private static final Logger logger = LoggerFactory.getLogger(RetryProcessor.class);

	@Autowired
	LotSituationGenRepo retourRepo;
	private final Map<String, ExecutorSituationPersonne> implementations;

	RetryProcessor(Map<String, ExecutorSituationPersonne> implementations) {
		this.implementations = implementations;
	}
	
	@Override
	public LotSituationGen process(LotSituationGen retour) throws Exception {
		logger.info("RetryProcessor processing retour : " + retour.getId()+" and type "+retour.getType());
		try {
			LotSituationGen reoutDb = retourRepo.findById(retour.getId()).orElse(null);
			if (reoutDb != null &&  reoutDb.getType() != null) {
				implementations.get(retour.getType()).executeWork(reoutDb.getId(), reoutDb.getType());
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return retour;
	}
}
