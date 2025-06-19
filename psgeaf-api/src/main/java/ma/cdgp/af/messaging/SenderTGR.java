package ma.cdgp.af.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SenderTGR {

	
	@Autowired
	private RabbitTemplate amqpTemplate;

	@Value("${spring.rabbitmq.queue.notification.tgr}")
	private String queue;

	@Autowired
    public SenderTGR(RabbitTemplate rabbitTemplate) {
        this.amqpTemplate = rabbitTemplate;
    }
	
	public void send(Object obj, String type) {
		System.out.println("TGR ==> sending to : " + queue);
		ObjectMapper mapper = new ObjectMapper();
		String message = "";
		try {
			message = mapper.writeValueAsString(obj);
			this.amqpTemplate.convertAndSend(queue, message, m -> {
				m.getMessageProperties().getHeaders().put("type", type);
				return m;
			});
			System.out.println(" Message envoyé ");

		} catch (JsonProcessingException e) {
			System.out.println("**TGR sender : JsonProcessingException***");
		}
	}

}