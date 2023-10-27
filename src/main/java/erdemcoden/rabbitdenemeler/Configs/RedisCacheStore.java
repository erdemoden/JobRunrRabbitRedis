package erdemcoden.rabbitdenemeler.Configs;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class RedisCacheStore {

    private final RedisTemplate template;

    public void put(Object key,Object value,long expiration){
        template.opsForValue().set(key,value,expiration,TimeUnit.SECONDS);
    }

    public <T> T get(Object key){
        return (T) template.opsForValue().get(key);
    }

}
