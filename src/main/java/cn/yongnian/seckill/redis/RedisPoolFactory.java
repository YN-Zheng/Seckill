package cn.yongnian.seckill.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

/**
 * TODO
 */
@Service
public class RedisPoolFactory {
    @Autowired
    RedisConfig redisConfig;

    @Bean
    public JedisPool JedisFactory() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(redisConfig.getPoolMaxTotal());
        config.setMaxIdle(redisConfig.getPoolMaxIdle());
        config.setMaxWaitMillis(redisConfig.getPoolMaxWait() * 1000);
        JedisPool jedisPool = new JedisPool(config, redisConfig.getHost(),
                redisConfig.getPort(), redisConfig.getTimeout() * 1000,
                redisConfig.getPassword(), 0);

        return jedisPool;

    }


}