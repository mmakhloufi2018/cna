package ma.cdgp.af.messaging;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import ma.cdgp.af.dto.af.rsu.LotDemandeRsuDto;
import ma.cdgp.af.service.ServiceDemandeRsuSender;
import ma.cdgp.af.utils.Utils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParseException;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;

import ma.cdgp.af.entity.CandidatCollected;
import ma.cdgp.af.repository.CandidatCollectedRepo;
import ma.cdgp.af.service.ExecutorSituationPersonne;
import ma.cdgp.af.service.RequestMassarExecutor;

@Component
public class LotReceiverService {

	@Autowired
	RequestMassarExecutor massarExec;

	@Autowired
	CandidatCollectedRepo candidatCollectedRepo;

	@Autowired
	ServiceDemandeRsuSender serviceDemandeRsuSender;

	private final Map<String, ExecutorSituationPersonne> implementations;

	LotReceiverService(Map<String, ExecutorSituationPersonne> implementations) {
		this.implementations = implementations;
	}


	@RabbitListener(queues = "${spring.rabbitmq.queue.asd}",concurrency = "3")
	public void receivedMsg(Message message) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		mapper.setVisibility(
				VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
		try {
			LotRabbitDto lot = mapper.readValue(message.getBody(), LotRabbitDto.class);
			if (lot != null) {
				System.err.println("Lot "+lot.getType()+" received " + lot.getIdLot());
				if (lot != null &&  lot.getType() != null) {
					implementations.get(lot.getType()).executeWork(Long.valueOf(lot.getIdLot()), lot.getType());
					System.err.println("Lot "+lot.getType()+" treated " + lot.getIdLot());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


//	@RabbitListener(queues = "${spring.rabbitmq.queue.asd_massar_2025}",concurrency = "8")
//	public void receivedMassar2025(Message message) throws JsonParseException, JsonMappingException, IOException {
//		ObjectMapper mapper = new ObjectMapper();
//		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//		mapper.setVisibility(
//				VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
//		try {
//			LotRabbitDto lot = mapper.readValue(message.getBody(), LotRabbitDto.class);
//			if (lot != null) {
//				System.err.println("Lot "+lot.getType()+" received " + lot.getIdLot());
//				if (lot != null &&  lot.getType() != null) {
//					implementations.get(lot.getType()).executeWork(Long.valueOf(lot.getIdLot()), lot.getType());
//					System.err.println("Lot "+lot.getType()+" treated " + lot.getIdLot());
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

//	@RabbitListener(queues = "${spring.rabbitmq.queue.asd.notmassar}",concurrency = "3")
//	public void receivedMsgNotMassar(Message message) throws JsonParseException, JsonMappingException, IOException {
//		ObjectMapper mapper = new ObjectMapper();
//		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//		mapper.setVisibility(
//				VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
//		try {
//			LotRabbitDto lot = mapper.readValue(message.getBody(), LotRabbitDto.class);
//			if (lot != null) {
//				System.err.println("Lot "+lot.getType()+" received " + lot.getIdLot());
//				if (lot != null &&  lot.getType() != null) {
//					implementations.get(lot.getType()).executeWork(Long.valueOf(lot.getIdLot()), lot.getType());
//					System.err.println("Lot "+lot.getType()+" treated " + lot.getIdLot());
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}


	@RabbitListener(queues = "${spring.rabbitmq.queue.asd_cmr}",concurrency = "1")
	public void receivedMsgCmr(Message message) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		mapper.setVisibility(
				VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
		try {
			LotRabbitDto lot = mapper.readValue(message.getBody(), LotRabbitDto.class);
			if (lot != null) {
				System.err.println("Lot "+lot.getType()+" received " + lot.getIdLot());
				if (lot != null &&  lot.getType() != null) {
					implementations.get(lot.getType()).executeWork(Long.valueOf(lot.getIdLot()), lot.getType());
					System.err.println("Lot "+lot.getType()+" treated " + lot.getIdLot());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@RabbitListener(queues = "${spring.rabbitmq.queue.asd_cnss}",concurrency = "1")
	public void receivedMsgCnss(Message message) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		mapper.setVisibility(
				VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
		try {
			LotRabbitDto lot = mapper.readValue(message.getBody(), LotRabbitDto.class);
			if (lot != null) {
				System.err.println("Lot "+lot.getType()+" received " + lot.getIdLot());
				if (lot != null &&  lot.getType() != null) {
					implementations.get(lot.getType()).executeWork(Long.valueOf(lot.getIdLot()), lot.getType());
					System.err.println("Lot "+lot.getType()+" treated " + lot.getIdLot());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@RabbitListener(queues = "${spring.rabbitmq.queue.asd_tgr}",concurrency = "3")
	public void receivedMsgTgr(Message message) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		mapper.setVisibility(
				VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
		try {
			LotRabbitDto lot = mapper.readValue(message.getBody(), LotRabbitDto.class);
			if (lot != null) {
				System.err.println("Lot "+lot.getType()+" received " + lot.getIdLot());
				if (lot != null &&  lot.getType() != null) {
					implementations.get(lot.getType()).executeWork(Long.valueOf(lot.getIdLot()), lot.getType());
					System.err.println("Lot "+lot.getType()+" treated " + lot.getIdLot());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@RabbitListener(queues = "${spring.rabbitmq.queue.asd_sante}",concurrency = "3")
	public void receivedMsgSante(Message message) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		mapper.setVisibility(
				VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
		try {
			LotRabbitDto lot = mapper.readValue(message.getBody(), LotRabbitDto.class);
			if (lot != null) {
				System.err.println("Lot "+lot.getType()+" received " + lot.getIdLot());
				if (lot != null &&  lot.getType() != null) {
					implementations.get(lot.getType()).executeWork(Long.valueOf(lot.getIdLot()), lot.getType());
					System.err.println("Lot "+lot.getType()+" treated " + lot.getIdLot());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//
//
//	@RabbitListener(queues = "${spring.rabbitmq.queue.asd.mi}",concurrency = "10")
//	public void receivedMsgMi(Message message) throws JsonParseException, JsonMappingException, IOException {
//		ObjectMapper mapper = new ObjectMapper();
//		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//		mapper.setVisibility(
//				VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
//		try {
//			LotRabbitDto lot = mapper.readValue(message.getBody(), LotRabbitDto.class);
//			if (lot != null) {
//				System.err.println("Lot "+lot.getType()+" received " + lot.getIdLot());
//				if (lot != null &&  lot.getType() != null) {
//					implementations.get(lot.getType()).executeWork(Long.valueOf(lot.getIdLot()), lot.getType());
//					System.err.println("Lot "+lot.getType()+" treated " + lot.getIdLot());
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
	@RabbitListener(queues = "${spring.rabbitmq.queue.asd.collect}",concurrency = "3")
	public void receivedMsgCollect(Message message) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		mapper.setVisibility(
				VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
		try {
			LotCollRabbitDto lot = mapper.readValue(message.getBody(), LotCollRabbitDto.class);
			if (lot != null && lot.getSubs() != null) {
				System.err.println("Lot "+lot.getReference()+" received ");
				List<CandidatCollected> listeColleted = lot.getSubs().stream()
						.map(t -> CandidatCollected.fromCandidatInfo(t, lot.getReference()))
						.collect(Collectors.toList());
				candidatCollectedRepo.saveAll(listeColleted);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@RabbitListener(queues = "${spring.rabbitmq.queue.demandes.rsu}" ,concurrency = "3")
	public void receiveMessage(@Payload String messageContent) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			LotRabitDemandesRsuDto lotRabitDemandesRsuDto = mapper.readValue(messageContent, LotRabitDemandesRsuDto.class);
			Long idRsu = Long.valueOf(lotRabitDemandesRsuDto.getIdLot());
			LotDemandeRsuDto lotDemandeRsuDto = lotRabitDemandesRsuDto.getLotDemandeRsuDto();
			String type = lotRabitDemandesRsuDto.getType();

			serviceDemandeRsuSender.sendDemandesRsu(idRsu, lotDemandeRsuDto, type);
//			System.err.println("Consumed successfully from rabbitmq -- "  +  Utils.dateToStringTimeWithSeconds(new Date()) + "\n");
//			System.err.println("-------------------------------------------------------------");
//			System.err.println("-------------------------------------------------------------");

		} catch (Exception e) {
			e.printStackTrace();

		}
	}
}


