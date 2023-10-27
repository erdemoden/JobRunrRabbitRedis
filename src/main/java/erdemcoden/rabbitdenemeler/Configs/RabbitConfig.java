package erdemcoden.rabbitdenemeler.Configs;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Value("${rabbit.posttoall}")
    public String POSTTOALLQUEUE;
    @Value("${dead.posttoall}")
    public String POSTTOALLDLQ;
    @Value("${rabbit.posttoallexc}")
    public String POSTTOALLEXCHANGE;
    @Value("${rabbit.routingerdem}")
    public String ROUTINGERDEM;
    @Value("${rabbit.routingserkan}")
    public String ROUTINGSERKAN;
    @Bean
    public MessageConverter converter(){
        return new Jackson2JsonMessageConverter();
    }
    @Bean
    public RabbitListenerContainerFactory<SimpleMessageListenerContainer>
    myRabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setDefaultRequeueRejected(false);
        return factory;
    }

    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory){
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter());
        return template;
    }

    @Bean
    public Queue postToAll(){
       return QueueBuilder.durable(POSTTOALLQUEUE)
               .withArgument("x-dead-letter-exchange", "")
               .withArgument("x-dead-letter-routing-key", POSTTOALLDLQ)
               .build();
    }

    @Bean Queue erdemQueue(){
        return QueueBuilder.durable(ROUTINGERDEM)
                .withArgument("x-dead-letter-exchange","")
                .withArgument("x-dead-letter-routing-key",POSTTOALLDLQ)
                .build();
    }
    @Bean Queue serkanQueue(){
        return QueueBuilder.durable(ROUTINGSERKAN)
                .withArgument("x-dead-letter-exchange","")
                .withArgument("x-dead-letter-routing-key",POSTTOALLDLQ)
                .build();
    }

    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange("exchange.topic");
    }
    @Bean
    public Binding erdemBinding(Queue erdemQueue,TopicExchange exchange){
        return BindingBuilder.bind(erdemQueue).to(exchange).with(ROUTINGERDEM);
    }
    @Bean
    public Binding serkanBinding(Queue serkanQueue,TopicExchange exchange){
        return BindingBuilder.bind(serkanQueue).to(exchange).with(ROUTINGSERKAN);
    }
    @Bean
    public FanoutExchange postToAllExchange(){
        return new FanoutExchange(POSTTOALLEXCHANGE);
    }

    @Bean
    public Binding posToAllBinding(){
        return BindingBuilder.bind(postToAll()).to(postToAllExchange());
    }
    // Dead Letter Queues
    @Bean Queue postToAllDLQ(){
        return new Queue(POSTTOALLDLQ);
    }


}
