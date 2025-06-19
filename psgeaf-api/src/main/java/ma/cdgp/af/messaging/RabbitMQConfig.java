//package ma.cdgp.af.messaging;
//import org.springframework.amqp.core.MessageListener;
//import org.springframework.amqp.core.Queue;
//import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
//import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class RabbitMQConfig {
//
//
//    @Value("${spring.rabbitmq.queue.asd}")
//    private String queueMassar;
//
//    @Value("${spring.rabbitmq.queue.asd_massar_2025}")
//    private String getQueueMassar2025;
//
//
//
//    @Bean
//    public Queue queueDemandesRsu() {
//        return new Queue("QUEUE_DEMANDES_RSU", true);
//    }
//
//    @Bean
//    public Queue queueNotMassar(){
//        return new Queue("QUEUE_ASD_NOT_MASSAR", true);
//    }
//
//    @Bean
//    public Queue queueAsdCollect(){
//        return new Queue("QUEUE_ASD_COLLECT", true);
//    }
//
//    @Bean
//    public Queue queueMi(){
//        return new Queue("QUEUE_ASD_MI", true);
//    }
//
//
//    @Bean
//    public Queue queue() {
//        return new Queue(queueMassar, true);
//    }
//
//
//    @Bean
//    public Queue queueMassar2025() {
//        return new Queue(getQueueMassar2025, true);
//    }
//
//
//    @Bean
//    public Queue queueCmr(){
//        return new Queue("QUEUE_ASD_CMR", true);
//    }
//
//    @Bean
//    public Queue queueCnss(){
//        return new Queue("QUEUE_ASD_CNSS", true);
//    }
//
//    @Bean
//    public Queue queueTgr(){
//        return new Queue("QUEUE_ASD_TGR", true);
//    }
//
//    @Bean
//    public Queue queueSante(){
//        return new Queue("QUEUE_ASD_SANTE", true);
//    }
//
//
//    @Bean
//    public SimpleMessageListenerContainer container1(
//            SimpleRabbitListenerContainerFactory factory,
//            MessageListener messageListener) {
//        SimpleMessageListenerContainer container = factory.createListenerContainer();
//        container.setQueueNames(queueMassar);
//        container.setMessageListener(messageListener);
//        return container;
//    }
//
//
//    @Bean
//    public SimpleMessageListenerContainer container2(
//            SimpleRabbitListenerContainerFactory factory,
//            MessageListener messageListener) {
//        SimpleMessageListenerContainer container = factory.createListenerContainer();
//        container.setQueueNames(queueMassar);
//        container.setMessageListener(messageListener);
//        return container;
//    }
//
//    @Bean
//    public SimpleMessageListenerContainer container3(
//            SimpleRabbitListenerContainerFactory factory,
//            MessageListener messageListener) {
//        SimpleMessageListenerContainer container = factory.createListenerContainer();
//        container.setQueueNames(queueMassar);
//        container.setMessageListener(messageListener);
//        return container;
//    }
//
//}
