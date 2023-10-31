package erdemcoden.rabbitdenemeler.Jobs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import erdemcoden.rabbitdenemeler.Configs.RabbitConfig;
import erdemcoden.rabbitdenemeler.Configs.RedisCacheStore;
import erdemcoden.rabbitdenemeler.DTOS.User;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.scheduling.JobScheduler;
import org.jobrunr.spring.annotations.Recurring;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class Jobs {
    private final RabbitTemplate template;

    private final RedisTemplate<String,Object> redisTemplate;

    private final RedisCacheStore cacheStore;

    private final JobScheduler jobScheduler;

    @Value("${checkuser.id}")
    private String checkUserId;

    @Value("${checkuser.cron}")
    private String checkUserCron;
    @PostConstruct
    private void init(){
        jobScheduler.scheduleRecurrently(checkUserId,checkUserCron,()->{
           checkUser();
        });
    }
    private User erdem = new User("erdemoden5@gmail.com","erdem","öden");
    private User serkan = new User("serkan@hotmail.com","serkan","fidancı");

  public void checkUser(){
      System.out.println("Merhaba Dünya");
  }

  @Recurring(id = "erdemListen",cron ="0 * * * *" )
  public void erdemListen(){
        template.convertAndSend("exchange.topic","Erdem","Selam Erdem");
  }
  @Recurring(id = "DenemeRabbit",cron="*/1 * * * *")
  @Job(name = "RabbitDeneme")
    public void rabbitDeneme(){
       template.convertAndSend("PostToAllExchange","","deneme");
  }

  @Recurring(id = "RedisDeneme",cron = "0 * * * *")
    @Job(name = "RedisDeneme")
    public void redisDeneme() throws JsonProcessingException {
      ObjectMapper mapper = new ObjectMapper();
      Gson gson = new Gson();
      cacheStore.put("erdem",gson.toJson(erdem),90);
      cacheStore.put("serkan",gson.toJson(serkan),30);
    //redisTemplate.opsForValue().set("deneme2",gson.toJson(user),90, TimeUnit.SECONDS);
      //String jsonString = (String) redisTemplate.opsForValue().get("deneme2");
      String jsonString = (String) cacheStore.get("erdem");
      System.out.println("JSON String: " + jsonString);
    User user2 = gson.fromJson(jsonString,User.class);
      System.out.println(user2.getMail());
  }
}
