package erdemcoden.rabbitdenemeler.Consumers;

import com.google.gson.Gson;
import erdemcoden.rabbitdenemeler.Configs.RedisCacheStore;
import erdemcoden.rabbitdenemeler.DTOS.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AllConsumers {
    private final RedisCacheStore cacheStore;

    private final Gson gson;
    private static final Logger log = LoggerFactory.getLogger(AllConsumers.class);
    @RabbitListener(queues = "${rabbit.posttoall}")
    public void deneme(String message){
        System.out.println("Message : "+message);
    }
    @RabbitListener(queues = "${rabbit.routingerdem}")
    public void erdemConsumer(String message){
        try {
            User user = gson.fromJson((String) cacheStore.get("erdem"),User.class);
            if (user == null) {
                log.error("Kullanıcı cacheden silinmiş !");
                throw new AmqpRejectAndDontRequeueException("User not found in cacheStore");
            }
            log.info("Gelen Mesaj : {}",message);
        }
        catch (AmqpRejectAndDontRequeueException e){
            log.error("Kullanıcı cacheden silinmiş !");
            throw e;
        }
    }
}
