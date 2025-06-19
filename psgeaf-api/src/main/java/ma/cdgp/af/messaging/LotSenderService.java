package ma.cdgp.af.messaging;

import ma.cdgp.af.dto.af.rsu.LotDemandeRsuDto;
import ma.cdgp.af.utils.Utils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;

@Service
public class LotSenderService {
	@Autowired
	private RabbitTemplate amqpTemplate;

	@Value("${spring.rabbitmq.queue.asd}")
	private String queueMassar;
	
	@Value("${spring.rabbitmq.queue.asd.notmassar}")
	private String queueNotMassar;
	
	@Value("${spring.rabbitmq.queue.asd.collect}")
	private String queueCollect;
	
	@Value("${spring.rabbitmq.queue.asd.mi}")
	private String queueMi;

	@Value("${spring.rabbitmq.queue.demandes.rsu}")
	private String queueDemandesRsu;

	@Value("${spring.rabbitmq.queue.asd_massar_2025}")
	private String queueMassar2025;


	@Value("${spring.rabbitmq.queue.asd_cnss}")
	private String queueCnss;

	@Value("${spring.rabbitmq.queue.asd_cmr}")
	private String queueCmr;

	@Value("${spring.rabbitmq.queue.asd_tgr}")
	private String queueTgr;

	@Value("${spring.rabbitmq.queue.asd_sante}")
	private String queueSante;

	public void sendLot(Object obj, String type) {
		ObjectMapper mapper = new ObjectMapper();
		String message = "";
		try {
			message = mapper.writeValueAsString(obj);
			this.amqpTemplate.convertAndSend(queueMassar, message, m -> {
				m.getMessageProperties().getHeaders().put("type", type);
				return m;
			});
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}


	public void sendLotMassar2025(Object obj, String type) {
		ObjectMapper mapper = new ObjectMapper();
		String message = "";
		try {
			message = mapper.writeValueAsString(obj);
			this.amqpTemplate.convertAndSend(queueMassar2025, message, m -> {
				m.getMessageProperties().getHeaders().put("type", type);
				return m;
			});
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}



	public void sendDemandesRsuToQueue(Object dto, String type) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			String message = mapper.writeValueAsString(dto);
			this.amqpTemplate.convertAndSend(queueDemandesRsu, message, m -> {
				m.getMessageProperties().getHeaders().put("type", type);
				return m;
			});
//			System.err.println("Lot sent to RabbitMQ: " + " -- " + Utils.dateToStringTimeWithSeconds(new Date()) + "\n");
//			System.err.println("-------------------------------------------------------------");
//			System.err.println("-------------------------------------------------------------");
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
	public void sendLotNotMassar(Object obj, String type) {
		ObjectMapper mapper = new ObjectMapper();
		String message = "";
		try {
			message = mapper.writeValueAsString(obj);
			this.amqpTemplate.convertAndSend(queueNotMassar, message, m -> {
				m.getMessageProperties().getHeaders().put("type", type);
				return m;
			});
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}


	public void sendLotCnss(Object obj, String type) {
		ObjectMapper mapper = new ObjectMapper();
		String message = "";
		try {
			message = mapper.writeValueAsString(obj);
			this.amqpTemplate.convertAndSend(queueCnss, message, m -> {
				m.getMessageProperties().getHeaders().put("type", type);
				return m;
			});
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}


	public void sendLotCmr(Object obj, String type) {
		ObjectMapper mapper = new ObjectMapper();
		String message = "";
		try {
			message = mapper.writeValueAsString(obj);
			this.amqpTemplate.convertAndSend(queueCmr, message, m -> {
				m.getMessageProperties().getHeaders().put("type", type);
				return m;
			});
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}


	public void sendLotTgr(Object obj, String type) {
		ObjectMapper mapper = new ObjectMapper();
		String message = "";
		try {
			message = mapper.writeValueAsString(obj);
			this.amqpTemplate.convertAndSend(queueTgr, message, m -> {
				m.getMessageProperties().getHeaders().put("type", type);
				return m;
			});
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}


	public void sendLotSante(Object obj, String type) {
		ObjectMapper mapper = new ObjectMapper();
		String message = "";
		try {
			message = mapper.writeValueAsString(obj);
			this.amqpTemplate.convertAndSend(queueSante, message, m -> {
				m.getMessageProperties().getHeaders().put("type", type);
				return m;
			});
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
	public void sendLotMi(Object obj, String type) {
		ObjectMapper mapper = new ObjectMapper();
		String message = "";
		try {
			message = mapper.writeValueAsString(obj);
			this.amqpTemplate.convertAndSend(queueMi, message, m -> {
				m.getMessageProperties().getHeaders().put("type", type);
				return m;
			});
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	 
	public void sendLotCollect(Object obj, String type) {
		ObjectMapper mapper = new ObjectMapper();
		String message = "";
		try {
			message = mapper.writeValueAsString(obj);
			this.amqpTemplate.convertAndSend(queueCollect, message, m -> {
				m.getMessageProperties().getHeaders().put("type", type);
				return m;
			});
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
}
